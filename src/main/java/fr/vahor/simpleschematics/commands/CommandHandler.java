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
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        final Player player = (Player) sender;
        if (!Permissions.defaultPermission.test(player)) {
            String message = Message.NO_PERMISSION.toString();
            if (message.length() > 0) {
                player.sendMessage(Message.PREFIX + Message.NO_PERMISSION.toString());
            }
            return false;
        }

        SchematicsPlayer schematicsPlayer = API.getOrAddPlayer(player.getUniqueId());

        if (args.length == 0) {
            // Default command, gives tool
            schematicsPlayer.giveTool();
            player.sendMessage(Message.PREFIX + Message.COMMAND_TOOL.toString());
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
                player.sendMessage(Message.PREFIX + Message.COMMAND_TOGGLE.toString().replace("{enabled}", schematicsPlayer.isInSelectionMode() ? "enabled" : "disabled"));
                return true;
            }

            // pos1 2 3
            if (args[0].equalsIgnoreCase("pos1")) {
                schematicsPlayer.setPosition(0, schematicsPlayer.getCurrentPosition(), true);
                schematicsPlayer.setPosIndex(1);
                return true;
            }
            if (args[0].equalsIgnoreCase("pos2")) {
                schematicsPlayer.setPosition(1, schematicsPlayer.getCurrentPosition(), true);
                schematicsPlayer.setPosIndex(2);
                return true;
            }
            if (args[0].equalsIgnoreCase("pos3")) {
                schematicsPlayer.setPosition(2, schematicsPlayer.getCurrentPosition(), true);
                schematicsPlayer.setPosIndex(0);
                return true;
            }

            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                SimpleSchematics.getInstance().reload();
                player.getPlayer().sendMessage(Message.PREFIX + Message.COMMAND_RELOADED.toString());
                return true;
            }

            // Help info for commands
            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
                player.getPlayer().sendMessage(Message.GLOBAL_HELP.toString());
                return true;
            }


            if (args[0].equalsIgnoreCase("schematic") || args[0].equalsIgnoreCase("s")) {
                player.getPlayer().sendMessage(Message.SCHEMATIC_HELP.toString());
                return true;
            }

            if (args[0].equalsIgnoreCase("folder") || args[0].equalsIgnoreCase("f")) {
                player.getPlayer().sendMessage(Message.FOLDER_HELP.toString());
                return true;
            }


            player.getPlayer().sendMessage(Message.PREFIX + Message.UNKNOWN_COMMAND.toString());
            return true;

        }

        else {
            // /s folder|f
            // /s schematics|s

            try {
                String commandName = args[1];

                // Manage whole folder
                if (args[0].equalsIgnoreCase("folder") || args[0].equalsIgnoreCase("f")) {
                    if (args.length == 3) {
                        String folderNameWithSeparator = args[2];

                        if (commandName.equalsIgnoreCase("icon") || commandName.equalsIgnoreCase("i")) {
                            updateFolderIcon(schematicsPlayer, folderNameWithSeparator);
                            return true;
                        }

                    }
                    else {

                        String folderNameWithSeparator = args[3];
                        if (commandName.equalsIgnoreCase("thumbnail") || commandName.equalsIgnoreCase("t")) {
                            if (args[2].equalsIgnoreCase("generate")) {
                                boolean recursive = args.length > 4 && args[4].equalsIgnoreCase("-r");
                                generateThumbnailForFolder(schematicsPlayer, folderNameWithSeparator, recursive);
                                return true;
                            }
                            if (args[2].equalsIgnoreCase("toggle")) {
                                toggleThumbnailForFolder(schematicsPlayer, folderNameWithSeparator);
                                return true;
                            }
                        }
                    }

                    player.getPlayer().sendMessage(Message.FOLDER_HELP.toString());
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
                        }
                    }

                    player.getPlayer().sendMessage(Message.SCHEMATIC_HELP.toString());
                    return true;

                }


                player.getPlayer().sendMessage(Message.PREFIX + Message.UNKNOWN_COMMAND.toString());
                return true;


            } catch (InvalidSchematicNameException e) {
                player.getPlayer().sendMessage(Message.PREFIX + Message.INVALID_SCHEMATIC_NAME.toString());
                e.printStackTrace();
            } catch (FolderNotFoundException e) {
                player.getPlayer().sendMessage(Message.PREFIX + Message.FOLDER_DONT_EXIST.toString());
                e.printStackTrace();
            } catch (InvalidFolderNameException e) {
                player.getPlayer().sendMessage(Message.PREFIX + Message.INVALID_FOLDER_NAME.toString());
                e.printStackTrace();
            } catch (NullPointerException e) {
                player.getPlayer().sendMessage(Message.PREFIX + Message.INVALID_SELECTION.toString());
                e.printStackTrace();
            } catch (Exception e) {
                player.getPlayer().sendMessage(Message.PREFIX + Message.UNKNOWN_ERROR.toString());
                e.printStackTrace();
            }

        }

        return false;
    }

    private void generateThumbnailForFolder(SchematicsPlayer player, String folderNameWithSeparator, boolean recursive) throws FolderNotFoundException {
        SchematicFolder folder = API.getFolderByName(folderNameWithSeparator);
        if (folder == null) {
            throw new FolderNotFoundException();
        }

        generateThumbnailForFolder(player, folder, recursive);
        player.getPlayer().sendMessage(Message.PREFIX + Message.COMMAND_THUMBNAIL_GENERATE.toString()
                .replace("{folder}", folderNameWithSeparator)
                .replace("{recursive}", recursive ? "true" : "false")
        );
    }

    public void toggleThumbnailForFolder(SchematicsPlayer player, String folderNameWithSeparator) throws FolderNotFoundException, IOException {
        SchematicFolder folder = API.getFolderByName(folderNameWithSeparator);
        if (folder == null) {
            throw new FolderNotFoundException();
        }

        folder.setGenerateThumbnail(!folder.isGenerateThumbnail());
        folder.saveConfiguration();
        player.getPlayer().sendMessage(Message.PREFIX + Message.COMMAND_THUMBNAIL_TOGGLE.toString()
                .replace("{folder}", folderNameWithSeparator)
                .replace("{enabled}", folder.isGenerateThumbnail() ? "true" : "false")
        );
    }

    private void generateThumbnailForFolder(SchematicsPlayer player, SchematicFolder folder, boolean recursive) {

        player.sendActionBar(Message.COMMAND_THUMBNAIL_GENERATE_ACTION_BAR.toString().replace("{folder}", folder.getName()));

        for (ASchematic child : folder.getChildren()) {
            try {
                if (child instanceof SchematicWrapper) {
                    ((SchematicWrapper) child).loadSchematic(player.getFawePlayer().getWorld().getWorldData(), false);
                    API.generateThumbnail((SchematicWrapper) child);
                }
                else if (recursive && child instanceof SchematicFolder) {
                    generateThumbnailForFolder(player, (SchematicFolder) child, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateFolderIcon(SchematicsPlayer player, String folderNameWithSeparator) throws IOException, FolderNotFoundException {
        SchematicFolder folder = API.getFolderByName(folderNameWithSeparator);
        if (folder == null) {
            throw new FolderNotFoundException();
        }
        ItemStack itemInHand = player.getPlayer().getItemInHand();
        Material material = itemInHand.getType();
        if (material == null || material == Material.AIR) {
            player.getPlayer().sendMessage(Message.PREFIX + Message.INVALID_MATERIAL.toString());
            return;
        }

        folder.setMaterialData(itemInHand.getData().getData());
        folder.setMaterial(material);
        folder.saveConfiguration();

        player.getPlayer().sendMessage(Message.PREFIX + Message.COMMAND_FOLDER_UPDATE_ICON.toString()
                .replace("{folder}", folderNameWithSeparator)
                .replace("{material}", material.name())
                .replace("{data}", itemInHand.getData().getData() + ""));
    }

    private void createSchematic(SchematicsPlayer player, String folderNameWithSeparator, String schematicName) throws InvalidFolderNameException,
            IOException, InvalidSchematicNameException {
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

            player.getPlayer().sendMessage(Message.PREFIX + Message.COMMAND_SCHEMATIC_CREATE.toString()
                    .replace("{folder}", folderNameWithSeparator)
                    .replace("{schematic}", schematicName));

        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }

    }
}
