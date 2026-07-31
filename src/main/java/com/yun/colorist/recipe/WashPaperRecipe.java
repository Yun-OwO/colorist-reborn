package com.yun.colorist.recipe;

import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.registry.ModRecipes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.List;

public class WashPaperRecipe extends net.minecraft.recipe.CraftingRecipe {

    public WashPaperRecipe() {
        super(net.minecraft.recipe.RecipeCategory.MISC);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack paper = ItemStack.EMPTY;
        boolean hasCrystal = false;
        boolean hasWhiteDye = false;
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isOf(ModItems.MAGIC_PAPER)) paper = stack;
            else if (stack.isOf(ModItems.MAGIC_CRYSTAL)) hasCrystal = true;
            else if (stack.isOf(Items.WHITE_DYE)) hasWhiteDye = true;
        }
        return !paper.isEmpty() && hasCrystal && hasWhiteDye;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack paper = ItemStack.EMPTY;
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isOf(ModItems.MAGIC_PAPER)) {
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
    public boolean fits(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registries) {
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
}
