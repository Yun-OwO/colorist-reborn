package com.yun.colorist.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.List;

public record MagicBookData(List<MagicAttrData> attrs, MagicAttrData attr, boolean hasHpBonus) {

    public static final Codec<MagicBookData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MagicAttrData.CODEC.listOf().fieldOf("attrs").forGetter(MagicBookData::attrs),
            MagicAttrData.CODEC.fieldOf("attr").forGetter(MagicBookData::attr),
            Codec.BOOL.fieldOf("hasHpBonus").forGetter(MagicBookData::hasHpBonus)
    ).apply(instance, MagicBookData::new));

    public static final PacketCodec<ByteBuf, MagicBookData> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, MagicAttrData.PACKET_CODEC), MagicBookData::attrs,
            MagicAttrData.PACKET_CODEC, MagicBookData::attr,
            PacketCodecs.BOOLEAN, MagicBookData::hasHpBonus,
            MagicBookData::new
    );

    public static final MagicBookData DEFAULT = new MagicBookData(new ArrayList<>(), MagicAttrData.DEFAULT, false);
}
