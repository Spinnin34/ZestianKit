package p.zestianKits.handler;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import p.zestianKits.ZestianKits;

public abstract class BaseInventoryHandler implements InventoryHandler {
    protected final ZestianKits plugin;
    protected final NamespacedKey targetKey;

    public BaseInventoryHandler(ZestianKits plugin, String keyName) {
        this.plugin = plugin;
        this.targetKey = new NamespacedKey(plugin, keyName);
    }

    protected String getPersistentString(ItemStack item) {
        if(item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta()
                .getPersistentDataContainer()
                .get(targetKey, PersistentDataType.STRING);
    }

    protected boolean hasPersistentKey(ItemStack item) {
        return item != null
                && item.hasItemMeta()
                && item.getItemMeta().getPersistentDataContainer().has(targetKey, PersistentDataType.STRING);
    }
}
