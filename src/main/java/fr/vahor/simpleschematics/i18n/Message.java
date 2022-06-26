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

package fr.vahor.simpleschematics.i18n;

import fr.vahor.simpleschematics.utils.EncodingResourceBundleControl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public enum Message {

    PREFIX,

    NO_PERMISSION,

    GLOBAL_HELP,
    SCHEMATIC_HELP,
    FOLDER_HELP,

    FOLDER_DONT_EXIST,

    SCHEMATIC_DONT_EXIST,
    SCHEMATIC_ALREADY_EXIST,
    INVALID_SCHEMATIC_NAME,
    INVALID_FOLDER_NAME,
    INVALID_TRIM,
    INVALID_SELECTION,
    EMPTY_SELECTION,
    FAWE_ERROR,
    UNKNOWN_ERROR,
    UNKNOWN_COMMAND,
    EMPTY_BLOCK_TARGET,
    COMMAND_NO_SELECTION,

    COMMAND_TOOL,
    COMMAND_RELOADED,
    COMMAND_TOGGLE,
    COMMAND_POSITION,
    COMMAND_POSITION_SET, // x,y,z, index

    COMMAND_THUMBNAIL_GENERATE, // folder, recursive
    COMMAND_THUMBNAIL_GENERATE_SUBTITLE, // folder
    COMMAND_THUMBNAIL_TOGGLE, // folder, enabled
    COMMAND_FOLDER_UPDATE_ICON, // folder, material, data
    COMMAND_SCHEMATIC_CREATE, // folder, schematic
    COMMAND_TRIM_SUCCESS,
    COMMAND_FROM_SUCCESS,
    COMMAND_TO_SUCCESS,


    TOOL_NAME,
    TOOL_LORE,

    INVENTORY_TITLE,
    SELECTED_INVENTORY_TITLE,

    INVENTORY_GO_BACK_NAME,
    INVENTORY_GO_BACK_LORE,

    INVENTORY_DESELECT_ALL_NAME,
    INVENTORY_DESELECT_ALL_LORE,

    INVENTORY_SELECT_ALL_NAME,
    INVENTORY_SELECT_ALL_LORE,

    INVENTORY_SELECTED_INVENTORY_NAME,
    INVENTORY_SELECTED_INVENTORY_LORE,
    INVENTORY_SELECT_ALL_SUCCESS,
    INVENTORY_DESELECT_ALL_SUCCESS,

    INVENTORY_ROTATION_NAME,
    INVENTORY_ROTATION_LORE, // {current}

    INVENTORY_FOLDER_NAME, // {name}
    INVENTORY_FOLDER_LORE,

    INVENTORY_SCHEMATIC_NAME,  // {name}
    INVENTORY_SCHEMATIC_LORE, // {path}

    INVENTORY_PREVIOUS_NAME,
    INVENTORY_PREVIOUS_LORE,

    INVENTORY_NEXT_NAME,
    INVENTORY_NEXT_LORE,

    LOADING_THUMBNAIL_LORE,
    THUMBNAIL_NOT_FOUND_LORE,

    SCHEMATIC_ENABLED,  // {name}
    SCHEMATIC_DISABLED,  // {name}

    INVALID_MATERIAL,


    ;

    private static final EncodingResourceBundleControl utf8Control = new EncodingResourceBundleControl("UTF-8");
    private static ResourceBundle bundle;

    public static void loadLanguage(File dataFolder) {
        try {
            ResourceBundle.clearCache();
            ClassLoader loader = new URLClassLoader(new java.net.URL[]{dataFolder.toURI().toURL()});
            bundle = ResourceBundle.getBundle("messages", Locale.getDefault(), loader, utf8Control);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return bundle.getString(name());
    }
}
