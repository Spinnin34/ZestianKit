package p.zestianKits.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import p.zestianKits.database.DatabaseManager;
import p.zestianKits.model.Kit;
import p.zestianKits.utils.messages.LangUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

public class KitClaimService {

    private final KitService kitService;
    private final DatabaseManager dbManager;

    public KitClaimService(KitService kitService, DatabaseManager dbManager) {
        this.kitService = kitService;
        this.dbManager = dbManager;
    }

    public void handleClaim(Player player, String kitName) {
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage(LangUtil.getString(player, "messages.kit-not-exists", true));
            return;
        }

        if (!player.hasPermission(kit.getPermission())) {
            player.sendMessage(LangUtil.getString(player, "messages.notperms-kit", true));
            return;
        }

        try {
            Timestamp lastUsed = dbManager.getLastUsed(player.getUniqueId(), kitName);
            long now = System.currentTimeMillis();

            if (lastUsed == null || now - lastUsed.getTime() >= kit.getCooldown() * 1000L) {
                if (!canFitAllItems(player, kit)) {
                    player.sendMessage(LangUtil.getString("messages.kit-no-space", true, Map.of("%kit%", kitName)));
                    return;
                }

                dbManager.updateLastUsed(player.getUniqueId(), kitName);
                distributeKitItems(player, kit);
                player.sendMessage(LangUtil.getString("messages.kit-claim", true, Map.of("%kit%", kitName)));
            } else {
                long remainingMillis = (kit.getCooldown() * 1000L - (now - lastUsed.getTime()));
                String formattedTime = formatTime(remainingMillis);
                player.sendMessage(LangUtil.getString("messages.kit-claim-cooldown", true,
                        Map.of("%kit%", kitName, "%time%", formattedTime)));
            }
        } catch (SQLException e) {
            handleClaimError(player, e);
        }
    }

    private boolean canFitAllItems(Player player, Kit kit) {
        int mainInventorySize = 36;
        Inventory tempInv = Bukkit.createInventory(null, mainInventorySize);

        for (int i = 0; i < mainInventorySize; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null) {
                tempInv.setItem(i, item.clone());
            }
        }

        for (ItemStack item : kit.getItems()) {
            if (item == null) continue;
            ItemStack itemClone = item.clone();
            Map<Integer, ItemStack> leftovers = tempInv.addItem(itemClone);
            if (!leftovers.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void distributeKitItems(Player player, Kit kit) {
        for (ItemStack item : kit.getItems()) {
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }
    }

    private void handleClaimError(Player player, Exception e) {
        player.sendMessage(LangUtil.getString(player, "messages.error-cooldown", true));
        e.printStackTrace();
    }

    public String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        if (days > 0) {
            return days + (days == 1 ? " día" : " días");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hora" : " horas");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minuto" : " minutos");
        } else {
            return seconds + (seconds == 1 ? " segundo" : " segundos");
        }
    }
}