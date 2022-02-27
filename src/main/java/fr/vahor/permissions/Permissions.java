package fr.vahor.permissions;

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
