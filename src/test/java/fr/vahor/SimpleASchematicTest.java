package fr.vahor;

import fr.vahor.exceptions.FolderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleASchematicTest {

    @BeforeEach
    void setUp() {


        Config mockedConfig = mock(Config.class);
        when(mockedConfig.getToolIconMaterial()).thenReturn("DIRT");
        when(mockedConfig.getSchematicsFolderPath()).thenReturn("data/schematics");
        when(mockedConfig.getSeperator()).thenReturn(".");
        when(mockedConfig.getSeperatorPattern()).thenReturn(Pattern.compile(String.format("\\%s", ".")));

        API.setConfig(mockedConfig);
        API.initializeLogger(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME));
        API.loadSchematics();

    }

    @Test
    void testManageFolder() throws FolderNotFoundException {
        API.addNewFolder("trees.big.truc.a");
        API.moveFolder("trees.big.truc.a", "trees.test.truc.b");
        API.deleteFolder(API.getFolderByName("trees.test.truc.b"));

    }
}