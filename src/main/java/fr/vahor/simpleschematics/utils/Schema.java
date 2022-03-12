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

import fr.vahor.simpleschematics.API;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Schema {

    private static Predicate<String> repetitiveSeparator;

    public static void generatePattern(String separator) {
        repetitiveSeparator = Pattern.compile(String.format("\\%s{2,}", separator)).asPredicate();
    }

    public static boolean isValidFolderName(String name, String separator) {
        return !name.contains(" ") && !name.contains(API.SYSTEM_SEPARATOR) && !repetitiveSeparator.test(name) && (name.equals(separator) || (!name.startsWith(separator) &&
                !name.endsWith(separator)));
    }
}
