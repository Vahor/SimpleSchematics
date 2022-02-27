package fr.vahor.schematics.data;

import fr.vahor.API;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = true)
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

    public File getAsFile() {
        String folderPath = getPath(API.SYSTEM_SEPERATOR);
        folderPath = folderPath.substring(folderPath.indexOf(API.SYSTEM_SEPERATOR));
        File file = new File(API.getConfiguration().getSchematicsFolderPath(), folderPath);
        return file.getAbsoluteFile();
    }
}
