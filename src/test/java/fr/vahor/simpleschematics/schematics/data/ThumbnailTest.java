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
        File resources = new File("src/test/resources");
    }
}