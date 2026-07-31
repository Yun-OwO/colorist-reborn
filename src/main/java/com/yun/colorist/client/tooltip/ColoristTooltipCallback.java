package com.yun.colorist.client.tooltip;

import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.util.AttrUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ColoristTooltipCallback {

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.isOf(ModItems.MAGIC_PAPER)) {
                addPaperTooltip(stack, lines);
            } else if (stack.isOf(ModItems.MAGIC_BOOK)) {
                addBookTooltip(stack, lines);
            }
        });
    }

    private static void addPaperTooltip(ItemStack stack, List<Text> lines) {
        MagicPaperData data = stack.getOrDefault(ModComponents.MAGIC_PAPER, MagicPaperData.DEFAULT);
        MagicAttrData attr = AttrUtil.calculateFromPaper(data.level(), data.attr());
        int colorInt = parseColor(data.attr());
        lines.add(Text.translatable("tooltip.colorist.level").append(Text.literal(String.valueOf(data.level())).styled(s -> s.withColor(colorInt))));
        addAttrTooltip(lines, attr, null);
    }

    private static void addBookTooltip(ItemStack stack, List<Text> lines) {
        MagicBookData data = stack.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
        MagicAttrData attr = data.attr();
        int colorInt = parseColor(attr.color());
        lines.add(Text.translatable("tooltip.colorist.level").append(Text.literal(String.valueOf(attr.level())).styled(s -> s.withColor(colorInt))));
        addAttrTooltip(lines, attr, Text.translatable("tooltip.colorist.count", data.attrs().size(), AttrUtil.MAX_ATTRS));
    }

    private static void addAttrTooltip(List<Text> lines, MagicAttrData attr, Text ext) {
        if (attr == null) return;
        lines.add(Text.translatable("tooltip.colorist.rainbow").append(buildProgressBar(attr.r(), attr.g(), attr.b())));
        lines.add(Text.translatable("tooltip.colorist.yin_yang").append(buildProgressBar(attr.brightness(), attr.darkness(), 0)));
        if (Screen.hasShiftDown()) {
            lines.add(Text.empty());
            lines.add(Text.translatable("tooltip.colorist.red", attr.r()).formatted(Formatting.RED));
            lines.add(Text.translatable("tooltip.colorist.green", attr.g()).formatted(Formatting.GREEN));
            lines.add(Text.translatable("tooltip.colorist.blue", attr.b()).formatted(Formatting.BLUE));
            lines.add(Text.translatable("tooltip.colorist.dark", attr.darkness()).formatted(Formatting.DARK_GRAY));
            lines.add(Text.translatable("tooltip.colorist.bright", attr.brightness()).formatted(Formatting.WHITE));
            AttrUtil.BasicAttr value = AttrUtil.calculateValue(attr, false);
            lines.add(Text.empty());
            lines.add(Text.translatable("tooltip.colorist.cost", value.cost()).formatted(Formatting.BLUE));
            lines.add(Text.translatable("tooltip.colorist.atk", value.atk()).formatted(Formatting.RED));
            lines.add(Text.translatable("tooltip.colorist.hp", value.hp()).formatted(Formatting.GREEN));
            lines.add(Text.translatable("tooltip.colorist.br", value.br()).formatted(Formatting.DARK_AQUA));
            lines.add(Text.translatable("tooltip.colorist.bd", value.bd()).formatted(Formatting.DARK_PURPLE));
            if (ext != null) {
                lines.add(Text.empty());
                lines.add(ext);
            }
        } else {
            lines.add(Text.translatable("tooltip.colorist.shift").formatted(Formatting.DARK_GRAY));
        }
    }

    private static Text buildProgressBar(int r, int g, int b) {
        int sum = r + g + b;
        if (sum == 0) return Text.empty();
        int rLen = Math.round((float) r / sum * AttrUtil.PROG_LENGTH);
        int gLen = Math.round((float) g / sum * AttrUtil.PROG_LENGTH);
        int bLen = AttrUtil.PROG_LENGTH - rLen - gLen;
        return Text.literal("▍".repeat(Math.max(rLen, 0))).formatted(Formatting.RED)
                .append(Text.literal("▍".repeat(Math.max(gLen, 0))).formatted(Formatting.GREEN))
                .append(Text.literal("▍".repeat(Math.max(bLen, 0))).formatted(Formatting.BLUE));
    }

    private static int parseColor(String hex) {
        try {
            return Integer.parseInt(hex.substring(1), 16);
        } catch (Exception e) {
            return 0xFFFFFF;
        }
    }
}
