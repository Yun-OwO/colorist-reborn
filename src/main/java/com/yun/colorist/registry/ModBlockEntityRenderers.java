package com.yun.colorist.registry;

import com.yun.colorist.block.entity.MagicTableBlockEntity;
import com.yun.colorist.client.renderer.MagicTableRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class ModBlockEntityRenderers {

    public static void register() {
        BlockEntityRendererRegistry.register(ModBlockEntities.MAGIC_TABLE, MagicTableRenderer::new);
    }
}
