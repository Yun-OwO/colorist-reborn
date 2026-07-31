package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MagicStopPayload() implements CustomPayload {
    public static final CustomPayload.Id<MagicStopPayload> TYPE = new CustomPayload.Id<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_stop"));
    public static final PacketCodec<RegistryByteBuf, MagicStopPayload> CODEC = PacketCodec.unit(new MagicStopPayload());

    @Override
    public CustomPayload.Id<MagicStopPayload> getId() {
        return TYPE;
    }
}
