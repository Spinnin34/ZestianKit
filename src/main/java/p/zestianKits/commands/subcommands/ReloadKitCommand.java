package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.service.KitService;

public class ReloadKitCommand implements KitSubCommand {

    private final KitService kitService;

    public ReloadKitCommand(KitService kitService) {
        this.kitService = kitService;
    }

    @Override
    public void execute(Player player, String[] args) {
        kitService.reloadKits();
        player.sendMessage("§aKits recargados correctamente.");
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "zestiankits.admin";
    }

    @Override
    public String getUsage() {
        return "/kit reload";
    }
}

