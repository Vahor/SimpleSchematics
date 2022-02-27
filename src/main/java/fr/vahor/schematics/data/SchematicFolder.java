package fr.vahor.schematics.data;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"children"}, callSuper = true)
public class SchematicFolder extends ASchematic {

    private final List<ASchematic> children = new ArrayList<>();

    public SchematicFolder(String name) {
        super(name);
    }

    public void addSchematic(ASchematic schematic){
        schematic.setParent(this);
        children.add(schematic);
    }

    public List<ASchematic> getChildren() {
        return children;
    }
}
