package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPayload;
import net.minecraft.resources.Identifier;

public record MagicStopPayload() implements CustomPayload {
    public static final CustomPayload.Type<MagicStopPayload> TYPE = new CustomPayload.Type<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_stop"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MagicStopPayload> CODEC = StreamCodec.unit(new MagicStopPayload());

    @Override
    public CustomPayload.Type<? extends CustomPayload> type() {
        return TYPE;
    }
}
