package com.yun.colorist.compat.jei;

import com.yun.colorist.Colorist;
import com.yun.colorist.recipe.WashPaperRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class ColoristJeiPlugin implements IModPlugin {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRCategoryRegistration registration) {
        registration.addRecipeCategories(new WashPaperRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(WashPaperRecipeCategory.TYPE, java.util.List.of(new WashPaperRecipe()));
    }
}
