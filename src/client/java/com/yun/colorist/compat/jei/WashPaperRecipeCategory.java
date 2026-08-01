package com.yun.colorist.compat.jei;

import com.yun.colorist.Colorist;
import com.yun.colorist.registry.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WashPaperRecipeCategory implements IRecipeCategory<WashPaperRecipeCategory.WashPaperRecipe> {

    public static final IRecipeType<WashPaperRecipe> TYPE = IRecipeType.create(
            Colorist.MOD_ID, "wash_paper", WashPaperRecipe.class
    );

    private static final int WIDTH = 116;
    private static final int HEIGHT = 54;

    private final IDrawable icon;

    public WashPaperRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.MAGIC_PAPER));
    }

    @Override
    public IRecipeType<WashPaperRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.colorist.wash_paper.title");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WashPaperRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addItemStack(recipe.input1());
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 1)
                .addItemStack(recipe.input2());
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 37)
                .addItemStack(recipe.input3());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19)
                .addItemStack(recipe.output());
    }

    public static WashPaperRecipe createRecipe() {
        return new WashPaperRecipe(
                new ItemStack(ModItems.MAGIC_PAPER),
                new ItemStack(ModItems.MAGIC_CRYSTAL),
                new ItemStack(Items.WHITE_DYE),
                new ItemStack(ModItems.MAGIC_PAPER)
        );
    }

    public record WashPaperRecipe(ItemStack input1, ItemStack input2, ItemStack input3, ItemStack output) {
    }
}