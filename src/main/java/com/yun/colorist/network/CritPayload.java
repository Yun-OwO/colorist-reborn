package com.yun.colorist.network;

import com.yun.colorist.Colorist;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPayload;
import net.minecraft.resources.Identifier;

public record CritPayload(boolean crit) implements CustomPayload {
    public static final CustomPayload.Type<CritPayload> TYPE = new CustomPayload.Type<>(Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "crit"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CritPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, CritPayload::crit,
            CritPayload::new
    );

    @Override
    public CustomPayload.Type<? extends CustomPayload> type() {
        return TYPE;
    }
}
