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

    static public String[] getArgs() {
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
            player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "У вас нет прав на использование этой команды");
            return;
        }

        if(args.length < 1) {
            player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Введены неизвестные аргументы");
            return;
        }

        switch(args[0]) {
            case "list" -> ListWorlds(plugin, player);
            case "tp" -> Teleport(plugin, player, args);
            case "create" -> CreateWorld(plugin, player, args);
            case "remove" -> RemoveWorld(plugin, player, args[1], args[2]);
            default -> SendHelpUsage(player);
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
                    p.sendMessage(ChatColor.YELLOW + "[Warning] " + ChatColor.GOLD + "Вы телепортиваны из-за удаления мира");
                }
            }

            if(server.unloadWorld(world_to_remove, false)) {
                player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Мир \"" + world_to_remove_name + "\" был удалён");

                worlds.remove(world_to_remove_name);
                config.set("worlds", worlds);
                plugin.saveConfig();
            } else {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Мир \"" + world_to_remove_name + "\" не был удалён");
            }

        }
        else {
            player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Неизвестный мир, используйте \"/world list\" чтобы увидеть все миры");
        }
    }

    private void Teleport(Core plugin, Player player, String[] args) {
        if(args.length != 2) {
            player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Укажите название мира");
            return;
        }

        String world_name = args[1];

        for(String w : plugin.getConfig().getStringList("worlds")) {
            if(world_name.equals(w)) {
                player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Генерация мира...");
                player.teleport(plugin.getServer().createWorld(new WorldCreator(world_name)).getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Вы телепортированы");
                return;
            }
        }
        player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Этот мир не существует");
    }

    private void CreateWorld(Core plugin, Player player, String[] args) {
        FileConfiguration cfg = plugin.getConfig();
        String world_name = args[1];
        List<String> worlds = cfg.getStringList("worlds");

        for(String str : worlds) {
            if(str.equals(world_name)) {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Мир с таким именем уже есть");
                return;
            }
        }

        player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Создание мира...");
        plugin.getServer().createWorld(new WorldCreator(world_name));
        worlds.add(world_name);

        cfg.set("worlds", worlds);
        plugin.saveConfig();
        player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Мир \"" + world_name + "\" создан");
    }

    private void ListWorlds(Core plugin, Player player) {
        player.sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Миры: " + plugin.getServer().getWorldContainer());
    }

    private void SendHelpUsage(Player player) {
        ChatColor gold = ChatColor.GOLD;
        ChatColor bold = ChatColor.BOLD;
        ChatColor blue = ChatColor.BLUE;

        player.sendMessage(
                gold + "--------------------\n " +
                        "Usage: \"\\world <arg>\"\n" +
                        "  Args:\n" +
                        blue + bold + "  - remove <world_name> <world_name> " + gold +  "- Удаляет первый мир телепортирует всех с первого мира во второй\n" +
                        blue + bold + "  - create <world_name> " + gold +  "- Создаёт новый мир, если мира с таким именем нет\n" +
                        blue + bold + "  - list " + gold + "- Показывает все миры\n" +
                        blue + bold + "  - tp <world_name> " + gold +  "- Телепортирует в другой мир, если он есть\n" +
                        gold + bold + "--------------------");
    }
}