package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.recipe.WashPaperRecipe;
import com.yun.colorist.recipe.WashPaperRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {

    public static final RecipeSerializer<WashPaperRecipe> WASH_PAPER_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "wash_paper"),
            new WashPaperRecipeSerializer()
    );

    public static final RecipeType<WashPaperRecipe> WASH_PAPER_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "wash_paper"),
            new RecipeType<WashPaperRecipe>() {
                @Override
                public String toString() {
                    return "colorist:wash_paper";
                }
            }
    );

    public static void initialize() {
    }
}
