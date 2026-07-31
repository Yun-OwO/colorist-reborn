package com.yun.colorist.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;

public class WashPaperRecipeSerializer implements RecipeSerializer<WashPaperRecipe> {
    public static final MapCodec<WashPaperRecipe> CODEC = MapCodec.unit(WashPaperRecipe::new);
    public static final PacketCodec<RegistryByteBuf, WashPaperRecipe> PACKET_CODEC = PacketCodec.unit(new WashPaperRecipe());

    @Override
    public MapCodec<WashPaperRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, WashPaperRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}
