package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CritPayload(boolean crit) implements CustomPayload {
    public static final CustomPayload.Id<CritPayload> TYPE = new CustomPayload.Id<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "crit"));
    public static final PacketCodec<RegistryByteBuf, CritPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, CritPayload::crit,
            CritPayload::new
    );

    @Override
    public CustomPayload.Id<CritPayload> getId() {
        return TYPE;
    }
}
