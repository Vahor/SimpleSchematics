package fr.vahor.simpleschematics;

import com.boydti.fawe.object.exception.FaweException;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.SessionManager;
import fr.vahor.simpleschematics.exceptions.FolderNotFoundException;
import fr.vahor.simpleschematics.exceptions.InvalidFolderNameException;
import fr.vahor.simpleschematics.fawe.PNGWriter;
import fr.vahor.simpleschematics.schematics.Direction;
import fr.vahor.simpleschematics.schematics.RotationMode;
import fr.vahor.simpleschematics.schematics.SchematicsPlayer;
import fr.vahor.simpleschematics.schematics.data.ASchematic;
import fr.vahor.simpleschematics.schematics.data.SchematicFolder;
import fr.vahor.simpleschematics.schematics.data.SchematicWrapper;
import fr.vahor.simpleschematics.schematics.data.Thumbnail;
import fr.vahor.simpleschematics.utils.Schema;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class API {

    public static final Random RANDOM = new Random();
    private static final Map<UUID, SchematicsPlayer> playerMap = new HashMap<>();
    private static final Map<String, SchematicFolder> foldersByName = new HashMap<>();
    @Getter private static SchematicFolder rootSchematicFolder;
    @Getter private static Config configuration;
    @Getter private static Logger logger;

    public static final String SYSTEM_SEPARATOR = System.getProperty("file.separator");

    @Getter private static Material toolIcon;

    public static void initializeConfiguration(FileConfiguration configFile) {
        API.configuration = new Config(configFile);
    }

    @Deprecated
    public static void setConfig(Config config) {
        API.configuration = config;
    }

    public static void initializeLogger(Logger logger) {
        API.logger = logger;
    }

    public static Collection<SchematicsPlayer> getPlayers() {
        return playerMap.values();
    }

    public static void init() {
        try {
            toolIcon = Material.valueOf(configuration.getToolIconMaterial());
        } catch (Exception e) {
            toolIcon = Material.values()[1];
            e.printStackTrace();
        }
    }

    public static SchematicsPlayer getOrAddPlayer(UUID uuid) {
        SchematicsPlayer player = playerMap.get(uuid);
        if (player != null) return player;
        return addPlayer(uuid);
    }

    public static SchematicsPlayer addPlayer(UUID uuid) {
        SchematicsPlayer schematicsPlayer = new SchematicsPlayer(uuid);
        playerMap.put(uuid, schematicsPlayer);
        return schematicsPlayer;
    }

    public static void removePlayer(UUID uuid) {
        playerMap.remove(uuid);
    }

    public static SchematicFolder getFolderByName(String name) {
        return foldersByName.get(name);
    }

    public static void loadSchematics() {
        foldersByName.clear();
        File directory = new File(configuration.getSchematicsFolderPath());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        rootSchematicFolder = loadSchematicsInFolder(directory);
        foldersByName.put(configuration.getSeparator(), rootSchematicFolder);
    }

    private static SchematicFolder loadSchematicsInFolder(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            getLogger().severe("INTERNAL - loadSchematicsInFolder files list null");
            return null;
        }

        SchematicFolder schematicFolder = new SchematicFolder(directory.getName());


        for (File file : files) {
            ASchematic schematic;

            if (file.isDirectory())
                schematic = loadSchematicsInFolder(file);
            else
                schematic = registerSchematics(file);

            if (schematic != null) {
                schematicFolder.addSchematic(schematic);

            }
        }

        // Using file path as recursion is not finished here
        String pathInSystem = directory.getPath()
                .replaceFirst(configuration.getSchematicsFolderPath(), "")
                .replace(SYSTEM_SEPARATOR, configuration.getSeparator())
                .replaceFirst(configuration.getSeparator(), "");

        if (pathInSystem.isEmpty()) pathInSystem = configuration.getSeparator();

        foldersByName.put(pathInSystem, schematicFolder);

        return schematicFolder;
    }

    private static SchematicWrapper registerSchematics(File file) {
        if (!file.getName().endsWith(".schematic")) {
            return null;
        }

        SchematicWrapper schematicWrapper = new SchematicWrapper(file.getName());
        try {
            schematicWrapper.loadThumbnail(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schematicWrapper;

    }

    public static void addNewSchematic(Clipboard clipboard, SchematicFolder folder, String fileName) throws IOException {
        if (!fileName.endsWith(".schematic")) {
            fileName += ".schematic";
        }

        SchematicWrapper schematic = new SchematicWrapper(fileName);
        schematic.setClipboard(clipboard);
        folder.addSchematic(schematic);

        File schematicFile = schematic.getAsFile();

        try (FileOutputStream schematicOutput = new FileOutputStream(schematicFile)) {
            ClipboardWriter writerSchematic = ClipboardFormat.SCHEMATIC.getWriter(schematicOutput);
            writerSchematic.write(clipboard, clipboard.getRegion().getWorld().getWorldData());

            System.out.println(fileName + " saved " + schematicFile.getCanonicalPath());
        }

        generateThumbnail(schematic); // todo async
    }

    public static void generateThumbnail(SchematicWrapper schematic) throws IOException {
        Clipboard clipboard = schematic.getClipboard();
        File thumbnailFile = schematic.getThumbnailFile();
        try (FileOutputStream thumbnailOutput = new FileOutputStream(thumbnailFile)) {

            PNGWriter writer = new PNGWriter(thumbnailOutput);
            BufferedImage bufferedImage = writer.write(clipboard, Thumbnail.MAX_THUMBNAIL_SIZE);
            schematic.setThumbnail(new Thumbnail(bufferedImage));

            System.out.println(schematic.getName() + " thumbnail saved " + thumbnailFile.getCanonicalPath());
        }
    }

    public static void pasteSchematic(Clipboard clipboard, SchematicsPlayer player, @NotNull Vector toLocation) {
        try {
            EditSession editSession = player.getFawePlayer().getNewEditSession();
            editSession.setBlockChangeLimit(-1);
            editSession.setFastMode(true);

            AffineTransform transform = null;
            if (player.getRotationMode() == RotationMode.RANDOM) {
                transform = new AffineTransform();
                transform = transform.rotateY(Direction.random().getRotation());
            }
            else if (player.getRotationMode() == RotationMode.AUTO) {

                // Get direction from clipboard
                Vector origin = clipboard.getOrigin();
                Vector center = clipboard.getRegion().getCenter();

                // Direction from origin to center
                double diffX = origin.getX() - center.getX();
                double diffZ = origin.getZ() - center.getZ();
                double atan2 = Math.atan2(diffX, diffZ);

                Direction initialDirection = Direction.getDirectionByAngle(atan2);
                Direction playerDirection = Direction.getDirectionByYaw(player.getPlayer().getLocation().getYaw());

                transform = new AffineTransform();
                transform = transform.rotateY(playerDirection.getRotation() - initialDirection.getRotation());
            }

            new Schematic(clipboard).paste(editSession, toLocation, true, false, transform);
            editSession.flushQueue();

            // Push into history
            SessionManager manager = WorldEdit.getInstance().getSessionManager();
            LocalSession localSession = manager.get(player.getFawePlayer().getPlayer());
            localSession.remember(editSession);
        } catch (FaweException e) {
            player.getPlayer().sendMessage("todo fawe /wea");
            e.printStackTrace();
        }
    }

    public static SchematicFolder getOrCreateFolder(String folderNameWithSeparator) throws InvalidFolderNameException {
        if (!Schema.isValidFolderName(folderNameWithSeparator, configuration.getSeparator())) {
            throw new InvalidFolderNameException();
        }

        // folderNameWithSeparator : trees.big
        // addFolder trees and big

        SchematicFolder folder = getFolderByName(folderNameWithSeparator);
        // If folder already exists, return
        if (folder != null) {
            return folder;
        }

        String[] possibleFolderNames = configuration.getSeparatorPattern().split(folderNameWithSeparator);
        SchematicFolder parent = rootSchematicFolder;
        folder = new SchematicFolder(possibleFolderNames[possibleFolderNames.length - 1]);

        for (int i = 0; i < possibleFolderNames.length - 1; i++) {
            SchematicFolder possibleParent = getFolderByName(possibleFolderNames[i]);

            if (possibleParent == null) {
                // Register parent folder
                possibleParent = new SchematicFolder(possibleFolderNames[i]);
                parent.addSchematic(possibleParent);

                foldersByName.put(possibleParent.getPath(), possibleParent);
            }

            parent = possibleParent;
        }

        parent.addSchematic(folder);

        foldersByName.put(folder.getPath(), folder);

        folder.getAsFile().mkdirs();

        return folder;
    }

    public static void moveFolder(String fromName, String toName) throws FolderNotFoundException, IOException {

        // Unload fromName schematics
        // Use file management to move
        // Then load toName schematics with loadSchematicsInFolder

        SchematicFolder fromFolder = getFolderByName(fromName);
        if (fromFolder == null) {
            throw new FolderNotFoundException();
        }

        // Create new folder and register schematics
        SchematicFolder toFolder = getOrCreateFolder(toName);

        // Unload currentFolder
        unloadFolderRecursive(fromFolder);

        // Move folder
        File fromFolderAsFile = fromFolder.getAsFile();
        FileUtils.copyDirectory(fromFolderAsFile, toFolder.getAsFile());
        FileUtils.deleteDirectory(fromFolderAsFile);

    }

    public static void deleteFolder(SchematicFolder folder) throws FolderNotFoundException, IOException {
        if (folder == null) {
            throw new FolderNotFoundException();
        }
        unloadFolderRecursive(folder);

        FileUtils.deleteDirectory(folder.getAsFile());
    }

    private static void unloadFolderRecursive(@NotNull SchematicFolder folder) {

        // Delete every child
        for (ASchematic child : folder.getChildren()) {
            if (child instanceof SchematicFolder) {
                unloadFolderRecursive((SchematicFolder) child);
            }
        }
        unloadFolder(folder);
    }

    private static void unloadFolder(@NotNull SchematicFolder folder) {
        foldersByName.remove(folder.getPath());
    }
}
