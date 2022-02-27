package fr.vahor.schematics;

import fr.vahor.inventory.SchematicInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SchematicsPlayer {

    private final UUID uuid;
    Location[] pos = new Location[3];
    int posIndex = 0;

    public SchematicsPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void openMenu(){
        Player player = Bukkit.getPlayer(uuid);
        player.openInventory(new SchematicInventory(player).getInventory());
    }
}
