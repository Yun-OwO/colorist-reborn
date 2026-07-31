package com.yun.colorist.network.client;

import com.yun.colorist.network.CritPayload;
import com.yun.colorist.network.MagicStartPayload;
import com.yun.colorist.network.MagicStopPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientPayloadReceiver {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MagicStartPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> MagicClientEffect.start(payload.r(), payload.g(), payload.b()));
        });

        ClientPlayNetworking.registerGlobalReceiver(MagicStopPayload.TYPE, (payload, context) -> {
            context.client().execute(MagicClientEffect::stop);
        });

        ClientPlayNetworking.registerGlobalReceiver(CritPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> MagicClientEffect.crit(payload.crit()));
        });
    }
}
