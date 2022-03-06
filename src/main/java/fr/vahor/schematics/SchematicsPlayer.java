package fr.vahor.schematics;

import com.boydti.fawe.object.FawePlayer;
import com.sk89q.worldedit.Vector;
import fr.vahor.API;
import fr.vahor.i18n.Message;
import fr.vahor.inventory.SchematicInventory;
import fr.vahor.schematics.data.SchematicWrapper;
import fr.vahor.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class SchematicsPlayer {

    @Getter private final UUID uuid;

    @Getter Vector[] pos = new Vector[3];
    @Getter @Setter int posIndex = 0;

    @Getter boolean inSelectionMode = false;
    @Getter @Setter RotationMode rotationMode = RotationMode.DEFAULT;

    List<SchematicWrapper> selectedSchematics = new ArrayList<>(4);

    @Getter private final FawePlayer<?> fawePlayer;
    @Getter private final Player player;

    public SchematicsPlayer(UUID uuid) {
        this.uuid = uuid;

        player     = Bukkit.getPlayer(uuid);
        fawePlayer = FawePlayer.wrap(player);
    }

    public void openMenu() {
        Player player = Bukkit.getPlayer(uuid);
        player.openInventory(new SchematicInventory(player).getInventory());
    }

    public void selectSchematic(SchematicWrapper schematic) throws IOException {
        if (!selectedSchematics.contains(schematic)) {
            selectedSchematics.add(schematic);
            schematic.loadSchematic(fawePlayer.getPlayer().getWorld().getWorldData(), false);
        }
    }
    public void deselectSchematic(SchematicWrapper schematic) {
        selectedSchematics.remove(schematic);
    }

    /**
     *
     * @param schematic Schematic to add
     * @return true if added, false otherwise
     * @throws IOException
     */
    public boolean toggleSchematic(SchematicWrapper schematic) throws IOException {
        boolean contains = selectedSchematics.remove(schematic);
        if (!contains)
            selectSchematic(schematic);
        return !contains;
    }

    public boolean isSchematicEnabled(SchematicWrapper schematic) {
        return selectedSchematics.contains(schematic);
    }

    public void toggleSelectionMode() {
        posIndex        = 0;
        inSelectionMode = !inSelectionMode;

        if (!inSelectionMode) {
            // clear
            Arrays.fill(pos, null);
        }
    }

    public boolean isSelectionInvalid() {
        for (Vector po : pos) {
            if (po == null) return true;
        }
        return false;
    }

    public Vector getCurrentPosition() {
        return getFawePlayer().getPlayer().getBlockIn().toBlockVector();
    }

    public SchematicWrapper getRandomSchematic() {
        if (selectedSchematics.size() == 0) return null;
        return selectedSchematics.get(API.RANDOM.nextInt(selectedSchematics.size()));
    }

    public void clearSelected() {
        selectedSchematics.clear();
    }

    /**
     *
     * @param index [0-3]
     * @param location
     */
    public void setPosition(int index, Vector location, boolean withMessage) {
        pos[index] = location;
        if (withMessage) {
            if (index == 2) {
                // copy message
                player.sendMessage("todo copy pos set" + location);
            }
            else {
                player.sendMessage("todo pos" + (index + 1) + " set" + location);
            }
        }
    }

    public int getNextPositionIndex() {
        System.out.println("posIndex  = " + posIndex);
        int current = posIndex;
        posIndex = (posIndex + 1) % 3;
        return current;
    }

    public void giveTool() {
        player.getInventory().addItem(
                new ItemBuilder(API.getToolIcon())
                        .setName(Message.TOOL_NAME.toString())
                        .setLore(Message.TOOL_LORE.toString().split("\n"))
                        .build());
    }

    @Override
    public String toString() {
        return "SchematicsPlayer{" +
                "uuid=" + uuid +
                ", selectedSchematics=" + selectedSchematics +
                ", fawePlayer=" + fawePlayer +
                '}';
    }
}
