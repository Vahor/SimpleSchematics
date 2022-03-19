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

import fr.vahor.simpleschematics.commands.CommandHandler;
import fr.vahor.simpleschematics.commands.CommandHandlerTabCompleter;
import fr.vahor.simpleschematics.fawe.FaweColorCache;
import fr.vahor.simpleschematics.i18n.Message;
import fr.vahor.simpleschematics.listeners.InventoryListener;
import fr.vahor.simpleschematics.listeners.PlayerInteractListener;
import fr.vahor.simpleschematics.listeners.PlayerQuitListener;
import fr.vahor.simpleschematics.schematics.SchematicsPlayer;
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
        API.init();

        API.loadSchematics();
        FaweColorCache.init();

        Message.loadLanguage(getDataFolder());

        registerCommands();
        registerListeners();
    }

    public void reload() {
        Message.loadLanguage(getDataFolder());
        API.getPlayers().forEach(SchematicsPlayer::clearSelected);
        API.getConfiguration().reload(getConfig());
        API.init();

        API.loadSchematics();

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
