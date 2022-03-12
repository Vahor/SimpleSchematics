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

package fr.vahor.simpleschematics.schematics.data;

import fr.vahor.simpleschematics.API;
import fr.vahor.simpleschematics.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ThumbnailTest {

    @BeforeEach
    void setUp() {
        Config mockedConfig = mock(Config.class);
        when(mockedConfig.getThumbnailSize()).thenReturn(24);

        API.setConfig(mockedConfig);
        API.initializeLogger(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME));

    }

    @Test
    void toStringList() throws IOException {
        File resources = new File("src/test/resources");
        File file = new File(resources, "t.schematic.png");
        System.out.println("file.getAbsoluteFile() = " + file.getAbsoluteFile());
        Thumbnail thumbnail = new Thumbnail(ImageIO.read(file));
    }

    @Test
    void color() throws FileNotFoundException {
    }
}