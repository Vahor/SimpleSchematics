package fr.vahor.simpleschematics.i18n;

import fr.vahor.simpleschematics.utils.EncodingResourceBundleControl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public enum Message {

    PREFIX,

    FOLDER_DONT_EXIST,

    SCHEMATIC_DONT_EXIST,
    SCHEMATIC_ALREADY_EXIST,

    EMPTY_SELECTION,

    TOOL_NAME,
    TOOL_LORE,

    INVENTORY_TITLE,
    SELECTED_INVENTORY_TITLE,

    INVENTORY_GO_BACK_NAME,
    INVENTORY_GO_BACK_LORE,

    INVENTORY_SELECT_ALL_NAME,
    INVENTORY_SELECT_ALL_LORE,

    INVENTORY_SELECTED_INVENTORY_NAME,
    INVENTORY_SELECTED_INVENTORY_LORE,

    INVENTORY_ROTATION_NAME,
    INVENTORY_ROTATION_LORE, // {current}

    INVENTORY_FOLDER_NAME, // {name}
    INVENTORY_FOLDER_LORE,

    INVENTORY_SCHEMATIC_NAME,  // {name}
    INVENTORY_SCHEMATIC_LORE, // {path}

    LOADING_THUMBNAIL_LORE,
    THUMBNAIL_NOT_FOUND_LORE,

    SCHEMATIC_ENABLED,  // {name}
    SCHEMATIC_DISABLED,  // {name}

    ;

    private static ResourceBundle bundle;
    private static final EncodingResourceBundleControl utf8Control = new EncodingResourceBundleControl("UTF-8");

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
