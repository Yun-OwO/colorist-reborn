package com.yun.colorist.client;

import com.yun.colorist.Colorist;
import com.yun.colorist.client.tooltip.ColoristTooltipCallback;
import com.yun.colorist.network.client.ClientPayloadReceiver;
import net.fabricmc.api.ClientModInitializer;

public class ColoristClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        long startTime = System.currentTimeMillis();
        Colorist.LOGGER.info("=== Colorist client initializing ===");

        Colorist.LOGGER.info("Registering client payload receivers...");
        ClientPayloadReceiver.register();
        Colorist.LOGGER.info("Registering block entity renderers...");
        ModBlockEntityRenderers.register();
        Colorist.LOGGER.info("Registering tooltip callbacks...");
        ColoristTooltipCallback.register();

        long elapsed = System.currentTimeMillis() - startTime;
        Colorist.LOGGER.info("=== Colorist client initialization complete ({} ms) ===", elapsed);
    }
}
