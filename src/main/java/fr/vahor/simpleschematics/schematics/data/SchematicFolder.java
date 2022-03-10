package fr.vahor.simpleschematics.schematics.data;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"children"}, callSuper = true)
public class SchematicFolder extends ASchematic {

    private final List<ASchematic> children = new ArrayList<>();

    public SchematicFolder(String name) {
        super(name);
    }

    @Override
    protected String getExtension() {
        return "";
    }

    public void addSchematic(ASchematic schematic) {
        schematic.setParent(this);
        children.add(schematic);
    }

    public List<ASchematic> getChildren() {
        return children;
    }

    public boolean hasChildrenWithName(String name) {
        for (ASchematic child : children) {
            if (child.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }
}
