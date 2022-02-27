package fr.vahor;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import fr.vahor.exceptions.FolderNotFoundException;
import fr.vahor.exceptions.InvalidFolderNameException;
import fr.vahor.schematics.SchematicsPlayer;
import fr.vahor.schematics.data.ASchematic;
import fr.vahor.schematics.data.Schematic;
import fr.vahor.schematics.data.SchematicFolder;
import fr.vahor.utils.Schema;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class API {

    private static final Map<UUID, SchematicsPlayer> playerMap = new HashMap<>();
    private static final Map<String, SchematicFolder> foldersByName = new HashMap<>();
    @Getter private static SchematicFolder rootSchematicFolder;
    @Getter private static int loadedSchematicCount = 0;
    @Getter private static WorldEditPlugin worldEdit;
    @Getter private static Config configuration;
    @Getter private static Logger logger;

    public static final String SYSTEM_SEPERATOR = System.getProperty("file.separator");

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

    public static void init(){
        try {
            toolIcon = Material.valueOf(configuration.getToolIconMaterial());
        }catch(Exception e){
            toolIcon = Material.values()[1];
            e.printStackTrace();
        }
    }

    public static SchematicsPlayer getOrAddPlayer(UUID uuid) {
        SchematicsPlayer player = playerMap.get(uuid);
        if(player != null) return player;
        return addPlayer(uuid);
    }


    public static SchematicsPlayer addPlayer(UUID uuid){
        if (!playerMap.containsKey(uuid)) {
            return playerMap.put(uuid, new SchematicsPlayer(uuid));
        }
        return null;
    }

    public static void removePlayer(UUID uuid){
        playerMap.remove(uuid);
    }

    public static SchematicFolder getFolderByName(String name){
        return foldersByName.get(name);
    }

    public static void loadWorldEdit(){
        try {
            worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        }catch (ClassCastException e) {
            getLogger().severe("WorldEdit plugin not found.");
            throw e;
        }
    }

    public static void loadSchematics(){
        File directory = new File(configuration.getSchematicsFolderPath());
        if(!directory.exists()){
            getLogger().severe("Schematic folder not found : " + directory.getAbsolutePath());
            return;
        }
        rootSchematicFolder = loadSchematicsInFolder(directory);
        System.out.println("foldersByName = " + foldersByName);
    }

    private static SchematicFolder loadSchematicsInFolder(File directory){
        File[] files = directory.listFiles();
        if(files == null){
            getLogger().severe("INTERNAL - loadSchematicsInFolder files list null");
            return null;
        }

        SchematicFolder schematicFolder = new SchematicFolder(directory.getName());


        for (File file : files) {
            ASchematic schematic;

            if(file.isDirectory())
                schematic = loadSchematicsInFolder(file);
            else
                schematic = registerSchematics(file);

            if(schematic != null){
                schematicFolder.addSchematic(schematic);

            }
        }

        // Using file path as recursion is not finished here
        String pathInSystem = directory.getPath()
                .replaceFirst(configuration.getSchematicsFolderPath(), "")
                .replace(SYSTEM_SEPERATOR, configuration.getSeperator())
                .replaceFirst(configuration.getSeperator(), "");

        if(pathInSystem.isEmpty()) pathInSystem = configuration.getSeperator();

        foldersByName.put(pathInSystem, schematicFolder);

        return schematicFolder;
    }

    private static Schematic registerSchematics(File file){
        if(!file.getName().endsWith(".schematic")){
            return null;
        }

        Schematic schematic = new Schematic(file.getName());
        return schematic;

    }

    public static SchematicFolder addNewFolder(String folderName) throws InvalidFolderNameException {
        if(!Schema.isValidFolderName(folderName)) {
            throw new InvalidFolderNameException();
        }

        // folderName : trees.big
        // addFolder trees and big

        SchematicFolder folder = getFolderByName(folderName);
        // If folder already exists, return
        if(folder != null) {
            return folder;
        }

        String[] possibleFolderNames = configuration.getSeperatorPattern().split(folderName);
        SchematicFolder parent = rootSchematicFolder;
        SchematicFolder schematicFolder = new SchematicFolder(possibleFolderNames[possibleFolderNames.length - 1]);

        for (int i = 0; i < possibleFolderNames.length - 1; i++) {
            SchematicFolder possibleParent = getFolderByName(possibleFolderNames[i]);

            if(possibleParent == null){
                // Register parent folder
                possibleParent = new SchematicFolder(possibleFolderNames[i]);
                parent.addSchematic(possibleParent);

                foldersByName.put(possibleParent.getPath(), possibleParent);

            }

            parent = possibleParent;
        }

        parent.addSchematic(schematicFolder);

        foldersByName.put(schematicFolder.getPath(), schematicFolder);

        schematicFolder.getAsFile().mkdirs();

        return schematicFolder;
    }

    public static void moveFolder(String fromName, String toName) throws FolderNotFoundException {

        // Unload fromName schematics
        // Use file management to move
        // Then load toName schematics with loadSchematicsInFolder

        SchematicFolder fromFolder = getFolderByName(fromName);
        if(fromFolder == null) {
            throw new FolderNotFoundException();
        }

        // Create new folder
        SchematicFolder toFolder = addNewFolder(toName);

        // Unload currentFolder
        unloadFolderRecursive(fromFolder);


        fromFolder.getAsFile().renameTo(toFolder.getAsFile());


    }

    public static void deleteFolder(SchematicFolder folder) throws FolderNotFoundException {
        if(folder == null){
            throw new FolderNotFoundException();
        }
        unloadFolderRecursive(folder);

        System.out.println("file.getAbsolutePath() = " + folder.getAsFile().getAbsolutePath());
//        file.getAbsoluteFile().delete();

    }

    private static void unloadFolderRecursive(@NotNull SchematicFolder folder) {

        // Delete every child
        for (ASchematic child : folder.getChildren()) {
            if(child instanceof SchematicFolder){
                unloadFolderRecursive((SchematicFolder) child);
            }
        }
        unloadFolder(folder);
    }

    private static void unloadFolder(@NotNull SchematicFolder folder){
        foldersByName.remove(folder.getPath());
    }
}
