package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.model.commands.Permissions;
import p.zestianKits.service.KitService;
import p.zestianKits.utils.messages.LangUtil;

public class ReloadKitCommand implements KitSubCommand {

    private final KitService kitService;

    public ReloadKitCommand(KitService kitService) {
        this.kitService = kitService;
    }

    @Override
    public void execute(Player player, String[] args) {
        kitService.reloadKits();
        LangUtil.reloadLang();
        player.sendMessage(LangUtil.getString(player, "messages.kits-reloaded", true));
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_RELOAD.getPermission();
    }
}

