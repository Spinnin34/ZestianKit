package p.zestianKits.handler.item;

import org.bukkit.event.inventory.InventoryClickEvent;
import p.zestianKits.ZestianKits;
import p.zestianKits.handler.BaseInventoryHandler;

public class GuiItemHandler extends BaseInventoryHandler {

    public GuiItemHandler(ZestianKits plugin) {
        super(plugin, "gui_item");
    }

    @Override
    public boolean shouldHandle(InventoryClickEvent event) {
        return getPersistentString(event.getCurrentItem()) != null;
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
