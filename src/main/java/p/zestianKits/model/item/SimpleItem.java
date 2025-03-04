package p.zestianKits.model.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import p.zestianKits.ZestianKits;

public class SimpleItem implements GuiItem {
    private final ItemStack item;

    public SimpleItem(ItemStack item) {
        this.item = item;
        setActionKey();
    }

    @Override
    public ItemStack getItemStack() {
        return item;
    }

    private void setActionKey() {
        ItemMeta meta = getItemStack().getItemMeta();
        if (meta != null) {
            NamespacedKey kitIcon = new NamespacedKey(ZestianKits.getInstance(), "gui_item");
            meta.getPersistentDataContainer().set(kitIcon, PersistentDataType.STRING, "gui_item");

            getItemStack().setItemMeta(meta);
        }
    }
}

