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

package fr.vahor;

import fr.vahor.simpleschematics.exceptions.FolderNotFoundException;
import fr.vahor.simpleschematics.exceptions.InvalidFolderNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class APITest {

//    WorldData legacyWorldData;

    @BeforeEach
    void setUp() {


//        Config mockedConfig = mock(Config.class);
//        when(mockedConfig.getToolIconMaterial()).thenReturn("DIRT");
//        when(mockedConfig.getSchematicsFolderPath()).thenReturn("data/schematics");
//        when(mockedConfig.getSeparator()).thenReturn(".");
//        when(mockedConfig.getSeparatorPattern()).thenReturn(Pattern.compile(String.format("\\%s", ".")));
//
//        Schema.generatePattern(mockedConfig.getSeparator());
//
//        API.setConfig(mockedConfig);
//        API.initializeLogger(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME));
//        API.loadSchematics();
//
//        legacyWorldData = LegacyWorldData.getInstance();

    }

    @Test
    void addNewSchematic() throws IOException, InvalidFolderNameException {
//        List<ASchematic> defaultSchematics = API.getFolderByName("default").getChildren();
//        SchematicWrapper schematic = (SchematicWrapper) defaultSchematics.get(0);
//        Clipboard clipboard = schematic.loadSchematic(null);
//        System.out.println("clipboard = " + clipboard);
//
//        API.addNewSchematic(clipboard,
//                legacyWorldData,
//                API.getOrCreateFolder("arbre.machin.truc"),
//                "petit");

    }

    @Test
    void testManageFolder() throws FolderNotFoundException, IOException {
//        API.getOrCreateFolder("trees.big.truc.a");
//        API.moveFolder("trees.big", "trees.test.truc.b");
//
//
//        SchematicFolder folder = API.getFolderByName("trees.test");
//        // move
//
//        final CuboidRegion region = new CuboidRegion(new Vector(0, 0, 0), new Vector(100, 100, 100));
//
//        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
//        clipboard.setOrigin(new Vector(50, 50, 50));
//
//        API.addNewSchematic(clipboard, legacyWorldData, folder, "test");

    }
}