package p.zestianKits.service;

import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.model.Kit;
import p.zestianKits.gui.KitView;
import p.zestianKits.utils.messages.LangUtil;

public class KitViewService {

    private final KitService kitService;
    private final ZestianKits plugin;

    public KitViewService(KitService kitService, ZestianKits plugin) {
        this.kitService = kitService;
        this.plugin = plugin;
    }

    public void openKitView(Player player, String kitName) {
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage(LangUtil.getString(player, "messages.kit-not-exists", true));
            return;
        }

        new KitView(kitService, kitName, plugin).open(player);
    }
}

