package fr.vahor.commands;

import fr.vahor.API;
import fr.vahor.exceptions.FolderNotFoundException;
import fr.vahor.i18n.Message;
import fr.vahor.permissions.Permissions;
import fr.vahor.schematics.SchematicsPlayer;
import fr.vahor.utils.ItemBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
            player.getInventory().addItem(
                    new ItemBuilder(API.getToolIcon())
                            .setName("todo")
                            .setLore("todo 2".split("\n"))
                            .build());
        }
        else {

            if (args.length == 1) {

                // Help info for commands
                if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
                    player.sendMessage("todo Help create doc");
                    return true;
                }

                // Open menu
                else if (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("m")) {
                    schematicsPlayer.openMenu();
                    return true;
                }

            }

            // Manage whole folder
            else if (args.length == 2) {
                String folder = args[1];
                boolean alreadyExists = API.getFolderByName(folder) != null;

                if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {

                    if (alreadyExists) {
                        player.sendMessage(Message.PREFIX + Message.FOLDER_ALREADY_EXISTS.toString());
                        return true;
                    }

                    try {
                        API.addNewFolder(folder);
                    } catch (FolderNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("d")) {
                    if (!alreadyExists) {
                        player.sendMessage(Message.PREFIX + Message.FOLDER_DONT_EXIST.toString());
                        return true;
                    }
                }

                else if (args[0].equalsIgnoreCase("move") || args[0].equalsIgnoreCase("mv")) {
                    if (!alreadyExists) {
                        player.sendMessage(Message.PREFIX + Message.FOLDER_DONT_EXIST.toString());
                        return true;
                    }
                }
            }

            // Manage schematic
            else if (args.length == 3) {
                String folder = args[1];
                if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {

                }
                else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("d")) {

                }
                else if (args[0].equalsIgnoreCase("move") || args[0].equalsIgnoreCase("mv")) {

                }
            }

        }

        return false;
    }
}
