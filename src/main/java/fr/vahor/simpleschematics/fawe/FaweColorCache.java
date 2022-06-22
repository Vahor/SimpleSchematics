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
import fr.vahor.simpleschematics.utils.OutputHelper;

import java.awt.Color;

import static com.boydti.fawe.FaweCache.CACHE_COLOR;
import static com.boydti.fawe.FaweCache.getCombined;

public class FaweColorCache {

    public static void init() {
        OutputHelper.info("Initializing Fawe color cache...");

        int[] blocksLikeWool = {95, 159, 177, 160, 171}; // glass, clay, banner, glass-pane, carpet
        for (int blockId : blocksLikeWool) {
            for (int data = 0; data < 15; data++) {
                CACHE_COLOR[getCombined(blockId, data)] = CACHE_COLOR[getCombined(35, data)];
            }
        }

        CACHE_COLOR[getCombined(5, 4)] = ColorUtils.ColorData.GOLD.getColor(); // Acacia Planks

        CACHE_COLOR[getCombined(107, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Oak fence gate
        CACHE_COLOR[getCombined(183, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Spruce fence gate
        CACHE_COLOR[getCombined(174, 0)] = ColorUtils.ColorData.WHITE.getColor(); // Birch fence gate

        CACHE_COLOR[getCombined(85, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Oak Fence
        CACHE_COLOR[getCombined(188, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Spruce Fence
        CACHE_COLOR[getCombined(189, 0)] = ColorUtils.ColorData.WHITE.getColor(); // Birch Fence
        CACHE_COLOR[getCombined(190, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Jungle Fence
        CACHE_COLOR[getCombined(191, 0)] = ColorUtils.ColorData.DARK_GRAY.getColor(); // Jungle Fence

        CACHE_COLOR[getCombined(425, 3)] = ColorUtils.ColorData.GOLD.getColor(); // Wooden Door

        CACHE_COLOR[getCombined(125, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Acacia Wood Slab Block
        CACHE_COLOR[getCombined(126, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Oak Wood Slab
        CACHE_COLOR[getCombined(164, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Dark Oak Stairs


        CACHE_COLOR[getCombined(58, 0)]  = ColorUtils.ColorData.GOLD.getColor(); // Crafting Table
        CACHE_COLOR[getCombined(86, 0)]  = ColorUtils.ColorData.GOLD.getColor(); // Pumpkin
        CACHE_COLOR[getCombined(170, 0)] = ColorUtils.ColorData.YELLOW.getColor(); // Hay Bale
        CACHE_COLOR[getCombined(25, 0)]  = ColorUtils.ColorData.GOLD.getColor(); // JukeBox
        CACHE_COLOR[getCombined(61, 0)]  = ColorUtils.ColorData.GRAY.getColor(); // Furnace
        CACHE_COLOR[getCombined(54, 0)]  = ColorUtils.ColorData.GOLD.getColor(); // Chest
        CACHE_COLOR[getCombined(103, 0)] = ColorUtils.ColorData.GREEN.getColor(); // Melon Block
        CACHE_COLOR[getCombined(323, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Sign
        CACHE_COLOR[getCombined(68, 0)]  = ColorUtils.ColorData.GOLD.getColor(); // Wall Sign
        CACHE_COLOR[getCombined(324, 0)] = ColorUtils.ColorData.GOLD.getColor(); // Wooden Door
        CACHE_COLOR[getCombined(96, 0)]  = ColorUtils.ColorData.GOLD.getColor(); // TrapDoor

        CACHE_COLOR[getCombined(100, 15)] = new Color(255, 255, 255); // White Mushroom Block


        CACHE_COLOR[getCombined(162, 13)] = ColorUtils.ColorData._BROWN.getColor(); // FullBlock Dark Oak Wod

    }
}
