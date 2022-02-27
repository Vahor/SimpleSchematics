package fr.vahor.utils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Schema {

    private static final Predicate<String> repetitiveSeparator = Pattern.compile("\\.{2,}").asPredicate();

    public static boolean isValidFolderName(String name) {
        return !name.startsWith(".") &&
                !name.endsWith(".") && !repetitiveSeparator.test(name);

    }
}
