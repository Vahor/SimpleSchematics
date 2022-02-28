package fr.vahor.i18n;

import java.util.ResourceBundle;

public enum Message {

    PREFIX,

    FOLDER_DONT_EXIST,
    FOLDER_ALREADY_EXISTS

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
