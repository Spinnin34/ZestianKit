package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.model.commands.Permissions;
import p.zestianKits.service.KitService;
import p.zestianKits.utils.messages.LangUtil;

import java.util.Map;

public class SetDefaultKitCommand implements KitSubCommand {

    private final KitService kitService;

    public SetDefaultKitCommand(KitService kitService) {
        this.kitService = kitService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            player.sendMessage(LangUtil.getString(player, "messages.notperms", true));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(LangUtil.getString(player, "messages.usage-set-default-kit", true));
            return;
        }

        String kitName = args[1];
        if (kitService.getKit(kitName) == null) {
            player.sendMessage(LangUtil.getString(player, "messages.kit-not-exists", true));
            return;
        }

        ZestianKits.getInstance().getConfig().set("default-kit", kitName);
        ZestianKits.getInstance().saveConfig();
        player.sendMessage(LangUtil.getString("messages.default-kit-set", true, Map.of("%kit%", kitName)));
    }

    @Override
    public String getName() {
        return "set-default";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_SET_DEFAULT.getPermission();
    }
}