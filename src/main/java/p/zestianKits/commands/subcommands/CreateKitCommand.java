package p.zestianKits.commands.subcommands;

import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.gui.KitCreate;
import p.zestianKits.commands.KitSubCommand;
import p.zestianKits.model.commands.Permissions;
import p.zestianKits.service.KitService;
import p.zestianKits.utils.KitIDGenerator;
import p.zestianKits.utils.messages.LangUtil;

public class CreateKitCommand implements KitSubCommand {

    private final KitService kitService;
    private final KitIDGenerator idGenerator;

    public CreateKitCommand(KitService kitService) {
        this.kitService = kitService;
        this.idGenerator = new KitIDGenerator(ZestianKits.getInstance().getDataFolder());
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            player.sendMessage(LangUtil.getString(player, "messages.notperms", true));
            return;
        }
        if (args.length < 3) {
            player.sendMessage(LangUtil.getString(player, "messages.usage-create-kit", true));
            return;
        }
        String kitName = args[1];
        if (kitService.getKit(kitName) != null) {
            player.sendMessage(LangUtil.getString(player, "messages.kit-exists", true));
            return;
        }
        long cooldown;
        try {
            cooldown = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(LangUtil.getString(player, "messages.invalid-cooldown", true));
            return;
        }

        String permission = args.length >= 4 ? args[3] : "zestiankits.kit." + kitName;

        long id = idGenerator.getNextId();

        new KitCreate(kitService, kitName, cooldown, permission, id, ZestianKits.getInstance()).open(player);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_CREATE.getPermission();
    }
}
