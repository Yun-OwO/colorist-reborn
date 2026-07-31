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
        ClientTickEvents.END_CLIENT_TICK.register(MagicClientEffect::tick);
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
        if (client.player == null || client.world == null) return;
        Vec3d pos = client.player.getEyePos();
        client.world.playSound(pos.x, pos.y, pos.z,
                crit ? SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT : SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK,
                SoundCategory.MASTER, 0.8f, 1.4f + (float) Math.random(), true);
    }

    private static void tick(MinecraftClient client) {
        if (!casting || client.player == null || client.world == null) return;
        ClientPlayerEntity player = client.player;
        Vec3d eye = player.getEyePos(1f);
        Vec3d look = player.getRotationVector().multiply(10);
        if (!soundStarted) {
            client.world.playSound(eye.x, eye.y, eye.z, SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.MASTER, 1f, 1f + (float) Math.random(), true);
            client.world.playSound(eye.x, eye.y, eye.z, SoundEvents.ENTITY_WARDEN_DEATH, SoundCategory.MASTER, 0.6f, 1.5f + (float) Math.random(), true);
            soundStarted = true;
        }
        progress += (float) (Math.random() / 50 + 0.05);
        if (progress > 1f) return;
        Vec3d p = eye.add(look.multiply(progress));
        client.world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, p.x, p.y, p.z, 0, 0, 0);
        client.world.addParticle(ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 0, 0, 0);
        client.world.addParticle(ParticleTypes.ENCHANT, p.x, p.y, p.z, 0, 0, 0);
        if (((int) (progress * 10) % 2) == 0) {
            client.world.addParticle(ParticleTypes.SONIC_BOOM, p.x, p.y, p.z, 0, 0, 0);
            client.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, p.x, p.y, p.z, 0, 0, 0);
        } else {
            client.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, p.x, p.y, p.z, 0, 0, 0);
        }
    }
}
