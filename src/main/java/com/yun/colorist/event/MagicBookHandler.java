package com.yun.colorist.event;

import com.yun.colorist.Colorist;
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
        Colorist.LOGGER.debug("Registering MagicBookHandler tick listener");
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                tickPlayer(player);
            }
        });
    }

    private static void tickPlayer(ServerPlayer player) {
        CastData data = CASTING.get(player.getUUID());
        if (data == null) return;
        long now = player.level().getGameTime();
        Colorist.LOGGER.debug("Casting tick for {}: elapsed={}", player.getName().getString(), now - data.startTime);
        if (now - data.startTime > 10) {
            Colorist.LOGGER.debug("Cast finished for {}", player.getName().getString());
            CASTING.remove(player.getUUID());
            DAMAGED.remove(player.getUUID());
            ModPayloads.sendMagicStop(player);
            return;
        }
        if (DAMAGED.getOrDefault(player.getUUID(), false)) {
            Colorist.LOGGER.debug("Already damaged for this cast");
            return;
        }
        HitResult hit = player.pick(10, 1f, false);
        if (hit instanceof EntityHitResult entityHit) {
            Entity target = entityHit.getEntity();
            if (target instanceof LivingEntity living) {
                ItemStack stack = player.getMainHandItem();
                if (!stack.is(ModItems.MAGIC_BOOK)) {
                    Colorist.LOGGER.debug("Player no longer holding magic book");
                    return;
                }
                MagicBookData bookData = stack.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
                MagicAttrData attr = bookData.attr();
                AttrUtil.BasicAttr value = AttrUtil.calculateValue(attr, false);
                double damage = value.atk();
                boolean crit = Math.random() < value.br();
                if (crit) {
                    damage *= value.bd();
                    player.displayClientMessage(Component.translatable("message.colorist.crit"), false);
                    Colorist.LOGGER.info("CRIT! {} hit {} for {} damage", player.getName().getString(), living.getName().getString(), damage);
                } else {
                    Colorist.LOGGER.debug("{} hit {} for {} damage", player.getName().getString(), living.getName().getString(), damage);
                }
                ModPayloads.sendCrit(player, crit);
                living.hurt(player.level().damageSources().playerAttack(player), (float) damage);
                DAMAGED.put(player.getUUID(), true);
            } else {
                Colorist.LOGGER.debug("Hit entity is not living: {}", target);
            }
        } else {
            Colorist.LOGGER.debug("No entity hit during cast");
        }
    }

    public static void startCast(ServerPlayer player) {
        Colorist.LOGGER.info("Starting cast for {}", player.getName().getString());
        CASTING.put(player.getUUID(), new CastData(player.level().getGameTime()));
        DAMAGED.put(player.getUUID(), false);
    }

    private static class CastData {
        final long startTime;

        CastData(long startTime) {
            this.startTime = startTime;
        }
    }
}
