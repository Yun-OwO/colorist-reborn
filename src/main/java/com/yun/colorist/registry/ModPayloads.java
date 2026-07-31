package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.network.CritPayload;
import com.yun.colorist.network.MagicStartPayload;
import com.yun.colorist.network.MagicStopPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ModPayloads {

    public static final CustomPayload.Id<MagicStartPayload> MAGIC_START_ID = new CustomPayload.Id<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_start"));
    public static final CustomPayload.Id<MagicStopPayload> MAGIC_STOP_ID = new CustomPayload.Id<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_stop"));
    public static final CustomPayload.Id<CritPayload> CRIT_ID = new CustomPayload.Id<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "crit"));

    public static void initialize() {
        Registry.register(Registries.PACKET_TYPE, Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_start"), MAGIC_START_ID);
        Registry.register(Registries.PACKET_TYPE, Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_stop"), MAGIC_STOP_ID);
        Registry.register(Registries.PACKET_TYPE, Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "crit"), CRIT_ID);

        ServerPlayNetworking.registerGlobalReceiver(MagicStartPayload.TYPE, (payload, context) -> {});
        ServerPlayNetworking.registerGlobalReceiver(MagicStopPayload.TYPE, (payload, context) -> {});
        ServerPlayNetworking.registerGlobalReceiver(CritPayload.TYPE, (payload, context) -> {});
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
