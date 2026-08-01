package com.yun.colorist.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yun.colorist.Colorist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ColoristConfig {

    public static final ColoristConfig INSTANCE = new ColoristConfig();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "colorist.json");

    public int maxAttrs = 12;
    public int castRange = 10;
    public boolean enableCritMessages = true;

    private ColoristConfig() {
        load();
    }

    public void load() {
        if (!Files.exists(CONFIG_PATH)) {
            Colorist.LOGGER.info("Config file not found at {}, creating default", CONFIG_PATH.toAbsolutePath());
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            ColoristConfig loaded = GSON.fromJson(reader, ColoristConfig.class);
            if (loaded != null) {
                this.maxAttrs = loaded.maxAttrs;
                this.castRange = loaded.castRange;
                this.enableCritMessages = loaded.enableCritMessages;
                Colorist.LOGGER.info("Config loaded: maxAttrs={}, castRange={}, enableCritMessages={}", maxAttrs, castRange, enableCritMessages);
            } else {
                Colorist.LOGGER.warn("Config file is empty or malformed, using defaults");
            }
        } catch (IOException e) {
            Colorist.LOGGER.error("Failed to load config", e);
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
            Colorist.LOGGER.debug("Config saved: maxAttrs={}, castRange={}, enableCritMessages={}", maxAttrs, castRange, enableCritMessages);
        } catch (IOException e) {
            Colorist.LOGGER.error("Failed to save config", e);
        }
    }

    public Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("config.colorist.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("config.colorist.general"))
                        .tooltip(Component.translatable("config.colorist.general.tooltip"))
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("config.colorist.max_attrs"))
                                .description(OptionDescription.of(Component.translatable("config.colorist.max_attrs.desc")))
                                .binding(12, () -> maxAttrs, value -> maxAttrs = value)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 36).step(1))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("config.colorist.cast_range"))
                                .description(OptionDescription.of(Component.translatable("config.colorist.cast_range.desc")))
                                .binding(10, () -> castRange, value -> castRange = value)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(3, 32).step(1))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("config.colorist.enable_crit_messages"))
                                .description(OptionDescription.of(Component.translatable("config.colorist.enable_crit_messages.desc")))
                                .binding(true, () -> enableCritMessages, value -> enableCritMessages = value)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .save(this::save)
                .build()
                .generateScreen(parent);
    }
}
