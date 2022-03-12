/*
 *     Copyright (C) 2022 Nathan David
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
            if (distance < 500) {
                return value;
            }

            if (distance < min) {
                min       = distance;
                bestMatch = value;
            }
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

        GOLD(new Color(255, 170, 0), ChatColor.GOLD),
        _ORANGE(new Color(235, 150, 75), ChatColor.GOLD),
        _BROWN(new Color(135, 79, 34), ChatColor.GOLD),
        _COPPER(new Color(194, 114, 66), ChatColor.GOLD),
        _DARK_ORANGE(new Color(155, 70, 0), ChatColor.GOLD),
        _COCONUT(new Color(160, 95, 55), ChatColor.GOLD),
        _JASPER_ORANGE(new Color(235, 147, 75), ChatColor.GOLD),

        DARK_RED(new Color(170, 0, 0), ChatColor.DARK_RED),
        _CHESTNUT(new Color(162, 67, 52), ChatColor.DARK_RED),
        _REDWOOD(new Color(166, 95, 83), ChatColor.DARK_RED),
        _BLOOD(new Color(120, 45, 35), ChatColor.DARK_RED),

        DARK_PURPLE(new Color(170, 0, 170), ChatColor.DARK_PURPLE),
        _METALLIC_VIOLET(new Color(84, 0, 140), ChatColor.DARK_PURPLE),

        GRAY(new Color(170, 170, 170), ChatColor.GRAY),

        DARK_GRAY(new Color(85, 85, 85), ChatColor.DARK_GRAY),
        _ROSE_EBONY(new Color(98, 67, 62), ChatColor.DARK_GRAY),

        BLUE(new Color(0, 0, 255), ChatColor.BLUE),
        _BLUE(new Color(85, 85, 255), ChatColor.BLUE),
        _CHINESE_BLUE(new Color(60, 92, 156), ChatColor.BLUE),

        GREEN(new Color(0, 255, 0), ChatColor.GREEN),
        _GREEN(new Color(85, 255, 85), ChatColor.GREEN),
        _MOSS(new Color(121, 132, 64), ChatColor.GREEN),
        _DARK_LEMON_LIME(new Color(108, 214, 34), ChatColor.GREEN),

        AQUA(new Color(85, 255, 255), ChatColor.AQUA),
        _DARKER_AQUA(new Color(135, 194, 234), ChatColor.AQUA),

        RED(new Color(255, 0, 0), ChatColor.RED),
        _RED(new Color(255, 85, 85), ChatColor.RED),
        _LIGHT_RED(new Color(208, 117, 101), ChatColor.RED),
        _LIGHT_RED2(new Color(215, 95, 73), ChatColor.RED),

        LIGHT_PURPLE(new Color(255, 85, 255), ChatColor.LIGHT_PURPLE),

        YELLOW(new Color(255, 255, 0), ChatColor.YELLOW),
        _YELLOW(new Color(255, 255, 85), ChatColor.YELLOW),

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
