package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MagicStartPayload(float r, float g, float b) implements CustomPayload {
    public static final CustomPayload.Id<MagicStartPayload> TYPE = new CustomPayload.Id<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_start"));
    public static final PacketCodec<RegistryByteBuf, MagicStartPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, MagicStartPayload::r,
            PacketCodecs.FLOAT, MagicStartPayload::g,
            PacketCodecs.FLOAT, MagicStartPayload::b,
            MagicStartPayload::new
    );

    @Override
    public CustomPayload.Id<MagicStartPayload> getId() {
        return TYPE;
    }
}
