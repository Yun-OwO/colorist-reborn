package com.yun.colorist.client;

import com.yun.colorist.Colorist;
import com.yun.colorist.registry.ModBlockEntities;
import com.yun.colorist.client.renderer.MagicTableRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class ModBlockEntityRenderers {

    public static void register() {
        Colorist.LOGGER.debug("Registering MagicTableRenderer for block entity type MAGIC_TABLE");
        BlockEntityRendererRegistry.register(ModBlockEntities.MAGIC_TABLE, context -> {
            Colorist.LOGGER.debug("Creating MagicTableRenderer instance");
            return new MagicTableRenderer(context);
        });
    }
}
