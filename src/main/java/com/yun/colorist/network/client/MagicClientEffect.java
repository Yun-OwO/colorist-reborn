package com.yun.colorist.network.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class MagicClientEffect {

    private static boolean casting = false;
    private static float r = 1f, g = 1f, b = 1f;
    private static float progress = 0f;
    private static boolean soundStarted = false;

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick(client));
    }

    public static void start(float red, float green, float blue) {
        casting = true;
        r = red;
        g = green;
        b = blue;
        progress = 0f;
        soundStarted = false;
    }

    public static void stop() {
        casting = false;
    }

    public static void crit(boolean crit) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.getSoundManager().play(crit ? SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT : SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.MASTER, 0.8f, 1.4f + (float) Math.random(), client.player.getBlockPos());
        }
    }

    private static void tick(MinecraftClient client) {
        if (!casting || client.player == null) return;
        ClientPlayerEntity player = client.player;
        Vec3d eye = player.getEyePos(1f);
        Vec3d look = player.getRotationVector().multiply(10);
        if (!soundStarted) {
            client.getSoundManager().play(SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.MASTER, 1f, 1f + (float) Math.random(), player.getBlockPos());
            client.getSoundManager().play(SoundEvents.ENTITY_WARDEN_DEATH, SoundCategory.MASTER, 0.6f, 1.5f + (float) Math.random(), player.getBlockPos());
            soundStarted = true;
        }
        progress += Math.random() / 50 + 0.05f;
        if (progress > 1f) return;
        Vec3d pos = eye.add(look.multiply(progress));
        client.world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, pos.x, pos.y, pos.z, 0, 0, 0);
        client.world.addParticle(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y, pos.z, 0, 0, 0);
        client.world.addParticle(ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, 0, 0, 0);
        if (((int) (progress * 10) % 2) == 0) {
            client.world.addParticle(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 0, 0, 0);
            client.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, pos.x, pos.y, pos.z, 0, 0, 0);
        } else {
            client.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }
}
