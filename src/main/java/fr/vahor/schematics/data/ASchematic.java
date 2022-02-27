package fr.vahor.schematics.data;

import fr.vahor.API;
import lombok.ToString;

@ToString(of = {"name"})
public abstract class ASchematic {

    private final String name;
    private SchematicFolder parent = null; // Null for root folder

    public ASchematic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SchematicFolder getParent() {
        return parent;
    }

    protected void setParent(SchematicFolder parent){
        this.parent = parent;
    }

    public String getPath() {
        return getPath(API.getConfiguration().getSeperator());
    }

    public String getPath(String separator) {
        String path = "";
        if(parent != null && parent.getParent() != null) // Don't want root folder in path
            path += parent.getPath(separator) + separator;
        path += name;
        return path;
    }
}
