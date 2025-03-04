package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.gui.KitEditor;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.model.Kit;
import p.zestianKits.service.KitService;

public class EditKitCommand implements KitSubCommand {

    private final KitService kitService;

    public EditKitCommand(KitService kitService) {
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
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage("§cEl kit no existe.");
            return;
        }

        new KitEditor(kitService, kitName, kit.getCooldown(), ZestianKits.getInstance()).open(player);
    }

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getPermission() {
        return "zestiankits.admin";
    }

    @Override
    public String getUsage() {
        return "/kit edit <nombre>";
    }
}
