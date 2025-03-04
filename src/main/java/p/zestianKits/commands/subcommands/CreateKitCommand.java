package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.gui.KitCreate;
import p.zestianKits.gui.KitEditor;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.service.KitService;

public class CreateKitCommand implements KitSubCommand {

    private final KitService kitService;

    public CreateKitCommand(KitService kitService) {
        this.kitService = kitService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            player.sendMessage("§cNo tienes permiso para esto.");
            return;
        }
        if (args.length < 3) {
            player.sendMessage("§cUso: " + getUsage());
            return;
        }
        String kitName = args[1];
        if (kitService.getKit(kitName) != null) {
            player.sendMessage("§cEl kit ya existe.");
            return;
        }
        long cooldown;
        try {
            cooldown = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cEl cooldown debe ser un número válido.");
            return;
        }
        new KitCreate(kitService, kitName, cooldown, ZestianKits.getInstance()).open(player);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPermission() {
        return "zestiankits.admin";
    }

    @Override
    public String getUsage() {
        return "/kit create <nombre> <cooldown>";
    }
}
