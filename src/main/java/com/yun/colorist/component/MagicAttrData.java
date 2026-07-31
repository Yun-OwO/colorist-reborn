package com.yun.colorist.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MagicAttrData(int r, int g, int b, int brightness, int darkness, int level, String color) {

    public static final Codec<MagicAttrData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("r").forGetter(MagicAttrData::r),
            Codec.INT.fieldOf("g").forGetter(MagicAttrData::g),
            Codec.INT.fieldOf("b").forGetter(MagicAttrData::b),
            Codec.INT.fieldOf("brightness").forGetter(MagicAttrData::brightness),
            Codec.INT.fieldOf("darkness").forGetter(MagicAttrData::darkness),
            Codec.INT.fieldOf("level").forGetter(MagicAttrData::level),
            Codec.STRING.fieldOf("color").forGetter(MagicAttrData::color)
    ).apply(instance, MagicAttrData::new));

    public static final StreamCodec<ByteBuf, MagicAttrData> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MagicAttrData::r,
            ByteBufCodecs.VAR_INT, MagicAttrData::g,
            ByteBufCodecs.VAR_INT, MagicAttrData::b,
            ByteBufCodecs.VAR_INT, MagicAttrData::brightness,
            ByteBufCodecs.VAR_INT, MagicAttrData::darkness,
            ByteBufCodecs.VAR_INT, MagicAttrData::level,
            ByteBufCodecs.STRING_UTF8, MagicAttrData::color,
            MagicAttrData::new
    );

    public static final MagicAttrData DEFAULT = new MagicAttrData(0, 0, 0, 0, 0, 0, "#FFFFFF");
}
