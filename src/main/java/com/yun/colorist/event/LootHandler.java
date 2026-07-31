package com.yun.colorist.event;

import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class LootHandler {

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;
            Identifier id = key.getValue();

            // Entity loot tables
            if (matchesEntity(id, net.minecraft.entity.EntityType.WITCH)) {
                addPool(tableBuilder, buildMagicPaperPool(1, 2, "#00CCCC"));
                addPool(tableBuilder, buildCrystalPool(2, 3));
            } else if (matchesEntity(id, net.minecraft.entity.EntityType.CREEPER)) {
                addPool(tableBuilder, buildMagicPaperPool(0, 1, "#66FF00"));
            } else if (matchesEntity(id, net.minecraft.entity.EntityType.SKELETON)) {
                addPool(tableBuilder, buildMagicPaperPool(0, 1, "#FFFFFF"));
            } else if (matchesEntity(id, net.minecraft.entity.EntityType.WARDEN)) {
                addPool(tableBuilder, buildCrystalPool(4, 6));
                addPool(tableBuilder, buildDyePool(Items.BLACK_DYE, 0, 5));
                addPool(tableBuilder, buildMagicPaperPool(0, 3, "#008888"));
            } else if (matchesEntity(id, net.minecraft.entity.EntityType.ENDERMAN)) {
                addPool(tableBuilder, buildCrystalPool(1, 2));
                addPool(tableBuilder, buildDyePool(Items.BLACK_DYE, 0, 2));
                addPool(tableBuilder, buildMagicPaperPool(0, 2, "#00CCCC"));
            } else if (id.equals(Identifier.ofVanilla("blocks/amethyst_block"))) {
                addPool(tableBuilder, LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.MAGIC_CRYSTAL_ORE))
                        .build());
            }
        });
    }

    private static boolean matchesEntity(Identifier lootTableId, net.minecraft.entity.EntityType<?> type) {
        Optional<RegistryKey<net.minecraft.loot.LootTable>> optKey = type.getLootTableId();
        return optKey.map(k -> k.getValue().equals(lootTableId)).orElse(false);
    }

    private static void addPool(net.minecraft.loot.LootTable.Builder builder, LootPool pool) {
        builder.pool(pool);
    }

    private static LootPool buildMagicPaperPool(int min, int max, String color) {
        ItemStack stack = new ItemStack(ModItems.MAGIC_PAPER);
        stack.set(ModComponents.MAGIC_PAPER, new MagicPaperData(1, color));
        return LootPool.builder()
                .rolls(UniformLootNumberProvider.create(min, max))
                .with(ItemEntry.builder(ModItems.MAGIC_PAPER)
                        .apply(SetComponentsLootFunction.builder(stack.getComponents())))
                .build();
    }

    private static LootPool buildCrystalPool(int min, int max) {
        return LootPool.builder()
                .rolls(UniformLootNumberProvider.create(min, max))
                .with(ItemEntry.builder(ModItems.MAGIC_CRYSTAL))
                .build();
    }

    private static LootPool buildDyePool(net.minecraft.item.Item dye, int min, int max) {
        return LootPool.builder()
                .rolls(UniformLootNumberProvider.create(min, max))
                .with(ItemEntry.builder(dye))
                .build();
    }
}
