package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.recipe.WashPaperRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

    public static final RecipeSerializer<?> WASH_PAPER_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "wash_paper"),
            new WashPaperRecipeSerializer()
    );

    public static final RecipeType<?> WASH_PAPER_TYPE = Registry.register(
            Registries.RECIPE_TYPE,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "wash_paper"),
            new RecipeType<>() {}
    );

    public static void initialize() {
    }
}
