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
