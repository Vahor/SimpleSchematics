package fr.vahor.simpleschematics.utils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Schema {

    private static Predicate<String> repetitiveSeparator;

    public static void generatePattern(String separator) {
        repetitiveSeparator = Pattern.compile(String.format("\\%s{2,}", separator)).asPredicate();
    }

    public static boolean isValidFolderName(String name, String separator) {
        return !name.contains(" ") && !repetitiveSeparator.test(name) && (name.equals(separator) || (!name.startsWith(separator) &&
                !name.endsWith(separator)));

    }
}
