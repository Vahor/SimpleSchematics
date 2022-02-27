package fr.vahor.listeners;

import fr.vahor.API;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        API.removePlayer(event.getPlayer().getUniqueId());
    }
}
