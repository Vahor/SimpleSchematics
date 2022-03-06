package fr.vahor.fawe;

import java.awt.*;

import static com.boydti.fawe.FaweCache.CACHE_COLOR;
import static com.boydti.fawe.FaweCache.getCombined;

public class FaweColorCache {

    public static void init() {
        CACHE_COLOR[getCombined(159, 14)] = new Color(152, 67, 52);
    }
}
