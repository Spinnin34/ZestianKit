package p.zestianKits.handler.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import p.zestianKits.ZestianKits;
import p.zestianKits.handler.BaseInventoryHandler;
import p.zestianKits.service.KitSaveService;
import p.zestianKits.service.KitMenuService;

public class ActionHandler extends BaseInventoryHandler {

    private final KitSaveService saveService;
    private final KitMenuService menuService;

    public ActionHandler(ZestianKits plugin, KitSaveService saveService, KitMenuService menuService) {
        super(plugin, "action");
        this.saveService = saveService;
        this.menuService = menuService;
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

        switch (action.toLowerCase()) {
            case "save":
                saveService.handleSave(player, event.getInventory());
                break;
            case "cancel":
                player.closeInventory();
                player.sendMessage("§cAcción cancelada.");
                break;
            case "main":
                menuService.openMainMenu(player);
                break;
            default:
                player.sendMessage("§cAcción desconocida.");
                break;
        }
    }
}
