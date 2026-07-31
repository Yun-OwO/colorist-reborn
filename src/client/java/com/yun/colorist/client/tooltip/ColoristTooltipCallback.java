package com.yun.colorist.client.tooltip;

import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.util.AttrUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ColoristTooltipCallback {

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.is(ModItems.MAGIC_PAPER)) {
                addPaperTooltip(stack, lines);
            } else if (stack.is(ModItems.MAGIC_BOOK)) {
                addBookTooltip(stack, lines);
            }
        });
    }

    private static void addPaperTooltip(ItemStack stack, List<Component> lines) {
        MagicPaperData data = stack.getOrDefault(ModComponents.MAGIC_PAPER, MagicPaperData.DEFAULT);
        MagicAttrData attr = AttrUtil.calculateFromPaper(data.level(), data.attr());
        int colorInt = parseColor(data.attr());
        lines.add(Component.translatable("tooltip.colorist.level").append(Component.literal(String.valueOf(data.level())).withStyle(s -> s.withColor(colorInt))));
        addAttrTooltip(lines, attr, null);
    }

    private static void addBookTooltip(ItemStack stack, List<Component> lines) {
        MagicBookData data = stack.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
        MagicAttrData attr = data.attr();
        int colorInt = parseColor(attr.color());
        lines.add(Component.translatable("tooltip.colorist.level").append(Component.literal(String.valueOf(attr.level())).withStyle(s -> s.withColor(colorInt))));
        addAttrTooltip(lines, attr, Component.translatable("tooltip.colorist.count", data.attrs().size(), AttrUtil.MAX_ATTRS));
    }

    private static void addAttrTooltip(List<Component> lines, MagicAttrData attr, Component ext) {
        if (attr == null) return;
        lines.add(Component.translatable("tooltip.colorist.rainbow").append(buildProgressBar(attr.r(), attr.g(), attr.b())));
        lines.add(Component.translatable("tooltip.colorist.yin_yang").append(buildProgressBar(attr.brightness(), attr.darkness(), 0)));
        if (Screen.hasShiftDown()) {
            lines.add(Component.empty());
            lines.add(Component.translatable("tooltip.colorist.red", attr.r()).withStyle(ChatFormatting.RED));
            lines.add(Component.translatable("tooltip.colorist.green", attr.g()).withStyle(ChatFormatting.GREEN));
            lines.add(Component.translatable("tooltip.colorist.blue", attr.b()).withStyle(ChatFormatting.BLUE));
            lines.add(Component.translatable("tooltip.colorist.dark", attr.darkness()).withStyle(ChatFormatting.DARK_GRAY));
            lines.add(Component.translatable("tooltip.colorist.bright", attr.brightness()).withStyle(ChatFormatting.WHITE));
            AttrUtil.BasicAttr value = AttrUtil.calculateValue(attr, false);
            lines.add(Component.empty());
            lines.add(Component.translatable("tooltip.colorist.cost", value.cost()).withStyle(ChatFormatting.BLUE));
            lines.add(Component.translatable("tooltip.colorist.atk", value.atk()).withStyle(ChatFormatting.RED));
            lines.add(Component.translatable("tooltip.colorist.hp", value.hp()).withStyle(ChatFormatting.GREEN));
            lines.add(Component.translatable("tooltip.colorist.br", value.br()).withStyle(ChatFormatting.DARK_AQUA));
            lines.add(Component.translatable("tooltip.colorist.bd", value.bd()).withStyle(ChatFormatting.DARK_PURPLE));
            if (ext != null) {
                lines.add(Component.empty());
                lines.add(ext);
            }
        } else {
            lines.add(Component.translatable("tooltip.colorist.shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private static Component buildProgressBar(int r, int g, int b) {
        int sum = r + g + b;
        if (sum == 0) return Component.empty();
        int rLen = Math.round((float) r / sum * AttrUtil.PROG_LENGTH);
        int gLen = Math.round((float) g / sum * AttrUtil.PROG_LENGTH);
        int bLen = AttrUtil.PROG_LENGTH - rLen - gLen;
        return Component.literal("▍".repeat(Math.max(rLen, 0))).withStyle(ChatFormatting.RED)
                .append(Component.literal("▍".repeat(Math.max(gLen, 0))).withStyle(ChatFormatting.GREEN))
                .append(Component.literal("▍".repeat(Math.max(bLen, 0))).withStyle(ChatFormatting.BLUE));
    }

    private static int parseColor(String hex) {
        try {
            return Integer.parseInt(hex.substring(1), 16);
        } catch (Exception e) {
            return 0xFFFFFF;
        }
    }
}
