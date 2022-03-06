package fr.vahor.schematics.data;

import fr.vahor.API;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@ToString(of = {"name"})
@EqualsAndHashCode(of = {"name", "parent"})
public abstract class ASchematic implements Comparable<ASchematic> {

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

    protected void setParent(SchematicFolder parent) {
        this.parent = parent;
    }

    public String getPath() {
        return getPath(API.getConfiguration().getSeparator());
    }

    public String getPath(String separator) {
        return getPath(separator, "");
    }

    public String getPath(String separator, String extension) {
        String path = "";
        if (parent != null && parent.getParent() != null)
            path += parent.getPath(separator) + separator;
        path += name;
        path += extension;
        return path;

    }

    public File getAsFile() {
        String path = getPath(API.SYSTEM_SEPARATOR);
        File file = new File(API.getConfiguration().getSchematicsFolderPath(), path);
        return file.getAbsoluteFile();
    }

    @Override
    public int compareTo(@NotNull ASchematic o) {
        // Folders first
        // then sort by name
        if (this instanceof SchematicFolder) {
            if (!(o instanceof SchematicFolder)) {
                return -1;
            }
        }
        else {
            if (o instanceof SchematicFolder) {
                return 1;
            }
        }
        return name.compareTo(o.name);
    }
}
