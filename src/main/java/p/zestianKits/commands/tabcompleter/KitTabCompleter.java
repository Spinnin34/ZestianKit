package p.zestianKits.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.service.KitService;
import p.zestianKits.model.Kit;

import java.util.*;
import java.util.stream.Collectors;

public class KitTabCompleter implements TabCompleter {

    private final KitService kitService;
    private final Map<String, KitSubCommand> subCommands;

    public KitTabCompleter(KitService kitService, Map<String, KitSubCommand> subCommands) {
        this.kitService = kitService;
        this.subCommands = subCommands;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player player)) return completions;

        String currentArg = args.length > 0 ? args[args.length - 1].toLowerCase() : "";

        if (args.length == 1) {
            subCommands.values().stream()
                    .filter(subCmd -> player.hasPermission(subCmd.getPermission()))
                    .map(KitSubCommand::getName)
                    .forEach(completions::add);
        }
        else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();

            if (subCommand.equals("edit") || subCommand.equals("delete") || subCommand.equals("set-default")) {
                kitService.getAllKits().stream()
                        .map(Kit::getName)
                        .forEach(completions::add);
            }
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            completions.addAll(Arrays.asList("60", "3600", "86400"));
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("create")) {
            completions.addAll(Arrays.asList("zestian.kits.default", "zestian.kits.vip", "zestian.kits.admin"));
        }

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(currentArg))
                .sorted()
                .collect(Collectors.toList());
    }
}
