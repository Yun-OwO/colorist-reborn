package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.recipe.WashPaperRecipe;
import com.yun.colorist.recipe.WashPaperRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {

    public static final RecipeSerializer<WashPaperRecipe> WASH_PAPER_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "wash_paper"),
            new WashPaperRecipeSerializer()
    );

    public static void initialize() {
    }
}
