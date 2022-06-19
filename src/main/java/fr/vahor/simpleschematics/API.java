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
import fr.vahor.simpleschematics.exceptions.InvalidFolderNameException;
import fr.vahor.simpleschematics.exceptions.InvalidSchematicNameException;
import fr.vahor.simpleschematics.fawe.PNGWriter;
import fr.vahor.simpleschematics.i18n.Message;
import fr.vahor.simpleschematics.schematics.Direction;
import fr.vahor.simpleschematics.schematics.RotationMode;
import fr.vahor.simpleschematics.schematics.SchematicsPlayer;
import fr.vahor.simpleschematics.schematics.data.ASchematic;
import fr.vahor.simpleschematics.schematics.data.SchematicFolder;
import fr.vahor.simpleschematics.schematics.data.SchematicWrapper;
import fr.vahor.simpleschematics.schematics.data.Thumbnail;
import fr.vahor.simpleschematics.utils.OutputHelper;
import fr.vahor.simpleschematics.utils.Schema;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public static List<String> getFoldersNames() {
        return foldersByName.values().stream().map(ASchematic::getPath).collect(Collectors.toList());
    }

    public static void init() {
        try {
            toolIcon = Material.valueOf(configuration.getToolIconMaterial());
        } catch (Exception e) {
            toolIcon = Material.values()[1];
            e.printStackTrace();
        }

        foldersByName.clear();
        rootSchematicFolder = null;
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
        foldersByName.values().forEach(SchematicFolder::loadConfiguration);
    }

    private static SchematicFolder loadSchematicsInFolder(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            getLogger().severe("INTERNAL - loadSchematicsInFolder files list null");
            return null;
        }

        String folderName = directory.getName();
        if (folderName.equals(configuration.getSchematicsFolderName())) folderName = ".";
        SchematicFolder schematicFolder = new SchematicFolder(folderName);

        for (File file : files) {
            ASchematic schematic;

            if (file.isDirectory()) {
                schematic = loadSchematicsInFolder(file);
            }
            else
                schematic = registerSchematics(file);

            if (schematic != null) {
                schematicFolder.addSchematic(schematic);
            }
        }

        // Using file path as recursion is not finished here
        String pathInSystem = directory.getPath()
                .replace(SYSTEM_SEPARATOR, "/") // System compatibility => Mac separator is / ; and Windows is \
                .replaceFirst(configuration.getSchematicsFolderPath(), "") // Don't need to prefix with plugin/../Schematics
                .replace("/", configuration.getSeparator()); // Replaces all '/' by custom separator

        if (pathInSystem.isEmpty()) pathInSystem = configuration.getSeparator();
        else if (pathInSystem.startsWith(configuration.getSeparator())) pathInSystem = pathInSystem.substring(configuration.getSeparator().length()); // Remove first seperator

        foldersByName.put(pathInSystem, schematicFolder);

        return schematicFolder;
    }

    private static SchematicWrapper registerSchematics(File file) {
        if (!file.getName().endsWith(".schematic")) {
            return null;
        }

        return new SchematicWrapper(file.getName().substring(0, file.getName().lastIndexOf(".schematic")));

    }

    public static void addNewSchematic(Clipboard clipboard, SchematicFolder folder, String fileName) throws IOException, InvalidSchematicNameException {
        if (!Schema.isValidFolderName(fileName, configuration.getSeparator())) {
            throw new InvalidSchematicNameException();
        }

        SchematicWrapper schematic = new SchematicWrapper(fileName);
        schematic.setClipboard(clipboard);
        folder.addSchematic(schematic);

        if (!fileName.endsWith(".schematic")) {
            fileName += ".schematic";
        }

        File schematicFile = schematic.getAsFile();

        try (FileOutputStream schematicOutput = new FileOutputStream(schematicFile)) {
            ClipboardWriter writerSchematic = ClipboardFormat.SCHEMATIC.getWriter(schematicOutput);
            writerSchematic.write(clipboard, clipboard.getRegion().getWorld().getWorldData());

            OutputHelper.info("Saved " + fileName + " to " + schematicFile.getCanonicalPath());
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

            OutputHelper.info(schematic.getName() + " thumbnail saved " + thumbnailFile.getCanonicalPath());
        }
    }

    public static void pasteSchematic(Clipboard clipboard, SchematicsPlayer player, Vector toLocation) {
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
            player.getPlayer().sendMessage(Message.PREFIX + Message.FAWE_ERROR.toString());
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
        StringBuilder parents = new StringBuilder();

        for (int i = 0; i < possibleFolderNames.length - 1; i++) {
            String possibleFolderName = possibleFolderNames[i];
            // Add parents prefix:
            // ex: possibleFolderName => big
            // But we need to check for trees.big ( trees. is parents prefix )
            SchematicFolder possibleParent = getFolderByName(parents + possibleFolderName);

            if (possibleParent == null) {
                // Register parent folder
                possibleParent = new SchematicFolder(possibleFolderName);
                parent.addSchematic(possibleParent);

                foldersByName.put(possibleParent.getPath(), possibleParent);
            }

            parent = possibleParent;

            parents.append(possibleFolderName);
            parents.append(configuration.getSeparator());
        }

        parent.addSchematic(folder);

        foldersByName.put(folder.getPath(), folder);

        folder.getAsFile().mkdirs();

        return folder;
    }

    private static void unloadFolderRecursive(SchematicFolder folder) {

        // Delete every child
        for (ASchematic child : folder.getChildren()) {
            if (child instanceof SchematicFolder) {
                unloadFolderRecursive((SchematicFolder) child);
            }
        }
        unloadFolder(folder);
    }

    private static void unloadFolder(SchematicFolder folder) {
        foldersByName.remove(folder.getPath());
    }

    public static void trimSelection(SchematicsPlayer player) {

        Vector pos1 = player.getPos()[0];
        Vector pos2 = player.getPos()[1];

        Vector minPoint = Vector.getMinimum(pos1, pos2);
        Vector maxPoint = Vector.getMaximum(pos1, pos2);

        World world = player.getPlayer().getWorld();

        Vector trimBottom = getFirstNonEmptyBlock(world, minPoint, maxPoint, true);
        Vector trimTop = getFirstNonEmptyBlock(world, minPoint, maxPoint, false);

        System.out.println("minPoint = " + minPoint);
        System.out.println("trimBottom = " + trimBottom);

        System.out.println("maxPoint = " + maxPoint);
        System.out.println("trimTop = " + trimTop);

    }

    @Deprecated
    private static Vector getFirstNonEmptyBlock(World world, Vector fromCorner, Vector toCorner, boolean min) {
        Block blockAt;

        Vector vector = null;
        int iter = 0;
        try {
            if (min) {


                int maxY = toCorner.getBlockY();
                int startY = fromCorner.getBlockY();

                for (int y = startY; y <= maxY; y++) {
                    int maxX = toCorner.getBlockX();
                    int startX = fromCorner.getBlockX();

                    int maxZ = toCorner.getBlockZ();
                    int startZ = fromCorner.getBlockZ();
                    while (startX <= maxX && startZ <= maxZ) {
                        // Diagonal Iteration
                        // Start from low corner, up to high corner. Return first non-air
                        for (int x = startX; x <= maxX; x++) {

                            blockAt = world.getBlockAt(x, y, startZ);

                            iter++;
                            Material type = blockAt.getType();
                            if (type == Material.AIR) continue;
                            blockAt.setType(Material.GOLD_BLOCK);


                            vector = new Vector(x, y, startZ);
                            break;
                        }

                        for (int z = startZ + 1; z <= maxZ; z++) {
                            blockAt = world.getBlockAt(startX, y, z);

                            iter++;

                            Material type = blockAt.getType();
                            if (type == Material.AIR) continue;
                            blockAt.setType(Material.DIAMOND_BLOCK);


                            Vector newVector = new Vector(startX, y, startZ);
                            vector = vector == null ? newVector : Vector.getMinimum(newVector, vector);
                            break;
                        }

                        if (vector != null) return vector;

                        startZ++;
                        startX++;
                    }

                }
            }
            else {

                int minY = fromCorner.getBlockY();
                int startY = toCorner.getBlockY();

                for (int y = startY; y > minY; y--) {
                    int minX = fromCorner.getBlockX();
                    int startX = toCorner.getBlockX();

                    int minZ = fromCorner.getBlockZ();
                    int startZ = toCorner.getBlockZ();
                    while (startX > minX && startZ > minZ) {
                        // Diagonal Iteration
                        // Start from up corner, down to low corner. Return first non-air
                        for (int x = startX; x > minX; x--) {

                            blockAt = world.getBlockAt(x, y, startZ);

                            iter++;
                            Material type = blockAt.getType();
                            if (type == Material.AIR) continue;
                            blockAt.setType(Material.IRON_BLOCK);


                            vector = new Vector(x, y, startZ);
                            break;
                        }

                        for (int z = startZ - 1; z > minZ; z--) {
                            blockAt = world.getBlockAt(startX, y, z);

                            iter++;

                            Material type = blockAt.getType();
                            if (type == Material.AIR) continue;
                            blockAt.setType(Material.REDSTONE_BLOCK);


                            Vector newVector = new Vector(startX, y, startZ);
                            vector = vector == null ? newVector : Vector.getMinimum(newVector, vector);
                            break;
                        }

                        if (vector != null) return vector;

                        startZ--;
                        startX--;
                    }

                }
            }
        } finally {
            System.out.println("iter = " + iter);
        }

        return fromCorner;
    }
}
