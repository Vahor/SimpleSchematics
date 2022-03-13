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
import fr.vahor.simpleschematics.schematics.data.ASchematic;
import fr.vahor.simpleschematics.schematics.data.SchematicFolder;
import fr.vahor.simpleschematics.schematics.data.SchematicWrapper;
import fr.vahor.simpleschematics.schematics.data.Thumbnail;
import fr.vahor.simpleschematics.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectedSchematicInventory extends InventoryBuilder {

    private final SchematicsPlayer schematicsPlayer;
    private final SchematicFolder previousFolder;

    public SelectedSchematicInventory(Player player, SchematicFolder previousFolder) {
        super(player, Bukkit.createInventory(player, 54, Message.SELECTED_INVENTORY_TITLE.toString()));

        schematicsPlayer    = API.getOrAddPlayer(player.getUniqueId());
        this.previousFolder = previousFolder;

        setItemsPerPage(36);

        build();
    }

    @Override
    public void onPageChanged() {
        listSchematics();
    }

    public void build() {
        listSchematics();
        addGoBackButton();
        addGlass();
    }

    public void listSchematics() {
        // Clear area
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 9; column++) {
                setItem(row * 9 + column, null, null);
            }
        }


        // List items
        int slot = 0;
        List<SchematicWrapper> schematicList = schematicsPlayer.getSelectedSchematics();
        schematicList.sort(ASchematic::compareTo);
        setTotalItems(schematicList.size());

        schematicList = schematicList.subList(getCurrentPage() * getItemsPerPage(),
                Math.min(schematicList.size(), (getCurrentPage() + 1) * getItemsPerPage()));

        for (SchematicWrapper child : schematicList) {
            addSchematicIcon(child, slot++);
        }

        addPagination();
    }

    private void addPagination() {

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

    public void addGoBackButton() {
        setItem(49,
                new ItemBuilder(Material.ARROW)
                        .setName(Message.INVENTORY_GO_BACK_NAME.toString())
                        .setLore(Message.INVENTORY_GO_BACK_LORE.toString().split("\n"))
                        .build(),
                (event) -> {
                    event.setCancelled(true);

                    player.closeInventory();
                    player.openInventory(new SchematicInventory(player, previousFolder).getInventory());
                });
    }

    public void addSchematicIcon(final SchematicWrapper schematic, final int slot) {
        List<String> lore = new ArrayList<>(Arrays.asList(
                Message.INVENTORY_SCHEMATIC_LORE.toString()
                        .replace("{path}", schematic.getParent().getPath())
                        .split("\n")));
        Thumbnail thumbnail = schematic.getThumbnail();
        if (thumbnail != null) {
            if (thumbnail.getCachedList().isEmpty()) {
                lore.add(Message.THUMBNAIL_NOT_FOUND_LORE.toString());
            }
            else {
                lore.addAll(thumbnail.getCachedList());
            }
        }
        else {
            lore.add(Message.LOADING_THUMBNAIL_LORE.toString());
        }

        setItem(slot,
                new ItemBuilder(Material.MAP)
                        .setEnchanted(true)
                        .setName(Message.INVENTORY_SCHEMATIC_NAME.toString().replace("{name}", schematic.getName()))
                        .setLore(lore)
                        .build(),
                (event) -> {
                    event.setCancelled(true);
                    schematicsPlayer.deselectSchematic(schematic);
                    player.sendMessage(Message.PREFIX + Message.SCHEMATIC_DISABLED.toString().replace("{name}", schematic.getName()));

                    listSchematics();
                });
    }

    private void addGlass() {
        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").build();
        for (int column = 0; column < 9; column++) {
            setItem(36 + column, glass,
                    (event) -> event.setCancelled(true));
        }
    }

}
