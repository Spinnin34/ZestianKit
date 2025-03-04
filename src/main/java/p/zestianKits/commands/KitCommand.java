package p.zestianKits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.gui.KitMenu;
import p.zestianKits.commands.subcommands.CreateKitCommand;
import p.zestianKits.commands.subcommands.DeleteKitCommand;
import p.zestianKits.commands.subcommands.EditKitCommand;
import p.zestianKits.commands.subcommands.ReloadKitCommand;
import p.zestianKits.service.KitService;

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
        // Registro de cada subcomando
        register(new CreateKitCommand(kitService));
        register(new EditKitCommand(kitService));
        register(new DeleteKitCommand(kitService));
        register(new ReloadKitCommand(kitService));
        // Si el subcomando no existe se mostrará un mensaje de ayuda
    }

    private void register(KitSubCommand command) {
        subCommands.put(command.getName(), command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cSolo jugadores pueden usar este comando.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            // Abre el menú de kits si no hay argumentos
            new KitMenu(kitService, ZestianKits.getInstance()).open(player);
            return true;
        }
        String subCommandName = args[0].toLowerCase();
        KitSubCommand subCommand = subCommands.get(subCommandName);
        if (subCommand != null) {
            subCommand.execute(player, args);
        } else {
            // Muestra ayuda si el subcomando es inválido
            player.sendMessage("§cSubcomando inválido. Uso: /kit <create|edit|delete|reload>");
        }
        return true;
    }

    public static Map<String, KitSubCommand> getSubCommands() {
        return subCommands;
    }
}
