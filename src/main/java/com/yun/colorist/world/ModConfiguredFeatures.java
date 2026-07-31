package com.yun.colorist.world;

import com.yun.colorist.Colorist;
import com.yun.colorist.registry.ModBlocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> MAGIC_CRYSTAL_ORE = RegistryKey.of(
            RegistryKeys.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_crystal_ore")
    );

    static {
        OreFeatureConfig config = new OreFeatureConfig(
                List.of(
                        OreFeatureConfig.createTarget(new net.minecraft.world.gen.blockpredicate.TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.MAGIC_CRYSTAL_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(new net.minecraft.world.gen.blockpredicate.TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.MAGIC_CRYSTAL_ORE.getDefaultState())
                ),
                12
        );
    }
}
