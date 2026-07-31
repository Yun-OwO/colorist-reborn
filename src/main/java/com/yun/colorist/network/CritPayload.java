package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CritPayload(boolean crit) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CritPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "crit"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CritPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, CritPayload::crit,
            CritPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
