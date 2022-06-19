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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SkullUtils {

    private static final HashMap<String, GameProfile> SKULL_CACHE = new HashMap<>();

    /**
     * Gets the texture of a skull
     *
     * @param meta The SkullMeta of the skull you want to get the base64 of.
     * @return The texture of the skull.
     */
    public static String getSkullBase64(SkullMeta meta) {
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            Object profile = profileField.get(meta);

            GameProfile gameProfile = (GameProfile) profile;

            Collection<Property> textures = gameProfile.getProperties().get("textures");

            return textures.iterator().next().getValue();

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static void setSkullBase64(SkullMeta meta, String base64) {

        GameProfile gameProfile = SKULL_CACHE.get(base64);

        if (gameProfile == null) {
            gameProfile = new GameProfile(UUID.randomUUID(), "SimpleSchematics");
            gameProfile.getProperties().put("textures", new Property("textures", base64));
            SKULL_CACHE.put(base64, gameProfile);
        }

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
