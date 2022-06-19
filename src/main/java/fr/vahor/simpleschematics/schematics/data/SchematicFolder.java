/*
 *     Copyright (C) 2022 Nathan David
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.vahor.simpleschematics.schematics.data;

import fr.vahor.simpleschematics.API;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"children"}, callSuper = true)
public class SchematicFolder extends ASchematic {

    private final List<ASchematic> children = new ArrayList<>();
    @Getter @Setter private Material material;
    @Getter @Setter private boolean generateThumbnail = true;
    @Getter @Setter private int materialData = 0;
    @Getter @Setter private String skullBase64 = null;

    public SchematicFolder(String name) {
        super(name);
    }

    public File getConfigurationFile() {
        return new File(getAsFile(), "folder_data.yml");
    }

    public void loadConfiguration() {
        File folderConfigFile = getConfigurationFile();
        if (folderConfigFile.exists()) {
            FileConfiguration folderConfig = YamlConfiguration.loadConfiguration(folderConfigFile);
            setMaterial(Material.valueOf(folderConfig.getString("material")));
            setMaterialData(folderConfig.getInt("data", 0));
            setSkullBase64(folderConfig.getString("skull_base64", null));
            setGenerateThumbnail(folderConfig.getBoolean("generate_thumbnail", true));
        }
    }

    public void saveConfiguration() throws IOException {
        File folderConfigFile = getConfigurationFile();
        FileConfiguration folderConfig;

        folderConfig = YamlConfiguration.loadConfiguration(folderConfigFile);
        if (material == null) material = API.getConfiguration().getDefaultFolderMaterial();
        folderConfig.set("material", material.name());
        folderConfig.set("data", materialData);
        folderConfig.set("skull_base64", skullBase64);
        folderConfig.set("generate_thumbnail", generateThumbnail);
        folderConfig.save(folderConfigFile);

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
