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

import fr.vahor.simpleschematics.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class CommandHandlerTabCompleter implements TabCompleter {

    private final List<String> complete0 = Arrays.asList("help", "menu", "toggle", "schematic", "folder", "pos1", "pos2", "pos3", "reload");
    private final List<String> complete1Folder = Arrays.asList("create", "delete", "move", "thumbnail");
    private final List<String> complete1Schematic = Arrays.asList("create", "delete", "move", "thumbnail");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
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
        else if(args.length == 3) {
            return filteredComplete(API.getFoldersNames(), lastArg);
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
