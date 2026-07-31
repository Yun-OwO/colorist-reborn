package com.yun.colorist.client;

import com.yun.colorist.client.tooltip.ColoristTooltipCallback;
import com.yun.colorist.network.client.ClientPayloadReceiver;
import com.yun.colorist.registry.ModBlockEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class ColoristClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPayloadReceiver.register();
        ModBlockEntityRenderers.register();
        ColoristTooltipCallback.register();
    }
}
