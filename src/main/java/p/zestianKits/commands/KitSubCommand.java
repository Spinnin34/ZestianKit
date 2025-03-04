package p.zestianKits.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface KitSubCommand {
    void execute(Player player, String[] args);
    String getName();
    String getPermission();
    String getUsage();
}

