package fr.vahor.i18n;

import java.util.ResourceBundle;

public enum Message {

    PREFIX,

    FOLDER_DONT_EXIST,
    FOLDER_ALREADY_EXISTS,

    TOOL_NAME,
    TOOL_LORE,


    INVENTORY_TITLE,

    INVENTORY_GO_BACK,

    INVENTORY_FOLDER_NAME, // {name}
    INVENTORY_FOLDER_LORE,

    INVENTORY_SCHEMATIC_NAME,  // {name}
    INVENTORY_SCHEMATIC_LORE,

    ;

    private static ResourceBundle bundle;

    public static void loadLanguage() {
        bundle = ResourceBundle.getBundle("messages");
    }

    @Override
    public String toString() {
        return bundle.getString(name());
    }
}
