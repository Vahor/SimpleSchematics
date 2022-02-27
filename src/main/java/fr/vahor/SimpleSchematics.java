package fr.vahor;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSchematics extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Hello : " + getDescription().getVersion());
    }
}
