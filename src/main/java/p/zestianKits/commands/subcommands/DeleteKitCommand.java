package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.service.KitService;

public class DeleteKitCommand implements KitSubCommand {

    private final KitService kitService;

    public DeleteKitCommand(KitService kitService) {
        this.kitService = kitService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            player.sendMessage("§cNo tienes permiso para esto.");
            return;
        }
        if (args.length < 2) {
            player.sendMessage("§cUso: " + getUsage());
            return;
        }
        String kitName = args[1];
        if (kitService.deleteKit(kitName)) {
            player.sendMessage("§aKit eliminado correctamente.");
        } else {
            player.sendMessage("§cEl kit no existe o no se pudo eliminar.");
        }
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getPermission() {
        return "zestiankits.admin";
    }

    @Override
    public String getUsage() {
        return "/kit delete <nombre>";
    }
}

