package fr.vahor;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.data.DataException;
import fr.vahor.exceptions.FolderNotFoundException;
import fr.vahor.exceptions.InvalidFolderNameException;
import fr.vahor.schematics.data.ASchematic;
import fr.vahor.schematics.data.SchematicWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class APITest {

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
    void addNewSchematic() throws IOException, DataException, InvalidFolderNameException {
        List<ASchematic> defaultSchematics = API.getFolderByName("default").getChildren();
        SchematicWrapper schematic = (SchematicWrapper) defaultSchematics.get(0);
        CuboidClipboard clipboard = schematic.loadSchematic();
        System.out.println("clipboard = " + clipboard);

        API.addNewSchematic(clipboard,
                API.addNewFolder("arbre.machin.truc"),
                "petit");

    }

    @Test
    void testManageFolder() throws FolderNotFoundException, IOException {
        API.addNewFolder("trees.big.truc.a");
        API.moveFolder("trees.big", "trees.test.truc.b");
    }
}