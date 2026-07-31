package com.yun.colorist.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record MagicPaperData(int level, String attr) {

    public static final Codec<MagicPaperData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("level").forGetter(MagicPaperData::level),
            Codec.STRING.fieldOf("attr").forGetter(MagicPaperData::attr)
    ).apply(instance, MagicPaperData::new));

    public static final PacketCodec<ByteBuf, MagicPaperData> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, MagicPaperData::level,
            PacketCodecs.STRING, MagicPaperData::attr,
            MagicPaperData::new
    );

    public static final MagicPaperData DEFAULT = new MagicPaperData(1, "#FFFFFF");
}
