package fr.vahor.simpleschematics;

import fr.vahor.simpleschematics.utils.Schema;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Pattern;

@Getter
@ToString
public class Config {

    private String schematicsFolderPath;
    private String toolIconMaterial;
    private String separator;
    private Pattern separatorPattern;
    private int thumbnailSize;

    public Config(FileConfiguration configuration) {
        reload(configuration);
    }

    public void reload(FileConfiguration configuration) {
        System.out.println("load Config");
        schematicsFolderPath = configuration.getString("folder");
        toolIconMaterial = configuration.getString("toolIconMaterial");
        thumbnailSize    = configuration.getInt("thumbnailSize");
        separator        = configuration.getString("separator");
        separatorPattern     = Pattern.compile(String.format("\\%s", separator));
        Schema.generatePattern(separator);
    }
}
