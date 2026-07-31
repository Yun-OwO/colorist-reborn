package com.yun.colorist.util;

import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class ColorUtil {

    public static final Map<String, String> DYE_COLORS = new HashMap<>();

    static {
        DYE_COLORS.put("white_dye", "#F9FFFE");
        DYE_COLORS.put("orange_dye", "#F9801D");
        DYE_COLORS.put("magenta_dye", "#C74EBD");
        DYE_COLORS.put("light_blue_dye", "#3AB3DA");
        DYE_COLORS.put("yellow_dye", "#FED83D");
        DYE_COLORS.put("lime_dye", "#80C71F");
        DYE_COLORS.put("pink_dye", "#F38BAA");
        DYE_COLORS.put("gray_dye", "#474F52");
        DYE_COLORS.put("light_gray_dye", "#9D9D97");
        DYE_COLORS.put("cyan_dye", "#169C9C");
        DYE_COLORS.put("purple_dye", "#8932B8");
        DYE_COLORS.put("blue_dye", "#3C44AA");
        DYE_COLORS.put("brown_dye", "#835432");
        DYE_COLORS.put("green_dye", "#5E7C16");
        DYE_COLORS.put("red_dye", "#B02E26");
        DYE_COLORS.put("black_dye", "#1D1D21");
        DYE_COLORS.put("soil_dye", "#8B7E6B");
    }

    public static int[] hexToRgb(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        return new int[]{
                Integer.parseInt(hex.substring(0, 2), 16),
                Integer.parseInt(hex.substring(2, 4), 16),
                Integer.parseInt(hex.substring(4, 6), 16)
        };
    }

    public static String rgbToHex(int r, int g, int b) {
        r = MathHelper.clamp(r, 0, 255);
        g = MathHelper.clamp(g, 0, 255);
        b = MathHelper.clamp(b, 0, 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public static int hexToInt(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        return Integer.parseInt(hex, 16);
    }

    public static String merge(String c1, String c2, float ratio) {
        int[] rgb1 = hexToRgb(c1 == null ? "#FFFFFF" : c1);
        int[] rgb2 = hexToRgb(c2 == null ? "#FFFFFF" : c2);
        float r = MathHelper.clamp(ratio, 0f, 1f);
        int rr = Math.round(rgb1[0] * (1 - r) + rgb2[0] * r);
        int rg = Math.round(rgb1[1] * (1 - r) + rgb2[1] * r);
        int rb = Math.round(rgb1[2] * (1 - r) + rgb2[2] * r);
        return rgbToHex(rr, rg, rb);
    }
}
