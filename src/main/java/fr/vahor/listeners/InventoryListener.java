package fr.vahor.listeners;

import fr.vahor.inventory.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (!InventoryBuilder.inventoryActions.containsKey(player.getUniqueId()))
            return;

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == player.getOpenInventory().getBottomInventory()) {
            event.setCancelled(true);

            return;
        }

        if (clickedInventory == player.getOpenInventory().getTopInventory()) {
            Consumer<InventoryClickEvent> consumer = InventoryBuilder.inventoryActions.get(player.getUniqueId())
                    .get(event.getSlot());
            if(consumer != null)
                    consumer.accept(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();

        if (!InventoryBuilder.inventoryActions.containsKey(player.getUniqueId()))
            return;

        InventoryBuilder.inventoryActions.remove(player.getUniqueId());

    }
}
