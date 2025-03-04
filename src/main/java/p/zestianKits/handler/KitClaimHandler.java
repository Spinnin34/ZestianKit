package p.zestianKits.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import p.zestianKits.ZestianKits;
import p.zestianKits.service.KitClaimService;

public class KitClaimHandler extends BaseInventoryHandler {

    private final KitClaimService claimService;

    public KitClaimHandler(ZestianKits plugin, KitClaimService claimService) {
        super(plugin, "kit_icon");
        this.claimService = claimService;
    }

    @Override
    public boolean shouldHandle(InventoryClickEvent event) {
        return getPersistentString(event.getCurrentItem()) != null;
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
        String kitName = getPersistentString(event.getCurrentItem());
        claimService.handleClaim((Player) event.getWhoClicked(), kitName);
    }
}
