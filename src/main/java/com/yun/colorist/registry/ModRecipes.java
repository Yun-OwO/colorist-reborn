package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.recipe.WashPaperRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {

    public static final RecipeSerializer<?> WASH_PAPER_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "wash_paper"),
            new WashPaperRecipeSerializer()
    );

    public static final RecipeType<?> WASH_PAPER_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "wash_paper"),
            new RecipeType<>() {}
    );

    public static void initialize() {
    }
}
