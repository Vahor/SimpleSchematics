package fr.vahor;

import fr.vahor.exceptions.FolderNotFoundException;
import fr.vahor.exceptions.InvalidFolderNameException;
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