package com.yun.colorist.registry;

import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.network.CritPayload;
import com.yun.colorist.network.MagicStartPayload;
import com.yun.colorist.network.MagicStopPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModPayloads {

    public static void initialize() {
        // Register S2C payloads (server -> client)
        PayloadTypeRegistry.playS2C().register(MagicStartPayload.TYPE, MagicStartPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MagicStopPayload.TYPE, MagicStopPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CritPayload.TYPE, CritPayload.CODEC);
    }

    public static void sendMagicStart(ServerPlayerEntity player, MagicAttrData attr) {
        ServerPlayNetworking.send(player, new MagicStartPayload(attr.r() / 10f, attr.g() / 10f, attr.b() / 10f));
    }

    public static void sendMagicStart(PlayerEntity player, MagicAttrData attr) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            sendMagicStart(serverPlayer, attr);
        }
    }

    public static void sendMagicStop(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new MagicStopPayload());
    }

    public static void sendCrit(ServerPlayerEntity player, boolean crit) {
        ServerPlayNetworking.send(player, new CritPayload(crit));
    }
}
