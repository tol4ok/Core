package me.kuboeb69.core.commands;

import me.kuboeb69.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "world";
    }

    @Override
    public String[] getArgs() {
        return new String[] {
                "list",
                "create",
                "tp",
                "remove",
        };
    }

    @Override
    public void execute(Core plugin, Player player, String[] args) {
        if(!player.hasPermission("core.world")) {
            player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "You don't have permission to use that command");
            return;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("list")) {
                ListWorlds(plugin, player);
            }
            else {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Invalid argument");
            }
        }
        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("tp")) {
                Teleport(plugin, player, args[1]);
            }
            else if(args[0].equalsIgnoreCase("create")) {
                CreateWorld(plugin, player, args[1]);
            }
            else if(args[0].equalsIgnoreCase("remove")) {
                RemoveWorld(plugin, player, args[1], args[2]);
            }
            else {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Invalid arguments");
            }
        }
        else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("remove")) {
                RemoveWorld(plugin, player, args[1], args[2]);
            }
        }
        else {
            SendHelpUsage(player);
        }
    }

    private void RemoveWorld(Core plugin, Player player, String world_to_remove_name, String world_to_teleport_name) {
        FileConfiguration config = plugin.getConfig();
        Server server = plugin.getServer();

        List<String> worlds = config.getStringList("worlds");
        boolean isFoundWorldToRemove = false;
        boolean isFoundWorldToTeleport = false;

        for(String world_name : worlds) {
            if(world_to_remove_name.equals(world_name))
                isFoundWorldToRemove = true;
            if(world_to_teleport_name.equals(world_name) && !world_to_teleport_name.equals(world_to_remove_name))
                isFoundWorldToTeleport = true;
        }

        if(isFoundWorldToRemove && isFoundWorldToTeleport) {
            World world_to_remove = server.createWorld(new WorldCreator(world_to_remove_name));
            World world_to_teleport = server.createWorld(new WorldCreator(world_to_teleport_name));

            for(Player p : server.getOnlinePlayers()) {
                if(p.getWorld() == world_to_remove) {
                    p.teleport(world_to_teleport.getSpawnLocation());
                    p.sendMessage(ChatColor.YELLOW + "[Warning] " + ChatColor.GOLD + "You was teleported because the world you was in was removed");
                }
            }

            if(server.unloadWorld(world_to_remove, false)) {
                player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "World \"" + world_to_remove_name + "\" was removed");

                worlds.remove(world_to_remove_name);
                config.set("worlds", worlds);
                plugin.saveConfig();
            } else {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "World \"" + world_to_remove_name + "\" wasn't removed");
            }

        }
        else {
            player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Invalid world, type \"/world list\" to see all accessible worlds");
        }
    }

    private void Teleport(Core plugin, Player player, String world_name) {
        for(String w : plugin.getConfig().getStringList("worlds")) {
            if(world_name.equals(w)) {
                player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Generating world...");
                player.teleport(plugin.getServer().createWorld(new WorldCreator(world_name)).getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "You teleported");
                return;
            }
        }
        player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "This world doesn't exist");
    }

    private void CreateWorld(Core plugin, Player player, String world_name) {
        FileConfiguration cfg = plugin.getConfig();
        List<String> worlds = cfg.getStringList("worlds");

        for(String str : worlds) {
            if(str.equals(world_name)) {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "World with this name is already exist");
                return;
            }
        }

        player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Creating new world...");
        plugin.getServer().createWorld(new WorldCreator(world_name));
        worlds.add(world_name);

        cfg.set("worlds", worlds);
        plugin.saveConfig();
        player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "The world \"" + world_name + "\" was created");
    }

    private void ListWorlds(Core plugin, Player player) {
        player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "List of worlds: " + plugin.getConfig().getStringList("worlds").toString());
    }

    private void SendHelpUsage(Player player) {
        player.sendMessage(ChatColor.GOLD + "--------------------\n " +
                "Usage: \"\\world <arg> <world_name>\"\n" +
                "  Args:\n" +
                "  - remove - teleports all players to another world then removes and unloads world\n" +
                "  - create - creates new world\n" +
                "  - list - shows all worlds\n" +
                "  - tp - teleports you to another world\n" +
                "--------------------");
    }
}
