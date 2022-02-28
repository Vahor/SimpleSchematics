package fr.vahor.inventory;

import fr.vahor.API;
import fr.vahor.i18n.Message;
import fr.vahor.schematics.data.ASchematic;
import fr.vahor.schematics.data.SchematicFolder;
import fr.vahor.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SchematicInventory extends InventoryBuilder {

    private SchematicFolder currentFolder;

    public SchematicInventory(Player player) {
        super(player, Bukkit.createInventory(player, 54, Message.INVENTORY_TITLE.toString()));
        this.currentFolder = API.getRootSchematicFolder();

        build();
    }

    public void build() {
        addGlass();
        listSchematics();
    }

    public void listSchematics() {
        // Clear area
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 9; column++) {
                inventory.setItem(row * 9 + column, null);
            }
        }

        // List items
        int slot = 0;
        for (ASchematic child : currentFolder.getChildren()) {

            if(child instanceof SchematicFolder) {
                setItem(slot++,
                        new ItemBuilder(Material.BOOK)
                                .setName(Message.INVENTORY_FOLDER_NAME.toString().replace("{name}", child.getName()))
                                .setLore(Message.INVENTORY_FOLDER_LORE.toString().split("\n"))
                                .build(),
                        (event) -> {
                            event.setCancelled(true);
                            currentFolder = (SchematicFolder) child;
                            listSchematics();
                        });
            }else {
                setItem(slot++,
                        new ItemBuilder(Material.PAPER)
                                .setName(Message.INVENTORY_SCHEMATIC_NAME.toString().replace("{name}", child.getName()))
                                .setLore(Message.INVENTORY_SCHEMATIC_LORE.toString().split("\n"))
                                .build(),
                        (event) -> {
                            event.setCancelled(true);
                            player.sendMessage(child.getName());
                        });
            }
        }

        addGoBackButton();
        addPagination();
    }

    public void addPagination(){

    }

    public void addGoBackButton() {
        if(currentFolder.getParent() != null){
            setItem(49,
                    new ItemBuilder(Material.ARROW)
                            .setName(Message.INVENTORY_GO_BACK.toString())
                            .build(),
                    (event) -> {
                        event.setCancelled(true);
                        currentFolder = currentFolder.getParent();
                        listSchematics();
                    });
        }else {
            setItem(49, null, null);
        }
    }

    private void addGlass() {
        ItemStack glass = new ItemBuilder(Material.valueOf("STAINED_GLASS_PANE")).setName(" ").build();
        for (int column = 0; column < 9; column++) {
            inventory.setItem(36 + column, glass);
        }
    }
}
