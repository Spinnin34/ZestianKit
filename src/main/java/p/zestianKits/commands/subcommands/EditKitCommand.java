package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.gui.KitEditor;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.model.Kit;
import p.zestianKits.model.commands.Permissions;
import p.zestianKits.service.KitService;
import p.zestianKits.utils.messages.LangUtil;

public class EditKitCommand implements KitSubCommand {

    private final KitService kitService;

    public EditKitCommand(KitService kitService) {
        this.kitService = kitService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            player.sendMessage(LangUtil.getString(player, "messages.notperms", true));
            return;
        }
        if (args.length < 2) {
            player.sendMessage(LangUtil.getString(player, "messages.usage-edit-kit", true));
            return;
        }
        String kitName = args[1];
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage(LangUtil.getString(player, "messages.kit-not-exists", true));
            return;
        }

        new KitEditor(kitService, kitName, kit.getCooldown(), kit.getPermission(), ZestianKits.getInstance()).open(player);
    }

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_EDIT.getPermission();
    }
}