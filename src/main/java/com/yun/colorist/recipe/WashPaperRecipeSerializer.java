package com.yun.colorist.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WashPaperRecipeSerializer implements RecipeSerializer<WashPaperRecipe> {
    public static final MapCodec<WashPaperRecipe> CODEC = MapCodec.unit(WashPaperRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, WashPaperRecipe> STREAM_CODEC = StreamCodec.unit(new WashPaperRecipe());

    @Override
    public MapCodec<WashPaperRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, WashPaperRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
