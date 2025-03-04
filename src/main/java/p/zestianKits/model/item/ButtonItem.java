package p.zestianKits.model.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import p.zestianKits.ZestianKits;

public class ButtonItem extends SimpleItem {

    private final String action;
    private final ZestianKits plugin;

    public ButtonItem(ItemStack item, String action, ZestianKits plugin) {
        super(item);
        this.action = action;
        this.plugin = plugin;
        setActionKey();
    }

    private void setActionKey() {
        ItemMeta meta = getItemStack().getItemMeta();
        if (meta != null) {
            NamespacedKey actionKey = new NamespacedKey(plugin, "action");
            meta.getPersistentDataContainer().set(actionKey, PersistentDataType.STRING, action);

            NamespacedKey guiKey = new NamespacedKey(plugin, "gui_item");
            meta.getPersistentDataContainer().set(guiKey, PersistentDataType.STRING, "true");

            getItemStack().setItemMeta(meta);
        }
    }
}


