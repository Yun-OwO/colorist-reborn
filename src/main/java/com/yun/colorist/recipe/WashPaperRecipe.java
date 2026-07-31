package com.yun.colorist.recipe;

import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.placement.PlacementInfo;
import net.minecraft.world.level.Level;

public class WashPaperRecipe implements CraftingRecipe {

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack paper = ItemStack.EMPTY;
        boolean hasCrystal = false;
        boolean hasWhiteDye = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ModItems.MAGIC_PAPER)) paper = stack;
            else if (stack.is(ModItems.MAGIC_CRYSTAL)) hasCrystal = true;
            else if (stack.is(Items.WHITE_DYE)) hasWhiteDye = true;
        }
        return !paper.isEmpty() && hasCrystal && hasWhiteDye;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack paper = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ModItems.MAGIC_PAPER)) {
                paper = stack;
                break;
            }
        }
        int level = 1;
        if (!paper.isEmpty()) {
            MagicPaperData data = paper.getOrDefault(ModComponents.MAGIC_PAPER, MagicPaperData.DEFAULT);
            level = data.level();
        }
        ItemStack result = new ItemStack(ModItems.MAGIC_PAPER);
        result.set(ModComponents.MAGIC_PAPER, new MagicPaperData(level, "#FFFFFF"));
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        ItemStack stack = new ItemStack(ModItems.MAGIC_PAPER);
        stack.set(ModComponents.MAGIC_PAPER, new MagicPaperData(1, "#FFFFFF"));
        return stack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WASH_PAPER_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WASH_PAPER_TYPE;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }
}
