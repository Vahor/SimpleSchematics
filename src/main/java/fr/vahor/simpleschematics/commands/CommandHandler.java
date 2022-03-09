package fr.vahor.simpleschematics.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import fr.vahor.simpleschematics.API;
import fr.vahor.simpleschematics.SimpleSchematics;
import fr.vahor.simpleschematics.exceptions.FolderNotFoundException;
import fr.vahor.simpleschematics.exceptions.InvalidFolderNameException;
import fr.vahor.simpleschematics.exceptions.InvalidSchematicNameException;
import fr.vahor.simpleschematics.i18n.Message;
import fr.vahor.simpleschematics.permissions.Permissions;
import fr.vahor.simpleschematics.schematics.SchematicsPlayer;
import fr.vahor.simpleschematics.schematics.data.ASchematic;
import fr.vahor.simpleschematics.schematics.data.SchematicFolder;
import fr.vahor.simpleschematics.schematics.data.SchematicWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        final Player player = (Player) sender;
        if (!Permissions.defaultPermission.test(player)) {
            return false;
        }

        SchematicsPlayer schematicsPlayer = API.getOrAddPlayer(player.getUniqueId());

        if (args.length == 0) {
            // Default command, gives tool
            schematicsPlayer.giveTool();
            return true;
        }

        if (args.length == 1) {

            // Open menu
            if (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("m")) {
                schematicsPlayer.openMenu();
                return true;
            }

            // Open menu
            if (args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("t")) {
                schematicsPlayer.toggleSelectionMode();
                player.sendMessage("todo toggle : " + schematicsPlayer.isInSelectionMode());
                return true;
            }

            // pos1 2 3
            if (args[0].equalsIgnoreCase("pos1")) {
                schematicsPlayer.setPosition(0, schematicsPlayer.getCurrentPosition(), true);
                return true;
            }
            if (args[0].equalsIgnoreCase("pos2")) {
                schematicsPlayer.setPosition(1, schematicsPlayer.getCurrentPosition(), true);
                return true;
            }
            if (args[0].equalsIgnoreCase("pos3")) {
                schematicsPlayer.setPosition(2, schematicsPlayer.getCurrentPosition(), true);
                return true;
            }


            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                SimpleSchematics.getInstance().reload();
                player.sendMessage("todo rl");
                return true;
            }

            // Help info for commands
            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
                player.sendMessage("todo Help level 0");
                return true;
            }


            if (args[0].equalsIgnoreCase("schematic") || args[0].equalsIgnoreCase("s")) {
                player.sendMessage("todo Help schematic");
                return true;
            }

            if (args[0].equalsIgnoreCase("folder") || args[0].equalsIgnoreCase("f")) {
                player.sendMessage("todo Help folder");
                return true;
            }


            player.sendMessage("todo type /s help pour voir la liste des commandes");
            return true;

        }

        else {
            // /s folder|f
            // /s schematics|s

            System.out.println("args = " + Arrays.toString(args) + " // " + args.length);
            try {
                String commandName = args[1];

                // Manage whole folder
                if (args[0].equalsIgnoreCase("folder") || args[0].equalsIgnoreCase("f")) {
                    if (args.length == 3) {
                        String folderNameWithSeparator = args[2];

                        // Create folder, if already exists does nothing
                        if (commandName.equalsIgnoreCase("create") || commandName.equalsIgnoreCase("c")) {
                            API.getOrCreateFolder(folderNameWithSeparator);
                            return true;
                        }

                        else if (commandName.equalsIgnoreCase("delete") || commandName.equalsIgnoreCase("d")) {
                            SchematicFolder folder = API.getFolderByName(folderNameWithSeparator);
                            API.deleteFolder(folder);
                            return true;
                        }
                        else if (commandName.equalsIgnoreCase("move") || commandName.equalsIgnoreCase("mv")) {

                        }
                        else if (commandName.equalsIgnoreCase("thumbnail") || commandName.equalsIgnoreCase("t")) {
                            generateThumbnailForFolder(schematicsPlayer, folderNameWithSeparator);
                            return true;
                        }

                    }
                    else if (args.length == 4) {
                        if (commandName.equalsIgnoreCase("move") || commandName.equalsIgnoreCase("mv")) {
                            API.moveFolder("trees.big", "trees.test.truc.b");
                            return true;
                        }
                    }

                    player.sendMessage("todo type /s folder pour voir la liste des commandes");
                    return true;
                }

                else if (args[0].equalsIgnoreCase("schematic") || args[0].equalsIgnoreCase("s")) {
                    if (args.length >= 4) {

                        String folder = args[2];
                        String schematicName = args[3];

                        if (args.length == 4) {
                            if (commandName.equalsIgnoreCase("create") || commandName.equalsIgnoreCase("c")) {
                                createSchematic(schematicsPlayer, folder, schematicName);
                                return true;
                            }
                            else if (commandName.equalsIgnoreCase("delete") || commandName.equalsIgnoreCase("d")) {

                            }
                            else if (commandName.equalsIgnoreCase("move") || commandName.equalsIgnoreCase("mv")) {

                            }
                            else if (commandName.equalsIgnoreCase("thumbnail") || commandName.equalsIgnoreCase("t")) {

                            }
                        }
                        else if (args.length == 5 || args.length == 6) {
                            String targetFolder = args[4];
                            String targetSchematicName = args.length == 5 ? schematicName : args[5];

                            // s s move folder schematicName targetFolder [targetSchematicName]

                        }
                    }

                    player.sendMessage("todo type /s schematic pour voir la liste des commandes");
                    return true;
                }

                player.sendMessage("help level 1");

            } catch (InvalidSchematicNameException e) {
                player.sendMessage("todo Nom de schematic invalide");
                e.printStackTrace();
            }  catch (InvalidFolderNameException e) {
                player.sendMessage("todo Nom de dossier invalide");
                e.printStackTrace();
            } catch (FolderNotFoundException e) {
                player.sendMessage("todo Ce dossier n'existe pas");
                e.printStackTrace();
            } catch (IOException e) {
                player.sendMessage("todo Erreur lors de l'écriture");
                e.printStackTrace();
            } catch (Exception e) {
                player.sendMessage("todo Erreur lors de execution");
                e.printStackTrace();
            }

        }

        return false;
    }

    private void generateThumbnailForFolder(SchematicsPlayer player, String folderNameWithSeparator) {
        SchematicFolder folder = API.getFolderByName(folderNameWithSeparator);
        if (folder == null) {
            player.getPlayer().sendMessage(Message.PREFIX + Message.FOLDER_DONT_EXIST.toString());
            return;
        }

        for (ASchematic child : folder.getChildren()) {
            try {
                if (child instanceof SchematicWrapper) {
                    ((SchematicWrapper) child).loadSchematic(player.getFawePlayer().getWorld().getWorldData(), false);
                    API.generateThumbnail((SchematicWrapper) child);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        player.getPlayer().sendMessage(Message.PREFIX + "todo generateThumbnailForFolder");
    }

    private void createSchematic(SchematicsPlayer player, String folderNameWithSeparator, String schematicName) throws InvalidFolderNameException, IOException, InvalidSchematicNameException {
        if (player.isSelectionInvalid()) {
            player.getPlayer().sendMessage(Message.PREFIX + Message.EMPTY_SELECTION.toString());
            return;
        }
        // todo tester si le schematic existe déjà, si c'est le cas, on fait une erreur lui disant de supprimer ou déplacer l'ancien

        SchematicFolder folderByName = API.getFolderByName(folderNameWithSeparator);
        if (folderByName != null) {
            if (folderByName.hasChildrenWithName(schematicName)) {
                player.getPlayer().sendMessage(Message.PREFIX + Message.SCHEMATIC_ALREADY_EXIST.toString());
                return;
            }
        }

        CuboidRegion region = new CuboidRegion(player.getFawePlayer().getWorld(), player.getPos()[0], player.getPos()[1]);
        try {
            BlockArrayClipboard clipboard = new BlockArrayClipboard(region, player.getUuid());
            clipboard.setOrigin(player.getPos()[2]);

            EditSession editSession = player.getFawePlayer().getNewEditSession();
            editSession.setFastMode(true);
            editSession.setBlockChangeLimit(-1);
            ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());

            Operations.completeLegacy(copy);
            SchematicFolder folder = API.getOrCreateFolder(folderNameWithSeparator);

            API.addNewSchematic(clipboard, folder, schematicName);

            player.getPlayer().sendMessage(Message.PREFIX + "todo created");
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }

    }
}
