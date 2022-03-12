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

package fr.vahor.simpleschematics.permissions;

import org.bukkit.entity.Player;

public class Permissions {

    public static final Permission defaultPermission = (player) -> player.hasPermission("simpleschematic.use");
    public static final Permission canCreateDirectory = (player) -> player.hasPermission("simpleschematic.docs.create");
    public static final Permission canDeleteDirectory = (player) -> player.hasPermission("simpleschematic.docs.delete");
    public static final FolderPermission canCreateSchematicInFolder = (player, folder) -> player.hasPermission("simpleschematic." + folder + ".create");
    public static final FolderPermission canDeleteSchematicInFolder = (player, folder) -> player.hasPermission("simpleschematic." + folder + ".delete");
    public static final FolderPermission canUseSchematicInFolder = (player, folder) -> player.hasPermission("simpleschematic." + folder + ".use");


    public interface Permission {
        boolean test(Player player);
    }

    public interface FolderPermission {
        boolean test(Player player, String folderName);
    }
}
