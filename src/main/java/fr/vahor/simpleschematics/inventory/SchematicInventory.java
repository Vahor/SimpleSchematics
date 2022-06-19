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
import fr.vahor.simpleschematics.schematics.data.ASchematic;
import fr.vahor.simpleschematics.schematics.data.SchematicFolder;
import fr.vahor.simpleschematics.schematics.data.SchematicWrapper;
import fr.vahor.simpleschematics.schematics.data.Thumbnail;
import fr.vahor.simpleschematics.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchematicInventory extends ASchematicInventory {

    private SchematicFolder currentFolder;

    public SchematicInventory(Player player) {
        this(player, API.getRootSchematicFolder());
    }

    public SchematicInventory(Player player, SchematicFolder currentFolder) {
        super(player, Bukkit.createInventory(player, 54, Message.INVENTORY_TITLE.toString()));

        this.currentFolder = currentFolder;
        build();
    }

    @Override
    protected void build() {
        super.build();
        addSelectAll();
        addRandomRotationButton();
        addShowSelectedButton();
    }

    private void addShowSelectedButton() {
        setItem(53,
                new ItemBuilder(Material.CHEST)
                        .setName(Message.INVENTORY_SELECTED_INVENTORY_NAME.toString())
                        .setLore(Message.INVENTORY_SELECTED_INVENTORY_LORE.toString().split("\n"))
                        .build(),
                (event) -> {
                    event.setCancelled(true);
                    player.closeInventory();
                    player.openInventory(new SelectedSchematicInventory(player, currentFolder).getInventory());
                });
    }

    private void setCurrentFolder(SchematicFolder folder) {
        currentFolder = folder;
    }

    @Override
    protected void addSchematics() {
        // Clear area
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 9; column++) {
                setItem(row * 9 + column, null, null);
            }
        }


        // List items
        int slot = 0;
        List<ASchematic> schematicList = currentFolder.getChildren();
        setTotalItems(schematicList.size());
        schematicList.sort(ASchematic::compareTo);

        schematicList = schematicList.subList(getCurrentPage() * getItemsPerPage(),
                Math.min(schematicList.size(), (getCurrentPage() + 1) * getItemsPerPage()));

        for (ASchematic child : schematicList) {

            if (child instanceof SchematicFolder) {
                SchematicFolder folder = ((SchematicFolder) child);
                Material material = folder.getMaterial();
                if (material == null)
                    material = API.getConfiguration().getDefaultFolderMaterial();

                setItem(slot++,
                        new ItemBuilder(material)
                                .setData(folder.getMaterialData()) // Default to 0
                                .setName(Message.INVENTORY_FOLDER_NAME.toString().replace("{name}", child.getName()))
                                .setLore(Message.INVENTORY_FOLDER_LORE.toString().split("\n"))
                                .build(),
                        (event) -> {
                            event.setCancelled(true);
                            setCurrentFolder((SchematicFolder) child);
                            setCurrentPage(0);
                        });
            }
            else {
                addSchematicIcon((SchematicWrapper) child, slot++);
            }
        }

        addGoBackButton();
        addPagination();
    }

    private void addSelectAll() {
        setItem(51,
                new ItemBuilder(Material.BARRIER)
                        .setName(Message.INVENTORY_SELECT_ALL_NAME.toString())
                        .setLore(Message.INVENTORY_SELECT_ALL_LORE.toString().split("\n"))
                        .build(),
                (event) -> {
                    event.setCancelled(true);
                    // Select all
                    if (event.isLeftClick()) {
                        try {
                            for (ASchematic child : currentFolder.getChildren()) {
                                if (child instanceof SchematicWrapper) {
                                    schematicsPlayer.selectSchematic((SchematicWrapper) child);
                                }
                            }
                        } catch (IOException e) {
                            player.sendMessage(Message.PREFIX + "todo erreur chargements");
                            e.printStackTrace();
                        }
                    }
                    else if (event.isRightClick()) {
                        // Deselect all
                        for (ASchematic child : currentFolder.getChildren()) {
                            if (child instanceof SchematicWrapper) {
                                schematicsPlayer.deselectSchematic((SchematicWrapper) child);
                            }
                        }
                    }
                    addSchematics();

                });
    }

    private void addGoBackButton() {
        if (currentFolder.getParent() != null) {
            setItem(49,
                    new ItemBuilder(Material.ARROW)
                            .setName(Message.INVENTORY_GO_BACK_NAME.toString())
                            .setLore(Message.INVENTORY_GO_BACK_LORE.toString().split("\n"))
                            .build(),
                    (event) -> {
                        event.setCancelled(true);
                        currentFolder = currentFolder.getParent();
                        setCurrentPage(0);
                    });
        }
        else {
            setItem(49, null, null);
        }
    }

    private void addRandomRotationButton() {
        setItem(47,
                new ItemBuilder(Material.COMPASS)
                        .setName(Message.INVENTORY_ROTATION_NAME.toString())
                        .setLore(Message.INVENTORY_ROTATION_LORE.toString()
                                .replace("{current}", schematicsPlayer.getRotationMode().name())
                                .split("\n"))
                        .build(),
                (event) -> {
                    event.setCancelled(true);
                    schematicsPlayer.setRotationMode(schematicsPlayer.getRotationMode().next());
                    addRandomRotationButton();
                    player.sendMessage(Message.PREFIX + schematicsPlayer.getRotationMode().name());
                });
    }

    public void addSchematicIcon(final SchematicWrapper schematic, final int slot) {
        boolean enabled = schematicsPlayer.isSchematicEnabled(schematic);
        List<String> lore = new ArrayList<>(Arrays.asList(
                Message.INVENTORY_SCHEMATIC_LORE.toString()
                        .replace("{path}", schematic.getParent().getPath())
                        .split("\n")));
        if (currentFolder.isGenerateThumbnail()) {
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
                final SchematicFolder folderWhenStarted = currentFolder;
                schematic.loadThumbnail(false).thenRun(() -> {
                    if (currentFolder == folderWhenStarted) // If still in the same folder
                        addSchematicIcon(schematic, slot);
                });
                lore.add(Message.LOADING_THUMBNAIL_LORE.toString());
            }
        }

        setItem(slot,
                new ItemBuilder(enabled ? Material.MAP : Material.PAPER)
                        .setEnchanted(enabled)
                        .setName(Message.INVENTORY_SCHEMATIC_NAME.toString().replace("{name}", schematic.getName()))
                        .setLore(lore)
                        .build(),
                (event) -> {
                    event.setCancelled(true);
                    try {
                        boolean added = schematicsPlayer.toggleSchematic(schematic);
                        if (added) {
                            player.sendMessage(Message.PREFIX + Message.SCHEMATIC_ENABLED.toString().replace("{name}", schematic.getName()));
                        }
                        else {
                            player.sendMessage(Message.PREFIX + Message.SCHEMATIC_DISABLED.toString().replace("{name}", schematic.getName()));
                        }
                        addSchematicIcon(schematic, slot);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
