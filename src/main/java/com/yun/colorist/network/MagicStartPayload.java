package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPayload;
import net.minecraft.resources.Identifier;

public record MagicStartPayload(float r, float g, float b) implements CustomPayload {
    public static final CustomPayload.Type<MagicStartPayload> TYPE = new CustomPayload.Type<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_start"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MagicStartPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, MagicStartPayload::r,
            ByteBufCodecs.FLOAT, MagicStartPayload::g,
            ByteBufCodecs.FLOAT, MagicStartPayload::b,
            MagicStartPayload::new
    );

    @Override
    public CustomPayload.Type<? extends CustomPayload> type() {
        return TYPE;
    }
}
