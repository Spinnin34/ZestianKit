package p.zestianKits.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import p.zestianKits.database.DatabaseManager;
import p.zestianKits.model.Kit;

import java.sql.SQLException;
import java.sql.Timestamp;

public class KitClaimService {

    private final KitService kitService;
    private final DatabaseManager dbManager;

    public KitClaimService(KitService kitService, DatabaseManager dbManager) {
        this.kitService = kitService;
        this.dbManager = dbManager;
    }

    public void handleClaim(Player player, String kitName) {
        Kit kit = kitService.getKit(kitName);
        if(kit == null) {
            player.sendMessage("§cEl kit no existe.");
            return;
        }

        try {
            Timestamp lastUsed = dbManager.getLastUsed(player.getUniqueId(), kitName);
            long now = System.currentTimeMillis();

            if(lastUsed == null || now - lastUsed.getTime() >= kit.getCooldown() * 1000L) {
                dbManager.updateLastUsed(player.getUniqueId(), kitName);
                distributeKitItems(player, kit);
                player.sendMessage("§aHas reclamado el kit: " + kitName);
            } else {
                long remaining = (kit.getCooldown() * 1000L - (now - lastUsed.getTime())) / 1000L;
                player.sendMessage("§cDebes esperar " + remaining + " segundos para reclamar este kit nuevamente.");
            }
        } catch(SQLException e) {
            handleClaimError(player, e);
        }
    }

    private void distributeKitItems(Player player, Kit kit) {
        for(ItemStack item : kit.getItems()) {
            if(item != null) {
                player.getInventory().addItem(item);
            }
        }
    }

    private void handleClaimError(Player player, Exception e) {
        player.sendMessage("§cError al procesar el cooldown.");
        e.printStackTrace();
    }
}
