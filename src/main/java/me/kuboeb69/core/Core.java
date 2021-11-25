package me.kuboeb69.core;

import me.kuboeb69.core.commands.CommandManager;
import me.kuboeb69.core.listeners.PlayerJoin;
import me.kuboeb69.core.tabcompleters.WorldTabCompleter;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Core extends JavaPlugin {

    //private HashMap<UUID, PermissionAttachment> playersPermissions = new HashMap<>();

    @Override
    public void onEnable() {
        Server server = getServer();

        server.getPluginCommand("world").setExecutor(new CommandManager(this));
        server.getPluginCommand("world").setTabCompleter(new WorldTabCompleter(this));

        server.getPluginManager().registerEvents(new PlayerJoin(this), this);
    }

    @Override
    public void onDisable() {

    }

    //private void SetupPermissions(Player player) {

    //}

}
