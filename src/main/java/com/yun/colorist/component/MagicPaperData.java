package com.yun.colorist.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MagicPaperData(int level, String attr) {

    public static final Codec<MagicPaperData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("level").forGetter(MagicPaperData::level),
            Codec.STRING.fieldOf("attr").forGetter(MagicPaperData::attr)
    ).apply(instance, MagicPaperData::new));

    public static final StreamCodec<ByteBuf, MagicPaperData> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MagicPaperData::level,
            ByteBufCodecs.STRING_UTF8, MagicPaperData::attr,
            MagicPaperData::new
    );

    public static final MagicPaperData DEFAULT = new MagicPaperData(1, "#FFFFFF");
}
