package fr.vahor;

import fr.vahor.commands.CommandHandler;
import fr.vahor.commands.CommandHandlerTabCompleter;
import fr.vahor.fawe.FaweColorCache;
import fr.vahor.i18n.Message;
import fr.vahor.listeners.InventoryListener;
import fr.vahor.listeners.PlayerInteractListener;
import fr.vahor.listeners.PlayerQuitListener;
import fr.vahor.schematics.SchematicsPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SimpleSchematics extends JavaPlugin {

    @Getter private static SimpleSchematics instance;

    @Override
    public void onEnable() {
        instance = this;
        writeConfigs();
        API.initializeConfiguration(getConfig());
        API.initializeLogger(getLogger());

        API.loadSchematics();
        FaweColorCache.init();

        Message.loadLanguage();
        API.init();

        registerCommands();
        registerListeners();
    }

    public void reload() {
        Message.loadLanguage();
        API.getPlayers().forEach(SchematicsPlayer::clearSelected);
        API.loadSchematics();
        API.getConfiguration().reload(getConfig());
        // todo unload schematics

        // todo retirer
        FaweColorCache.init();
    }

    private void registerCommands() {
        getCommand("s").setExecutor(new CommandHandler());
        getCommand("s").setTabCompleter(new CommandHandlerTabCompleter());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
    }

    private void writeConfigs() {
        saveDefaultConfig();
        File messageFile = new File(getDataFolder(), "messages.properties");
        if (!messageFile.exists()) {
            saveResource("messages.properties", false);
        }

    }

}
