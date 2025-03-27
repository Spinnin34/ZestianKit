package p.zestianKits.handler.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import p.zestianKits.ZestianKits;
import p.zestianKits.handler.BaseInventoryHandler;
import p.zestianKits.service.KitViewService;

public class KitViewHandler extends BaseInventoryHandler {

    private final KitViewService kitViewService;

    public KitViewHandler(ZestianKits plugin, KitViewService kitViewService) {
        super(plugin, "kit_icon");
        this.kitViewService = kitViewService;
    }

    @Override
    public boolean shouldHandle(InventoryClickEvent event) {
        return event.getClick() == ClickType.RIGHT && getPersistentString(event.getCurrentItem()) != null;
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
        String kitName = getPersistentString(event.getCurrentItem());
        kitViewService.openKitView((Player) event.getWhoClicked(), kitName);
    }
}
