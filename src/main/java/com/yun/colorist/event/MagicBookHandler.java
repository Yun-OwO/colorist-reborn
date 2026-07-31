package com.yun.colorist.event;

import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.registry.ModPayloads;
import com.yun.colorist.util.AttrUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MagicBookHandler {

    private static final Map<UUID, CastData> CASTING = new HashMap<>();
    private static final Map<UUID, Boolean> DAMAGED = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                tickPlayer(player);
            }
        });
    }

    private static void tickPlayer(ServerPlayerEntity player) {
        CastData data = CASTING.get(player.getUuid());
        if (data == null) return;
        long now = player.getServerWorld().getTime();
        if (now - data.startTime > 10) {
            CASTING.remove(player.getUuid());
            DAMAGED.remove(player.getUuid());
            ModPayloads.sendMagicStop(player);
            return;
        }
        if (DAMAGED.getOrDefault(player.getUuid(), false)) return;
        HitResult hit = player.raycast(10, 1f, false);
        if (hit instanceof EntityHitResult entityHit) {
            Entity target = entityHit.getEntity();
            if (target instanceof LivingEntity living) {
                ItemStack stack = player.getMainHandStack();
                if (!stack.isOf(ModItems.MAGIC_BOOK)) return;
                MagicBookData bookData = stack.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
                MagicAttrData attr = bookData.attr();
                AttrUtil.BasicAttr value = AttrUtil.calculateValue(attr, false);
                double damage = value.atk();
                boolean crit = Math.random() < value.br();
                if (crit) {
                    damage *= value.bd();
                    player.sendMessage(net.minecraft.text.Text.translatable("message.colorist.crit"), false);
                }
                ModPayloads.sendCrit(player, crit);
                living.damage(player.getServerWorld().getDamageSources().playerAttack(player), (float) damage);
                DAMAGED.put(player.getUuid(), true);
            }
        }
    }

    public static void startCast(ServerPlayerEntity player) {
        CASTING.put(player.getUuid(), new CastData(player.getServerWorld().getTime()));
        DAMAGED.put(player.getUuid(), false);
    }

    private static class CastData {
        final long startTime;

        CastData(long startTime) {
            this.startTime = startTime;
        }
    }
}
