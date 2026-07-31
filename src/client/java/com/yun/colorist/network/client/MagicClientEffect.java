package com.yun.colorist.network.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

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
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return;
        Vec3 pos = minecraft.player.getEyePosition();
        minecraft.level.playSound(null, pos.x, pos.y, pos.z,
                crit ? SoundEvents.LIGHTNING_BOLT_IMPACT : SoundEvents.AMETHYST_BLOCK_BREAK,
                SoundSource.MASTER, 0.8f, 1.4f + (float) Math.random());
    }

    private static void tick(Minecraft minecraft) {
        if (!casting || minecraft.player == null || minecraft.level == null) return;
        LocalPlayer player = minecraft.player;
        Vec3 eye = player.getEyePosition(1f);
        Vec3 look = player.getLookAngle().scale(10);
        if (!soundStarted) {
            minecraft.level.playSound(null, eye.x, eye.y, eye.z, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.MASTER, 1f, 1f + (float) Math.random());
            minecraft.level.playSound(null, eye.x, eye.y, eye.z, SoundEvents.WARDEN_DEATH, SoundSource.MASTER, 0.6f, 1.5f + (float) Math.random());
            soundStarted = true;
        }
        progress += (float) (Math.random() / 50 + 0.05);
        if (progress > 1f) return;
        Vec3 p = eye.add(look.scale(progress));
        minecraft.level.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, p.x, p.y, p.z, 0, 0, 0);
        minecraft.level.addParticle(ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 0, 0, 0);
        minecraft.level.addParticle(ParticleTypes.ENCHANT, p.x, p.y, p.z, 0, 0, 0);
        if (((int) (progress * 10) % 2) == 0) {
            minecraft.level.addParticle(ParticleTypes.SONIC_BOOM, p.x, p.y, p.z, 0, 0, 0);
            minecraft.level.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, p.x, p.y, p.z, 0, 0, 0);
        } else {
            minecraft.level.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, p.x, p.y, p.z, 0, 0, 0);
        }
    }
}
