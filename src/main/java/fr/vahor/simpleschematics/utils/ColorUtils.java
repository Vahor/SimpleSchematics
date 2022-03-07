package fr.vahor.simpleschematics.utils;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.awt.*;

public class ColorUtils {

    public static ColorData getClosestColor(int r, int g, int b) {
        ColorData bestMatch = null;
        double min = Double.MAX_VALUE; // todo min = first value
        for (ColorData value : ColorData.getValues()) {
            double distance = getDistance(r, g, b, value.color);

            // Don't need to check other values if this one is very close
//            if (distance < 6_500) {
//                return value;
//            }

            if (distance < min) {
                min       = distance;
                bestMatch = value;
            }
        }

        if(bestMatch.chatColor == ChatColor.GOLD) {
            System.out.println("r  = " + r);
            System.out.println("g = " + g);
            System.out.println("b = " + b);
            System.out.println("bestMatch = " + bestMatch);
            System.out.println("min = " + min);
        }
        return bestMatch;
    }

    // https://en.wikipedia.org/wiki/Color_difference
    public static double getDistance(int r, int g, int b, Color to) {
        return
                Math.pow(to.getRed() - r, 2) +
                        Math.pow(to.getGreen() - g, 2) +
                        Math.pow(to.getBlue() - b, 2);
    }

    public enum ColorData {
        BLACK(Color.BLACK, ChatColor.BLACK),
        DARK_BLUE(new Color(0, 0, 170), ChatColor.DARK_BLUE),

        DARK_GREEN(new Color(0, 170, 0), ChatColor.DARK_GREEN),
        _METALLIC_GREEN(new Color(54, 100, 15), ChatColor.DARK_GREEN),
        _CAMO_GREEN(new Color(85, 93, 46), ChatColor.DARK_GREEN),

        DARK_AQUA(new Color(0, 170, 170), ChatColor.DARK_AQUA),

        DARK_RED(new Color(170, 0, 0), ChatColor.DARK_RED),
        _BLOOD(new Color(106, 46, 37), ChatColor.DARK_RED),
        _CHESTNUT(new Color(162, 67, 52), ChatColor.DARK_RED),

        DARK_PURPLE(new Color(170, 0, 170), ChatColor.DARK_PURPLE),

        GOLD(new Color(255, 170, 0), ChatColor.GOLD),
        _ORANGE(new Color(235, 150, 75), ChatColor.GOLD),
        _BROWN(new Color(115, 72, 37), ChatColor.GOLD),

        GRAY(new Color(170, 170, 170), ChatColor.GRAY),

        DARK_GRAY(new Color(85, 85, 85), ChatColor.DARK_GRAY),

        BLUE(new Color(85, 85, 255), ChatColor.BLUE),

        GREEN(new Color(85, 255, 85), ChatColor.GREEN),
        _MOSS(new Color(121, 132, 64), ChatColor.GREEN),

        AQUA(new Color(85, 255, 255), ChatColor.AQUA),

        RED(new Color(255, 85, 85), ChatColor.RED),

        LIGHT_PURPLE(new Color(255, 85, 255), ChatColor.LIGHT_PURPLE),

        YELLOW(new Color(255, 255, 85), ChatColor.YELLOW),

        WHITE(new Color(255, 255, 255), ChatColor.WHITE),
        ;

        @Getter private final Color color;
        @Getter private final ChatColor chatColor;

        @Getter private static final ColorData[] values = values();

        ColorData(Color color, ChatColor chatColor) {
            this.color     = color;
            this.chatColor = chatColor;
        }
    }
}
