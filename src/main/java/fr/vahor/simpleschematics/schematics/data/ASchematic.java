package fr.vahor.simpleschematics.schematics.data;

import fr.vahor.simpleschematics.API;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.File;

@ToString(of = {"name"})
@EqualsAndHashCode(of = {"name", "parent"})
public abstract class ASchematic implements Comparable<ASchematic> {

    private final String name;
    private SchematicFolder parent = null; // Null for root folder

    private String cachedPath = null; // todo add better cache system
    private String cachedSeparator = null;
    private String cachedExtension = null;

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

    protected String getExtension() {
        return ".schematic";
    }

    public String getPath() {
        return getPath(API.getConfiguration().getSeparator());
    }

    public String getPath(String separator) {
        return getPath(separator, getExtension());
    }

    public String getPath(String separator, String extension) {
        if (separator.equals(cachedSeparator) && extension.equals(cachedExtension)) {
            return cachedPath;
        }
        String path = "";
        if (parent != null && parent.getParent() != null)
            path += parent.getPath(separator, "") + separator;
        path += name;
        path += extension;

        cachedPath      = path;
        cachedExtension = extension;
        cachedSeparator = separator;
        return path;
    }

    public File getAsFile() {
        String path = getPath(API.SYSTEM_SEPARATOR);
        File file = new File(API.getConfiguration().getSchematicsFolderPath(), path);
        return file.getAbsoluteFile();
    }

    public void clearCache() {
        // todo call this method when moving parent/renaming
        cachedPath = null;
        cachedSeparator = null;
        cachedExtension = null;
    }

    @Override
    public int compareTo(ASchematic o) {
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
