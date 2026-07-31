package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.block.MagicCrystalOreBlock;
import com.yun.colorist.block.MagicTableBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {

    public static final Block MAGIC_TABLE = register("magic_table", MagicTableBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE).sounds(BlockSoundGroup.STONE).luminance(state -> 3).strength(1.0f));

    public static final Block MAGIC_CRYSTAL_ORE = register("magic_crystal_ore", MagicCrystalOreBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(2.0f).requiresTool());

    private static <T extends Block> T register(String name, Function<AbstractBlock.Settings, T> blockFactory, AbstractBlock.Settings settings) {
        RegistryKey<Block> blockKey = RegistryKey.of(Registries.BLOCK.getKey(), Identifier.fromNamespaceAndPath(Colorist.MOD_ID, name));
        T block = blockFactory.apply(settings.registryKey(blockKey));
        Registry.register(Registries.BLOCK, blockKey, block);

        RegistryKey<Item> itemKey = RegistryKey.of(Registries.ITEM.getKey(), Identifier.fromNamespaceAndPath(Colorist.MOD_ID, name));
        BlockItem blockItem = new BlockItem(block, new Item.Properties().registryKey(itemKey).useBlockDescriptionPrefix());
        Registry.register(Registries.ITEM, itemKey, blockItem);
        return block;
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(MAGIC_TABLE);
            entries.add(MAGIC_CRYSTAL_ORE);
        });
    }
}
