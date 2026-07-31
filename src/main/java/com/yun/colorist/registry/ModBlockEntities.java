package com.yun.colorist.registry;

import com.yun.colorist.Colorist;
import com.yun.colorist.block.entity.MagicTableBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final BlockEntityType<MagicTableBlockEntity> MAGIC_TABLE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Colorist.MOD_ID, "magic_table"),
            FabricBlockEntityTypeBuilder.create(MagicTableBlockEntity::new, ModBlocks.MAGIC_TABLE).build()
    );

    public static void initialize() {
    }
}
