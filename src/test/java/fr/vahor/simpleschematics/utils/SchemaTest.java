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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemaTest {

    @Test
    void isValidFolderName() {
        String separator = ".";
        Schema.generatePattern(separator);
        assertTrue(Schema.isValidFolderName("trees.big.truc.a", separator));
        assertFalse(Schema.isValidFolderName("..", separator));
        assertFalse(Schema.isValidFolderName(" . . ", separator));
        assertFalse(Schema.isValidFolderName("../../truc", separator));
        assertFalse(Schema.isValidFolderName("..t.r.u.c.t.t.t.t", separator));
    }
}