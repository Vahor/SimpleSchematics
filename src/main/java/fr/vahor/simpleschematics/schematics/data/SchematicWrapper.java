package fr.vahor.simpleschematics.schematics.data;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.world.registry.WorldData;
import fr.vahor.simpleschematics.API;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SchematicWrapper extends ASchematic {

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    @Getter @Setter private Clipboard clipboard = null; // todo clear cache after x minutes of inactivity
    @Getter @Setter private Thumbnail thumbnail = null; // todo clear cache after x minutes of inactivity

    public SchematicWrapper(String name) {
        super(name);
    }

    public void loadSchematic(WorldData worldData, boolean force) throws IOException {
        if (clipboard == null || force) {
            File file = getAsFile();
            ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file));
            clipboard = reader.read(worldData);
        }
    }

    public CompletableFuture<Void> loadThumbnail(boolean force) {
        if (thumbnail == null || force) {
            File thumbnailFile = getThumbnailFile();
            if (thumbnailFile.exists())
                return CompletableFuture.runAsync(() -> {
                    try {
                        thumbnail = new Thumbnail(ImageIO.read(thumbnailFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, executor);

            else
                thumbnail = new Thumbnail(null);

        }
        return CompletableFuture.completedFuture(null);
    }

    public File getThumbnailFile() {
        String path = getPath(API.SYSTEM_SEPARATOR, ".png");
        File file = new File(API.getConfiguration().getSchematicsFolderPath(), path);
        return file.getAbsoluteFile();
    }
}
