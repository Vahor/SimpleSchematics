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

import fr.vahor.simpleschematics.API;
import fr.vahor.simpleschematics.i18n.Message;
import fr.vahor.simpleschematics.schematics.SchematicsPlayer;
import fr.vahor.simpleschematics.utils.ItemBuilder;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ASchematicInventory extends InventoryBuilder {

    protected final SchematicsPlayer schematicsPlayer;

    public ASchematicInventory(Player player, @NonNull Inventory inventory) {
        super(player, inventory);

        schematicsPlayer = API.getOrAddPlayer(player.getUniqueId());

        setItemsPerPage(36);
    }

    @Override
    public void onPageChanged() {
        addSchematics();
    }

    protected void build() {
        addGlass();
        addSchematics();
    }

    abstract protected void addSchematics();

    protected void addPagination() {

        if (isFirstPage()) {
            setItem(48, null, null);
        }
        else {
            setItem(48,
                    new ItemBuilder(Material.DIRT)
                            .setName(Message.INVENTORY_PREVIOUS_NAME.toString())
                            .setLore(Message.INVENTORY_PREVIOUS_LORE.toString().split("\n"))
                            .build(), event -> {
                        event.setCancelled(true);
                        setCurrentPage(getCurrentPage() - 1);
                    });
        }

        if (isLastPage()) {
            setItem(50, null, null);
        }
        else {
            setItem(50,
                    new ItemBuilder(Material.DIAMOND)
                            .setName(Message.INVENTORY_NEXT_NAME.toString())
                            .setLore(Message.INVENTORY_NEXT_LORE.toString().split("\n"))
                            .build(), event -> {
                        event.setCancelled(true);
                        setCurrentPage(getCurrentPage() + 1);
                    });
        }

    }

    private void addGlass() {
        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").build();
        for (int column = 0; column < 9; column++) {
            setItem(36 + column, glass,
                    (event) -> event.setCancelled(true));
        }
    }
}
