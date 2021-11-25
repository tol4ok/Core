package me.kuboeb69.core.commands;

import me.kuboeb69.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private final Core _plugin;
    private ArrayList<AbstractCommand> _commands = new ArrayList<AbstractCommand>();

    public CommandManager(Core plugin) {
        _plugin = plugin;
        _commands.add(new WorldCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        Player player;

        if(!(sender instanceof Player)) return false;
        player = (Player) sender;

        for(AbstractCommand cmd : _commands) {
            if(str.equalsIgnoreCase(cmd.getName())) {
                cmd.execute(_plugin, player, args);
                return true;
            }
        }

        return false;
    }
}
