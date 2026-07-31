package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MagicStartPayload(float r, float g, float b) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MagicStartPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_start"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MagicStartPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, MagicStartPayload::r,
            ByteBufCodecs.FLOAT, MagicStartPayload::g,
            ByteBufCodecs.FLOAT, MagicStartPayload::b,
            MagicStartPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
