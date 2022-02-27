package fr.vahor.schematics.data;

import fr.vahor.API;
import lombok.ToString;

import java.io.File;

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
        if(parent != null && parent.getParent() != null)
            path += parent.getPath(separator) + separator;
        path += name;
        return path;
    }

    public File getAsFile() {
        String path = getPath(API.SYSTEM_SEPERATOR);
        File file = new File(API.getConfiguration().getSchematicsFolderPath(), path);
        return file.getAbsoluteFile();
    }
}
