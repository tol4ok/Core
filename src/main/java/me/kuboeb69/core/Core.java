package me.kuboeb69.core;

import me.kuboeb69.core.commands.CommandManager;
import me.kuboeb69.core.tabcompleters.WorldTabCompleter;

import org.bukkit.Server;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();
        Server server = getServer();

        for(String world_name : config.getStringList("worlds")) {
            getServer().createWorld(new WorldCreator(world_name));
        }

        server.getPluginCommand("world").setExecutor(new CommandManager(this));
        server.getPluginCommand("world").setTabCompleter(new WorldTabCompleter(this));
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

}
