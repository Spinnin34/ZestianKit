package p.zestianKits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.commands.subcommands.*;
import p.zestianKits.gui.KitMenu;
import p.zestianKits.model.commands.Permissions;
import p.zestianKits.service.KitService;
import p.zestianKits.utils.messages.LangUtil;

import java.util.HashMap;
import java.util.Map;

public class KitCommand implements CommandExecutor {

    private final static Map<String, KitSubCommand> subCommands = new HashMap<>();
    private final KitService kitService;

    public KitCommand(KitService kitService) {
        this.kitService = kitService;
        registerSubCommands();
    }

    private void registerSubCommands() {
        register(new CreateKitCommand(kitService));
        register(new EditKitCommand(kitService));
        register(new DeleteKitCommand(kitService));
        register(new ReloadKitCommand(kitService));
        register(new SetDefaultKitCommand(kitService));
    }

    private void register(KitSubCommand command) {
        subCommands.put(command.getName(), command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LangUtil.getString("messages.notplayer", true));
            return true;
        }

        if (!player.hasPermission(Permissions.COMMAND_DEFUALT.getPermission())) {
            player.sendMessage(LangUtil.getString(player, "messages.notperms", true));
            return true;
        }

        if (args.length == 0) {
            kitService.reloadKits();
            new KitMenu(kitService, ZestianKits.getInstance()).open(player);
            return true;
        }
        String subCommandName = args[0].toLowerCase();
        KitSubCommand subCommand = subCommands.get(subCommandName);
        if (subCommand != null) {
            subCommand.execute(player, args);
        } else {
            player.sendMessage(LangUtil.getString(player, "messages.invalid-subcommand", true));
        }
        return true;
    }

    public static Map<String, KitSubCommand> getSubCommands() {
        return subCommands;
    }
}
