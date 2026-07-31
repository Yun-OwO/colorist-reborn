package com.yun.colorist.compat.jei;

import com.yun.colorist.Colorist;
import com.yun.colorist.recipe.WashPaperRecipe;
import com.yun.colorist.registry.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WashPaperRecipeCategory implements IRecipeCategory<WashPaperRecipe> {
    public static final RecipeType<WashPaperRecipe> TYPE = RecipeType.create(
            Colorist.MOD_ID, "wash_paper", WashPaperRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public WashPaperRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Identifier.fromNamespaceAndPath("jei", "textures/jei/gui/gui_vanilla.png"), 0, 60, 116, 54);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.MAGIC_PAPER));
    }

    @Override
    public RecipeType<WashPaperRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.colorist.wash_paper");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WashPaperRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStack(new ItemStack(ModItems.MAGIC_PAPER));
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 1).addItemStack(new ItemStack(ModItems.MAGIC_CRYSTAL));
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 1).addItemStack(new ItemStack(Items.WHITE_DYE));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 1).addItemStack(new ItemStack(ModItems.MAGIC_PAPER));
    }
}
