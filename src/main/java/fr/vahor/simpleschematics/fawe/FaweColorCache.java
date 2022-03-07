package fr.vahor.simpleschematics.fawe;

import java.awt.*;

import static com.boydti.fawe.FaweCache.CACHE_COLOR;
import static com.boydti.fawe.FaweCache.getCombined;

public class FaweColorCache {

    public static void init() {
        CACHE_COLOR[getCombined(159, 13)] = new Color(85, 93, 46); // Green Stained Clay
        CACHE_COLOR[getCombined(159, 14)] = new Color(152, 67, 52); // Red Stained Clay

        CACHE_COLOR[getCombined(5, 4)] = new Color(194, 115, 67); // Acacia Planks

        CACHE_COLOR[getCombined(125, 4)] = new Color(194, 115, 67); // Acacia Wood Slab Block
        CACHE_COLOR[getCombined(126, 12)] = new Color(194, 115, 67); // Acacia Wood Slab

        CACHE_COLOR[getCombined(100, 15)] = new Color(255, 255, 255); // White Mushroom Block
    }
}
