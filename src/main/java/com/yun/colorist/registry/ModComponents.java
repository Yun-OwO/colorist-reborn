package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.component.MagicPaperData;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModComponents {

    public static final DataComponentType<MagicPaperData> MAGIC_PAPER = register("magic_paper",
            DataComponentType.<MagicPaperData>builder().persistent(MagicPaperData.CODEC).networkSynchronized(MagicPaperData.PACKET_CODEC));

    public static final DataComponentType<MagicBookData> MAGIC_BOOK = register("magic_book",
            DataComponentType.<MagicBookData>builder().persistent(MagicBookData.CODEC).networkSynchronized(MagicBookData.PACKET_CODEC));

    public static final DataComponentType<MagicAttrData> ATTR = register("attr",
            DataComponentType.<MagicAttrData>builder().persistent(MagicAttrData.CODEC).networkSynchronized(MagicAttrData.PACKET_CODEC));

    private static <T> DataComponentType<T> register(String name, DataComponentType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(Colorist.MOD_ID, name), builder.build());
    }

    public static void initialize() {
    }
}
