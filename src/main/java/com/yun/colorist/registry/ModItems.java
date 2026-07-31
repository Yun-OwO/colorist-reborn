package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.item.MagicBookItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

    public static final Item RAINBOW_DYE = register("rainbow_dye", Item::new, new Item.Properties());
    public static final Item GRAYSCALE_DYE = register("grayscale_dye", Item::new, new Item.Properties());
    public static final Item BLEAK_DYE = register("bleak_dye", Item::new, new Item.Properties());
    public static final Item SOIL_DYE = register("soil_dye", Item::new, new Item.Properties());

    public static final Item MAGIC_PAPER = register("magic_paper", Item::new,
            new Item.Properties().stacksTo(1).component(ModComponents.MAGIC_PAPER, MagicPaperData.DEFAULT));

    public static final Item MAGIC_CRYSTAL = register("magic_crystal", Item::new, new Item.Properties());

    public static final Item MAGIC_BOOK = register("magic_book", MagicBookItem::new,
            new Item.Properties().stacksTo(1).durability(1000).component(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT));

    private static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties settings) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Colorist.MOD_ID, name));
        T item = factory.apply(settings.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.add(RAINBOW_DYE);
            entries.add(GRAYSCALE_DYE);
            entries.add(BLEAK_DYE);
            entries.add(SOIL_DYE);
            entries.add(MAGIC_PAPER);
            entries.add(MAGIC_CRYSTAL);
            entries.add(MAGIC_BOOK);
        });
    }
}
