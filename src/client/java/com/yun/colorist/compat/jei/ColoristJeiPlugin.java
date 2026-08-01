package com.yun.colorist.compat.jei;

import com.yun.colorist.Colorist;
import com.yun.colorist.registry.ModBlocks;
import com.yun.colorist.registry.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@JeiPlugin
public class ColoristJeiPlugin implements IModPlugin {

    private static final Identifier PLUGIN_ID = Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        Colorist.LOGGER.debug("Registering JEI recipe categories");
        registration.addRecipeCategories(
                new WashPaperRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new MagicTableRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Colorist.LOGGER.debug("Registering JEI recipes");

        // Register wash paper recipe demonstration
        registration.addRecipes(WashPaperRecipeCategory.TYPE, List.of(WashPaperRecipeCategory.createRecipe()));

        // Register magic table dyeing recipes (block interaction demonstrations)
        registration.addRecipes(MagicTableRecipeCategory.TYPE, MagicTableRecipeCategory.generateRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        Colorist.LOGGER.debug("Registering JEI recipe catalysts");
        registration.addRecipeCatalysts(WashPaperRecipeCategory.TYPE, ModItems.MAGIC_BOOK);
        registration.addRecipeCatalysts(MagicTableRecipeCategory.TYPE, ModBlocks.MAGIC_TABLE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        // No custom transfer handlers needed
    }
}