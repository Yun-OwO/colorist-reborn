package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MagicStopPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MagicStopPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_stop"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MagicStopPayload> CODEC = StreamCodec.unit(new MagicStopPayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
