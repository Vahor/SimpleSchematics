package fr.vahor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandHandlerTabCompleter implements TabCompleter {

    private final List<String> complete0 = Arrays.asList("help", "menu", "toggle", "schematic", "folder", "pos1", "pos2", "pos3", "reload");
    private final List<String> complete1Folder = Arrays.asList("create", "delete", "move", "thumbnail");
    private final List<String> complete1Schematic = Arrays.asList("create", "delete", "move", "thumbnail");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        String lastArg = args[args.length - 1];
        if(args.length == 1){
            return filteredComplete(complete0, lastArg);
        }

        if(args.length == 2) {
            if (args[0].equalsIgnoreCase("folder") || args[0].equalsIgnoreCase("f")) {
                return filteredComplete(complete1Folder, lastArg);
            }
            if (args[0].equalsIgnoreCase("schematic") || args[0].equalsIgnoreCase("s")) {
                return filteredComplete(complete1Schematic, lastArg);
            }
        }

        return Collections.emptyList();
    }

    private List<String> filteredComplete(final List<String> strings, String arg) {
        List<String> result = new ArrayList<>(strings.size());
        for (String string : strings) {
            if(string.contains(arg)) result.add(string);
        }
        return result;
    }
}
