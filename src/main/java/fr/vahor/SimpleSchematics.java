package fr.vahor;

import fr.vahor.commands.CommandHandler;
import fr.vahor.listeners.InventoryListener;
import fr.vahor.listeners.PlayerInteractListener;
import fr.vahor.listeners.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSchematics extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        API.initializeConfiguration(getConfig());
        API.initializeLogger(getLogger());

        API.loadSchematics();
        API.loadWorldEdit();

        API.init();

        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        getCommand("s").setExecutor(new CommandHandler());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
    }

}
