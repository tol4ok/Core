package me.kuboeb69.core.commands;

import me.kuboeb69.core.Core;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractCommand {

    public abstract String getName();

    public abstract void execute(Core plugin, Player player, String[] args);
}
