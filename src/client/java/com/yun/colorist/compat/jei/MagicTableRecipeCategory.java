package com.yun.colorist.compat.jei;

import com.yun.colorist.Colorist;
import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModBlocks;
import com.yun.colorist.registry.ModComponents;
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

import java.util.ArrayList;
import java.util.List;

public class MagicTableRecipeCategory implements IRecipeCategory<MagicTableRecipeCategory.MagicTableRecipe> {

    public static final IRecipeType<MagicTableRecipe> TYPE = IRecipeType.create(
            Colorist.MOD_ID, "magic_table", MagicTableRecipe.class
    );

    private static final int WIDTH = 140;
    private static final int HEIGHT = 54;

    private final IDrawable icon;

    public MagicTableRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.MAGIC_TABLE.asItem()));
    }

    @Override
    public IRecipeType<MagicTableRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.colorist.magic_table");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MagicTableRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 19)
                .addItemStack(recipe.input1());
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 19)
                .addItemStack(recipe.input2());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 119, 19)
                .addItemStack(recipe.output());
    }

    public static List<MagicTableRecipe> generateRecipes() {
        List<MagicTableRecipe> recipes = new ArrayList<>();

        // 1. Dye paper: Magic Paper + Red Dye → Colored Paper
        ItemStack paper = new ItemStack(ModItems.MAGIC_PAPER);
        paper.set(ModComponents.MAGIC_PAPER, new MagicPaperData(1, "#FFFFFF"));
        ItemStack dyedPaper = new ItemStack(ModItems.MAGIC_PAPER);
        dyedPaper.set(ModComponents.MAGIC_PAPER, new MagicPaperData(2, "#B02E26"));
        recipes.add(new MagicTableRecipe(paper, new ItemStack(Items.RED_DYE), dyedPaper));

        // 2. Crystal on paper: Magic Paper + Magic Crystal → Enhanced Paper
        ItemStack enhancedPaper = new ItemStack(ModItems.MAGIC_PAPER);
        enhancedPaper.set(ModComponents.MAGIC_PAPER, new MagicPaperData(6, "#FFFFFF"));
        recipes.add(new MagicTableRecipe(paper, new ItemStack(ModItems.MAGIC_CRYSTAL), enhancedPaper));

        // 3. Insert paper into book: Magic Book + Magic Paper → Book with attrs
        ItemStack emptyBook = new ItemStack(ModItems.MAGIC_BOOK);
        emptyBook.set(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
        ItemStack paperAttr = new ItemStack(ModItems.MAGIC_PAPER);
        paperAttr.set(ModComponents.MAGIC_PAPER, new MagicPaperData(1, "#B02E26"));
        MagicAttrData attr = new MagicAttrData(6, 0, 0, 10, 0, 1, "#B02E26");
        ItemStack bookWithAttr = new ItemStack(ModItems.MAGIC_BOOK);
        bookWithAttr.set(ModComponents.MAGIC_BOOK, new MagicBookData(List.of(attr), attr, false));
        recipes.add(new MagicTableRecipe(emptyBook, paperAttr, bookWithAttr));

        // 4. Crystal on book: Magic Book + Magic Crystal → Enhanced Book
        ItemStack bookWithAttr2 = new ItemStack(ModItems.MAGIC_BOOK);
        bookWithAttr2.set(ModComponents.MAGIC_BOOK, new MagicBookData(List.of(attr), attr, false));
        MagicAttrData enhancedAttr = new MagicAttrData(6, 0, 0, 10, 0, 6, "#B02E26");
        ItemStack enhancedBook = new ItemStack(ModItems.MAGIC_BOOK);
        enhancedBook.set(ModComponents.MAGIC_BOOK, new MagicBookData(List.of(enhancedAttr), enhancedAttr, false));
        recipes.add(new MagicTableRecipe(bookWithAttr2, new ItemStack(ModItems.MAGIC_CRYSTAL), enhancedBook));

        return recipes;
    }

    public record MagicTableRecipe(ItemStack input1, ItemStack input2, ItemStack output) {
    }
}