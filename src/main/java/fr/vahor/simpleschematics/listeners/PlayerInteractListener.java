package fr.vahor.simpleschematics.listeners;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.util.TargetBlock;
import fr.vahor.simpleschematics.API;
import fr.vahor.simpleschematics.i18n.Message;
import fr.vahor.simpleschematics.inventory.SchematicInventory;
import fr.vahor.simpleschematics.permissions.Permissions;
import fr.vahor.simpleschematics.schematics.SchematicsPlayer;
import fr.vahor.simpleschematics.schematics.data.SchematicWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        boolean hasToolInHand = player.getInventory().getItemInHand().getType() == API.getToolIcon();

        if (event.getClickedBlock() != null) {

            System.out.println("hasToolInHand = " + event.getClickedBlock().getTypeId());
            System.out.println("event = " + event.getClickedBlock().getData());
// todo remove debug
        }
        if (!hasToolInHand || !Permissions.defaultPermission.test(player)) {
            return;
        }

        event.setCancelled(true);

        SchematicsPlayer schematicsPlayer = API.getOrAddPlayer(player.getUniqueId());

        boolean isRightClick = (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK));
        boolean isLeftClick = !isRightClick && (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK));

        if (schematicsPlayer.isInSelectionMode()) {
            Location location = event.getClickedBlock().getLocation();
            Vector blockVector = new Vector(location.getX(), location.getY(), location.getZ());
            schematicsPlayer.setPosition(
                    schematicsPlayer.getNextPositionIndex(),
                    blockVector,
                    true);
        }
        else {
            if (isLeftClick) {
                player.openInventory(new SchematicInventory(player).getInventory());
            }

            else if (isRightClick) {
                SchematicWrapper schematic = schematicsPlayer.getRandomSchematic();
                if (schematic == null) { // empty list
                    return;
                }

                Vector targetBlock = new TargetBlock(schematicsPlayer.getFawePlayer().getPlayer(), 100, 0.2).getTargetBlock();

                if (targetBlock == null) {
                    player.sendMessage(Message.PREFIX + "todo pas ce cible");
                    return;
                }

                API.pasteSchematic(
                        schematic.getClipboard(),
                        schematicsPlayer,
                        targetBlock
                );
                player.sendMessage(targetBlock.toString());

            }
        }

    }
}
