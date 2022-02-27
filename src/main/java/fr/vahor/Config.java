package fr.vahor;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Pattern;

@Getter
public class Config {

    private String schematicsFolderPath;
    private String toolIconMaterial;
    private String seperator;
    private Pattern seperatorPattern;

    public Config(FileConfiguration configuration) {
        reload(configuration);
    }

    public void reload(FileConfiguration configuration) {
        schematicsFolderPath = configuration.getString("folder");
        toolIconMaterial = configuration.getString("toolIconMaterial");
        seperator = configuration.getString("seperator");
        seperatorPattern = Pattern.compile(String.format("\\%s", seperator));
    }
}
