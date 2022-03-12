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

package fr.vahor.simpleschematics.fawe;

import fr.vahor.simpleschematics.utils.ColorUtils;

import java.awt.*;

import static com.boydti.fawe.FaweCache.CACHE_COLOR;
import static com.boydti.fawe.FaweCache.getCombined;

public class FaweColorCache {

    public static void init() {
        System.out.println("FaweColorCache");
        CACHE_COLOR[getCombined(159, 13)] = new Color(85, 93, 46); // Green Stained Clay
        CACHE_COLOR[getCombined(159, 14)] = new Color(152, 67, 52); // Red Stained Clay

        CACHE_COLOR[getCombined(5, 4)] = new Color(194, 115, 67); // Acacia Planks

        CACHE_COLOR[getCombined(125, 4)] = new Color(194, 115, 67); // Acacia Wood Slab Block
        CACHE_COLOR[getCombined(126, 4)] = new Color(194, 115, 67); // Acacia Wood Slab UP
        CACHE_COLOR[getCombined(126, 12)] = new Color(194, 115, 67); // Acacia Wood Slab DOWN

        CACHE_COLOR[getCombined(100, 15)] = new Color(255, 255, 255); // White Mushroom Block


        CACHE_COLOR[getCombined(162, 13)] = ColorUtils.ColorData._BROWN.getColor(); // FullBlock Dark Oak Wod

    }
}
