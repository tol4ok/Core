package me.kuboeb69.core.tabcompleters;

import me.kuboeb69.core.Core;
import me.kuboeb69.core.commands.WorldCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class WorldTabCompleter implements TabCompleter {

    private final Core _plugin;

    public WorldTabCompleter(Core plugin) {
        _plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String str, String[] args) {

        if(args.length == 1) {
            return Arrays.asList(WorldCommand.getArgs());
        }
        else if(args.length > 1 && !args[0].equals("create") && !args[0].equals("list")) {
            return _plugin.getConfig().getStringList("worlds");
        }

        return null;
    }
}
