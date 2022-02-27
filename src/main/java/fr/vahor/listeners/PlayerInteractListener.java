package fr.vahor.listeners;

import fr.vahor.API;
import fr.vahor.inventory.SchematicInventory;
import fr.vahor.permissions.Permissions;
import fr.vahor.schematics.SchematicsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        final Player player = event.getPlayer();
        boolean hasToolInHand = player.getInventory().getItemInHand().getType() == API.getToolIcon();

        if(!hasToolInHand || !Permissions.defaultPermission.test(player)){
            return;
        }

        event.setCancelled(true);
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            player.openInventory(new SchematicInventory(player).getInventory());
        }else {
            SchematicsPlayer schematicsPlayer = API.getOrAddPlayer(player.getUniqueId());
        }


    }
}
