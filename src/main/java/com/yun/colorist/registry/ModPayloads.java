package com.yun.colorist.registry;

import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.network.CritPayload;
import com.yun.colorist.network.MagicStartPayload;
import com.yun.colorist.network.MagicStopPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ModPayloads {

    public static void initialize() {
        // Register S2C payloads (server -> client)
        PayloadTypeRegistry.clientboundPlay().register(MagicStartPayload.TYPE, MagicStartPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(MagicStopPayload.TYPE, MagicStopPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(CritPayload.TYPE, CritPayload.CODEC);
    }

    public static void sendMagicStart(ServerPlayer player, MagicAttrData attr) {
        ServerPlayNetworking.send(player, new MagicStartPayload(attr.r() / 10f, attr.g() / 10f, attr.b() / 10f));
    }

    public static void sendMagicStart(Player player, MagicAttrData attr) {
        if (player instanceof ServerPlayer serverPlayer) {
            sendMagicStart(serverPlayer, attr);
        }
    }

    public static void sendMagicStop(ServerPlayer player) {
        ServerPlayNetworking.send(player, new MagicStopPayload());
    }

    public static void sendCrit(ServerPlayer player, boolean crit) {
        ServerPlayNetworking.send(player, new CritPayload(crit));
    }
}
