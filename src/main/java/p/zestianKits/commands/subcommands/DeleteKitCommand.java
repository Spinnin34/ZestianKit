package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.model.commands.Permissions;
import p.zestianKits.service.KitService;
import p.zestianKits.utils.messages.LangUtil;

public class DeleteKitCommand implements KitSubCommand {

    private final KitService kitService;

    public DeleteKitCommand(KitService kitService) {
        this.kitService = kitService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            player.sendMessage(LangUtil.getString("messages.notperms", true));
            return;
        }
        if (args.length < 2) {
            player.sendMessage(LangUtil.getString("messages.usage-delete-kit", true));
            return;
        }
        String kitName = args[1];
        if (kitService.deleteKit(kitName)) {
            player.sendMessage(LangUtil.getString("messages.kit-deleted", true));
        } else {
            player.sendMessage(LangUtil.getString("messages.kit-not-exists", true));
        }
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_DELETE.getPermission();
    }
}

