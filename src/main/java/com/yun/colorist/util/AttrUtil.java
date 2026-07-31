package com.yun.colorist.util;

import com.yun.colorist.component.MagicAttrData;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class AttrUtil {

    public static final int MAX_ATTRS = 12;
    public static final int PROG_LENGTH = 18;

    public static double keep(double n, int p) {
        double factor = Math.pow(10, p);
        return Math.round(n * factor) / factor;
    }

    public static double keep(double n) {
        return keep(n, 1);
    }

    public static String format(String value, boolean ext) {
        return (value.startsWith("+") ? "" : (ext ? "" : "+")) + value;
    }

    public static MagicAttrData calculateAttr(MagicAttrData attr) {
        int r = attr.r();
        int g = attr.g();
        int b = attr.b();
        int level = attr.level();
        float brightness = (r + g + b) / 3f / 255f * 10f;
        int bright = Math.round(brightness);
        int dark = 10 - bright;
        return new MagicAttrData(r, g, b, bright, dark, level, attr.color());
    }

    public static MagicAttrData calculateFromPaper(int level, String color) {
        int[] rgb = ColorUtil.hexToRgb(color);
        int r = Math.round(rgb[0] / 255f * 10);
        int g = Math.round(rgb[1] / 255f * 10);
        int b = Math.round(rgb[2] / 255f * 10);
        return calculateAttr(new MagicAttrData(r, g, b, 0, 0, level, color));
    }

    public static BasicAttr calculateValue(MagicAttrData attr, boolean zero) {
        double cost = zero ? 0 : 0.9 - attr.b() / 150.0;
        double atk = zero ? 0 : Math.pow(attr.r(), 1.1) / 10 + Math.pow(attr.level(), 0.8) / 5;
        double hp = zero ? 0 : Math.pow(attr.g(), 1.1) / 5 + Math.pow(attr.level(), 0.8) / 5;
        double br = zero ? 0 : (Math.sqrt(attr.brightness()) * 2.5) / 100;
        double bd = zero ? 0 : attr.darkness() / 100.0;
        return new BasicAttr(keep(cost, 2), keep(atk, 1), keep(hp, 1), keep(br, 2), keep(bd, 2));
    }

    public static MagicAttrData combine(java.util.List<MagicAttrData> attrs) {
        int l = attrs.size();
        if (l == 0) return new MagicAttrData(0, 0, 0, 0, 0, 0, "#FFFFFF");
        int r = 0, g = 0, b = 0, level = 0, brightness = 0, darkness = 0;
        for (MagicAttrData attr : attrs) {
            r += attr.r();
            g += attr.g();
            b += attr.b();
            level += attr.level();
            brightness += attr.brightness();
            darkness += attr.darkness();
        }
        String color = ColorUtil.rgbToHex(
                Math.round((r / (float) l / 10f) * 255f),
                Math.round((g / (float) l / 10f) * 255f),
                Math.round((b / (float) l / 10f) * 255f)
        );
        return new MagicAttrData(r / l, g / l, b / l, brightness / l, darkness / l, level / l, color);
    }

    public static Text progressText(MagicAttrData attr) {
        Text result = Text.empty();
        int r = attr.r();
        int g = attr.g();
        int b = attr.b();
        int sum = r + g + b;
        if (sum == 0) return result;
        result.copy().append(progressBlock(r, sum, "red"));
        result.copy().append(progressBlock(g, sum, "green"));
        result.copy().append(progressBlock(b, sum, "blue"));
        return result;
    }

    private static Text progressBlock(int part, int sum, String color) {
        int n = Math.round((part / (float) sum) * PROG_LENGTH);
        Text t = Text.literal("▍".repeat(Math.max(n, 0)));
        return t;
    }

    public static Text gradientText(String text, String startColor, String endColor) {
        Text result = Text.empty();
        for (int i = 0; i < text.length(); i++) {
            float ratio = i / (float) Math.max(text.length() - 1, 1);
            String color = ColorUtil.merge(startColor, endColor, ratio);
            result.copy().append(Text.literal(String.valueOf(text.charAt(i))).setStyle(net.minecraft.text.Style.EMPTY.withColor(net.minecraft.util.Formatting.byName(color))));
        }
        return result;
    }

    public record BasicAttr(double cost, double atk, double hp, double br, double bd) {
    }
}
