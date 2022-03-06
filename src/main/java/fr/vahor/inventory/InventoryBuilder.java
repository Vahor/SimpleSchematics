package fr.vahor.inventory;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public abstract class InventoryBuilder {

    public static final Map<UUID, Map<Integer, Consumer<InventoryClickEvent>>> inventoryActions = new HashMap<>();

    @Getter  protected final Inventory inventory;
    protected final Player player;

    public InventoryBuilder(Player player, @NonNull  Inventory inventory) {
        this.inventory = inventory;
        this.player    = player;
        inventoryActions.put(player.getUniqueId(), new HashMap<>(inventory.getSize()));
    }

    public void setItem(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        inventoryActions.get(player.getUniqueId()).put(slot, consumer);
        inventory.setItem(slot, itemStack);
    }

}
