package p.zestianKits.handler.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import p.zestianKits.ZestianKits;
import p.zestianKits.handler.BaseInventoryHandler;
import p.zestianKits.service.KitSaveService;

public class ActionHandler extends BaseInventoryHandler {

    private final KitSaveService saveService;

    public ActionHandler(ZestianKits plugin, KitSaveService saveService) {
        super(plugin, "action");
        this.saveService = saveService;
    }

    @Override
    public boolean shouldHandle(InventoryClickEvent event) {
        return getPersistentString(event.getCurrentItem()) != null;
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
        String action = getPersistentString(event.getCurrentItem());
        Player player = (Player) event.getWhoClicked();

        switch(action.toLowerCase()) {
            case "save":
                saveService.handleSave(
                        event.getView().getTitle(),
                        event.getInventory(),
                        player
                );
                break;
            case "cancel":
                player.closeInventory();
                player.sendMessage("§cAcción cancelada.");
                break;
        }
    }
}
