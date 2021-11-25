package me.kuboeb69.core.listeners;

import me.kuboeb69.core.Core;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final Core _plugin;

    public PlayerJoin(Core plugin) {
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinServer(PlayerJoinEvent event) {
        Location spawn;

        try {
            spawn = _plugin.getServer().getWorld("hub").getSpawnLocation();
            event.getPlayer().teleport(spawn);

        } catch(NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
