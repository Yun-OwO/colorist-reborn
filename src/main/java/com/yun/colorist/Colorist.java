package com.yun.colorist;

import com.yun.colorist.event.InventoryHandler;
import com.yun.colorist.event.LootHandler;
import com.yun.colorist.event.MagicBookHandler;
import com.yun.colorist.registry.*;
import com.yun.colorist.world.ModBiomeModifications;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Colorist implements ModInitializer {
    public static final String MOD_ID = "colorist";
    public static final Logger LOGGER = LoggerFactory.getLogger("colorist");

    @Override
    public void onInitialize() {
        LOGGER.info("Colorist initializing...");

        ModComponents.initialize();
        ModItems.initialize();
        ModBlocks.initialize();
        ModBlockEntities.initialize();
        ModRecipes.initialize();
        ModPayloads.initialize();

        InventoryHandler.register();
        LootHandler.register();
        MagicBookHandler.register();
        ModBiomeModifications.register();
    }
}
