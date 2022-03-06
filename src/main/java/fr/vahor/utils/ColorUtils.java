package fr.vahor.utils;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.awt.*;

public class ColorUtils {

    public static ColorData getClosestColor(int r, int g, int b) {
        Color fromColor = new Color(r, g, b);

        ColorData bestMatch = null;
        double min = Double.MAX_VALUE; // todo min = first value
        for (ColorData value : ColorData.getValues()) {
            double distance = getDistance(fromColor, value.color);

            // Don't need to check other values if this one is very close
            if (distance < 6_500) {
                return value;
            }

            if (distance < min) {
                min       = distance;
                bestMatch = value;
            }
        }

        System.out.println("min = " + min);

        return bestMatch;
    }

    // https://en.wikipedia.org/wiki/Color_difference
    public static double getDistance(Color from, Color to) {
        return
                Math.pow(to.getRed() - from.getRed(), 2) +
                        Math.pow(to.getGreen() - from.getGreen(), 2) +
                        Math.pow(to.getBlue() - from.getBlue(), 2);
    }

    public enum ColorData {
        BLACK(Color.BLACK, ChatColor.BLACK),
        DARK_BLUE(new Color(0, 0, 170), ChatColor.DARK_BLUE),
        DARK_GREEN(new Color(0, 170, 0), ChatColor.DARK_GREEN),
        DARK_AQUA(new Color(0, 170, 170), ChatColor.DARK_AQUA),
        DARK_RED(new Color(170, 0, 0), ChatColor.DARK_AQUA),
        DARK_PURPLE(new Color(170, 0, 170), ChatColor.DARK_PURPLE),
        GOLD(new Color(255, 170, 0), ChatColor.GOLD),
        GRAY(new Color(170, 170, 170), ChatColor.GRAY),
        DARK_GRAY(new Color(85, 85, 85), ChatColor.DARK_GRAY),
        BLUE(new Color(85, 85, 255), ChatColor.BLUE),
        GREEN(new Color(85, 255, 85), ChatColor.GREEN),
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
