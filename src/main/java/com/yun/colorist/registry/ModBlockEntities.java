package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.block.entity.MagicTableBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<MagicTableBlockEntity> MAGIC_TABLE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_table"),
            FabricBlockEntityTypeBuilder.create(MagicTableBlockEntity::new, ModBlocks.MAGIC_TABLE).build()
    );

    public static void initialize() {
    }
}
