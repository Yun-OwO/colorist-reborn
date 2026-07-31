package com.yun.colorist.event;

import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModBlocks;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootHandler {

    private static final String[][] PAPER_LOOT = {
            {"witch", "#00CCCC"},
            {"creeper", "#66FF00"},
            {"skeleton", "#FFFFFF"},
            {"warden", "#008888"},
            {"enderman", "#00CCCC"}
    };

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;
            Identifier id = key.identifier();

            if (matchesEntity(id, EntityType.WITCH)) {
                tableBuilder.withPool(buildMagicPaperPool(1, 2, "#00CCCC"));
                tableBuilder.withPool(buildCrystalPool(2, 3));
            } else if (matchesEntity(id, EntityType.CREEPER)) {
                tableBuilder.withPool(buildMagicPaperPool(0, 1, "#66FF00"));
                tableBuilder.withPool(buildTntPool(0, 1));
            } else if (matchesEntity(id, EntityType.SKELETON)) {
                tableBuilder.withPool(buildMagicPaperPool(0, 1, "#FFFFFF"));
            } else if (matchesEntity(id, EntityType.WARDEN)) {
                tableBuilder.withPool(buildCrystalPool(4, 6));
                tableBuilder.withPool(buildDyePool(Items.BLACK_DYE, 0, 5));
                tableBuilder.withPool(buildMagicPaperPool(0, 3, "#008888"));
            } else if (matchesEntity(id, EntityType.ENDERMAN)) {
                tableBuilder.withPool(buildCrystalPool(1, 2));
                tableBuilder.withPool(buildDyePool(Items.BLACK_DYE, 0, 2));
                tableBuilder.withPool(buildObsidianPool(1, 1));
                tableBuilder.withPool(buildMagicPaperPool(0, 2, "#00CCCC"));
            } else if (id.equals(Identifier.withDefaultNamespace("blocks/amethyst_block"))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModBlocks.MAGIC_CRYSTAL_ORE)));
            }
        });
    }

    private static boolean matchesEntity(Identifier lootTableId, EntityType<?> type) {
        return type.getDefaultLootTable().map(k -> k.identifier().equals(lootTableId)).orElse(false);
    }

    private static LootPool.Builder buildMagicPaperPool(int min, int max, String color) {
        return LootPool.lootPool()
                .setRolls(UniformGenerator.between(min, max))
                .add(LootItem.lootTableItem(ModItems.MAGIC_PAPER)
                        .apply(SetComponentsFunction.setComponent(ModComponents.MAGIC_PAPER, new MagicPaperData(1, color))));
    }

    private static LootPool.Builder buildCrystalPool(int min, int max) {
        return LootPool.lootPool()
                .setRolls(UniformGenerator.between(min, max))
                .add(LootItem.lootTableItem(ModItems.MAGIC_CRYSTAL));
    }

    private static LootPool.Builder buildDyePool(Item dye, int min, int max) {
        return LootPool.lootPool()
                .setRolls(UniformGenerator.between(min, max))
                .add(LootItem.lootTableItem(dye));
    }

    private static LootPool.Builder buildTntPool(int min, int max) {
        return LootPool.lootPool()
                .setRolls(UniformGenerator.between(min, max))
                .add(LootItem.lootTableItem(Items.TNT));
    }

    private static LootPool.Builder buildObsidianPool(int min, int max) {
        return LootPool.lootPool()
                .setRolls(UniformGenerator.between(min, max))
                .add(LootItem.lootTableItem(Items.OBSIDIAN));
    }
}
