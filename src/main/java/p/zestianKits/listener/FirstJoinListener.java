package p.zestianKits.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import p.zestianKits.ZestianKits;
import p.zestianKits.model.Kit;
import p.zestianKits.service.KitService;
import p.zestianKits.utils.messages.LangUtil;

import java.util.Map;

public class FirstJoinListener implements Listener {

    private final KitService kitService;

    public FirstJoinListener(KitService kitService) {
        this.kitService = kitService;
        Bukkit.getServer().getPluginManager().registerEvents(this, ZestianKits.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore()) return;

        String kitName = ZestianKits.getInstance().getConfig().getString("default-kit");
        if (kitName == null || kitName.isEmpty()) return;

        Kit kit = kitService.getKit(kitName);
        if (kit == null) return;

        if (!canFitAllItems(player, kit)) {
            player.sendMessage(LangUtil.getString(player, "messages.default-kit-no-space", true));
            return;
        }

        for (ItemStack item : kit.getItems()) {
            if (item != null) {
                player.getInventory().addItem(item.clone());
            }
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
}
