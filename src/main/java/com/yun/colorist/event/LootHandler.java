package com.yun.colorist.event;

import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class LootHandler {

    public static void register() {
        LootTableEvents.MODIFY.register((id, tableBuilder, source) -> {
            if (!source.isBuiltin()) return;
            Identifier path = id.getValue();
            if (path.equals(net.minecraft.entity.EntityType.WITCH.getLootTableId())) {
                addPool(tableBuilder, buildMagicPaperPool(1, 2, "#00CCCC"));
                addPool(tableBuilder, buildCrystalPool(2, 3));
            } else if (path.equals(net.minecraft.entity.EntityType.CREEPER.getLootTableId())) {
                addPool(tableBuilder, buildMagicPaperPool(0, 1, "#66FF00"));
            } else if (path.equals(net.minecraft.entity.EntityType.SKELETON.getLootTableId())) {
                addPool(tableBuilder, buildMagicPaperPool(0, 1, "#FFFFFF"));
            } else if (path.equals(net.minecraft.entity.EntityType.WARDEN.getLootTableId())) {
                addPool(tableBuilder, buildCrystalPool(4, 6));
                addPool(tableBuilder, buildDyePool("black", 0, 5));
                addPool(tableBuilder, buildMagicPaperPool(0, 3, "#008888"));
            } else if (path.equals(net.minecraft.entity.EntityType.ENDERMAN.getLootTableId())) {
                addPool(tableBuilder, buildCrystalPool(1, 2));
                addPool(tableBuilder, buildDyePool("black", 0, 2));
                addPool(tableBuilder, buildMagicPaperPool(0, 2, "#00CCCC"));
            } else if (path.equals(Identifier.ofVanilla("blocks/amethyst_block"))) {
                addPool(tableBuilder, LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.MAGIC_CRYSTAL_ORE))
                        .build());
            }
        });
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
                        .apply(new SetComponentsLootFunction.Builder(stack.getComponents())))
                .build();
    }

    private static LootPool buildCrystalPool(int min, int max) {
        return LootPool.builder()
                .rolls(UniformLootNumberProvider.create(min, max))
                .with(ItemEntry.builder(ModItems.MAGIC_CRYSTAL))
                .build();
    }

    private static LootPool buildDyePool(String dye, int min, int max) {
        return LootPool.builder()
                .rolls(UniformLootNumberProvider.create(min, max))
                .with(ItemEntry.builder(net.minecraft.item.Items.valueOf(dye.toUpperCase() + "_DYE")))
                .build();
    }
}
