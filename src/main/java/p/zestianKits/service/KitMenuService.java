package p.zestianKits.service;

import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;
import p.zestianKits.gui.KitMenu;

public class KitMenuService {

    private final KitService kitService;
    private final ZestianKits plugin;

    public KitMenuService(KitService kitService, ZestianKits plugin) {
        this.kitService = kitService;
        this.plugin = plugin;
    }

    public void openMainMenu(Player player) {
        new KitMenu(kitService, plugin).open(player);
    }
}

