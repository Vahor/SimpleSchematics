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

package fr.vahor.simpleschematics.listeners;

import fr.vahor.simpleschematics.inventory.InventoryBuilder;
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
