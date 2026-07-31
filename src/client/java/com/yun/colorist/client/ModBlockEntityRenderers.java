package com.yun.colorist.client;

import com.yun.colorist.block.entity.ModBlockEntities;
import com.yun.colorist.client.renderer.MagicTableRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class ModBlockEntityRenderers {

    public static void register() {
        BlockEntityRendererRegistry.register(ModBlockEntities.MAGIC_TABLE, MagicTableRenderer::new);
    }
}
