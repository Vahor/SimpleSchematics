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

package fr.vahor.simpleschematics.inventory;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class InventoryBuilder {

    public static final Map<UUID, Map<Integer, Consumer<InventoryClickEvent>>> inventoryActions = new HashMap<>();

    @Getter protected final Inventory inventory;
    protected final Player player;

    private int currentPage = 0;
    private int itemsPerPage = 0;
    private int totalItems = 0;

    public InventoryBuilder(Player player, @NonNull Inventory inventory) {
        this.inventory = inventory;
        this.player    = player;
        inventoryActions.put(player.getUniqueId(), new HashMap<>(inventory.getSize()));
    }

    protected void onPageChanged() {}

    protected boolean isLastPage() {
        return (currentPage+1) * itemsPerPage >= totalItems;
    }

    protected int itemsInPage() {
        return totalItems - (currentPage * itemsPerPage);
    }

    protected boolean isFirstPage() {
        return currentPage == 0;
    }

    protected int getCurrentPage(){
        return currentPage;
    }

    protected void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        onPageChanged();
    }

    protected void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    protected void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    protected int getItemsPerPage() {
        return itemsPerPage;
    }

    protected void setItem(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        inventoryActions.get(player.getUniqueId()).put(slot, consumer);
        inventory.setItem(slot, itemStack);
    }

}
