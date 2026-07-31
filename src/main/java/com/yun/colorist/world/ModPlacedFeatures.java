package com.yun.colorist.world;

import com.yun.colorist.Colorist;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> MAGIC_CRYSTAL_ORE_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_crystal_ore")
    );
}
