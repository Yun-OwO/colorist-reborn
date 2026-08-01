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
        long startTime = System.currentTimeMillis();
        LOGGER.info("=== Colorist v{} initializing (main) ===", System.getProperty("mod_version", "1.0-beta"));
        LOGGER.info("Java: {}, Minecraft: 1.21.11, Fabric: {}", System.getProperty("java.version"), System.getProperty("minecraft_version", "?"));

        LOGGER.info("Registering data components...");
        ModComponents.initialize();
        LOGGER.info("Registering items...");
        ModItems.initialize();
        LOGGER.info("Registering blocks...");
        ModBlocks.initialize();
        LOGGER.info("Registering block entities...");
        ModBlockEntities.initialize();
        LOGGER.info("Registering recipes...");
        ModRecipes.initialize();
        LOGGER.info("Registering network payloads...");
        ModPayloads.initialize();

        LOGGER.info("Registering event handlers...");
        InventoryHandler.register();
        LootHandler.register();
        MagicBookHandler.register();
        LOGGER.info("Registering world features...");
        ModBiomeModifications.register();

        long elapsed = System.currentTimeMillis() - startTime;
        LOGGER.info("=== Colorist main initialization complete ({} ms) ===", elapsed);
    }
}
