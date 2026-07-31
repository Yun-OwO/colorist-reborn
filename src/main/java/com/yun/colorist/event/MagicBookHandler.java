package com.yun.colorist.event;

import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.registry.ModPayloads;
import com.yun.colorist.util.AttrUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MagicBookHandler {

    private static final Map<UUID, CastData> CASTING = new HashMap<>();
    private static final Map<UUID, Boolean> DAMAGED = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                tickPlayer(player);
            }
        });
    }

    private static void tickPlayer(ServerPlayer player) {
        CastData data = CASTING.get(player.getUUID());
        if (data == null) return;
        long now = player.serverLevel().getGameTime();
        if (now - data.startTime > 10) {
            CASTING.remove(player.getUUID());
            DAMAGED.remove(player.getUUID());
            ModPayloads.sendMagicStop(player);
            return;
        }
        if (DAMAGED.getOrDefault(player.getUUID(), false)) return;
        HitResult hit = player.pick(10, 1f, false);
        if (hit instanceof EntityHitResult entityHit) {
            Entity target = entityHit.getEntity();
            if (target instanceof LivingEntity living) {
                ItemStack stack = player.getMainHandItem();
                if (!stack.is(ModItems.MAGIC_BOOK)) return;
                MagicBookData bookData = stack.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
                MagicAttrData attr = bookData.attr();
                AttrUtil.BasicAttr value = AttrUtil.calculateValue(attr, false);
                double damage = value.atk();
                boolean crit = Math.random() < value.br();
                if (crit) {
                    damage *= value.bd();
                    player.displayClientMessage(Component.translatable("message.colorist.crit"), false);
                }
                ModPayloads.sendCrit(player, crit);
                living.hurt(player.serverLevel().damageSources().playerAttack(player), (float) damage);
                DAMAGED.put(player.getUUID(), true);
            }
        }
    }

    public static void startCast(ServerPlayer player) {
        CASTING.put(player.getUUID(), new CastData(player.serverLevel().getGameTime()));
        DAMAGED.put(player.getUUID(), false);
    }

    private static class CastData {
        final long startTime;

        CastData(long startTime) {
            this.startTime = startTime;
        }
    }
}
