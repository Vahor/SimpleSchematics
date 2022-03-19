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

package fr.vahor.simpleschematics;

import fr.vahor.simpleschematics.utils.Schema;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Pattern;

@Getter
@ToString
public class Config {

    private String schematicsFolderName;
    private String schematicsFolderPath;
    private String toolIconMaterial;
    private String separator;
    private Pattern separatorPattern;
    private Material defaultFolderMaterial;
    private int thumbnailSize;

    public Config(FileConfiguration configuration) {
        reload(configuration);
    }

    public void reload(FileConfiguration configuration) {
        System.out.println("load Config");
        schematicsFolderPath  = configuration.getString("folder");
        toolIconMaterial      = configuration.getString("toolIconMaterial");
        thumbnailSize         = configuration.getInt("thumbnailSize");
        separator             = configuration.getString("separator");
        defaultFolderMaterial = Material.valueOf(configuration.getString("defaultFolderMaterial"));
        separatorPattern      = Pattern.compile(String.format("\\%s", separator));
        Schema.generatePattern(separator);

        String[] split = schematicsFolderPath.split("/");
        schematicsFolderName = split[split.length - 1];

    }
}
