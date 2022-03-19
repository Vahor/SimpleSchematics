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

import lombok.Getter;
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
    @Getter private Material material;
    @Getter private int materialData = 0;

    public SchematicFolder(String name) {
        super(name);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    public void setMaterialData(int materialData) {
        this.materialData = materialData;
    }

    public File getConfigurationFile() {
        return new File(getAsFile(), "folder_data.yml");
    }

    public void loadMaterial() {
        File folderConfigFile = getConfigurationFile();
        if (folderConfigFile.exists()) {
            FileConfiguration folderConfig = YamlConfiguration.loadConfiguration(folderConfigFile);
            setMaterial(Material.valueOf(folderConfig.getString("material")));
            setMaterialData(folderConfig.getInt("data"));
        }
    }

    public void saveMaterial() throws IOException {
        File folderConfigFile = getConfigurationFile();
        FileConfiguration folderConfig;

        folderConfig = YamlConfiguration.loadConfiguration(folderConfigFile);
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
