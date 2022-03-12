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

package fr.vahor.simpleschematics.schematics;

import java.util.HashMap;
import java.util.Map;

public enum RotationMode {

    DEFAULT,
    AUTO,
    RANDOM;

    private static final Map<Integer, RotationMode> byOrdinal = new HashMap<>(3, 1);
    private static final int maxOrdinal;

    static {
        RotationMode[] values = values();
        for (RotationMode value : values) {
            byOrdinal.put(value.ordinal(), value);
        }
        maxOrdinal = values.length;
    }

    public RotationMode next() {
        return byOrdinal.get((ordinal() + 1) % maxOrdinal);
    }
}
