package p.zestianKits.model;

import org.bukkit.inventory.ItemStack;

public class Kit {
    private final String name;
    private final ItemStack[] items;
    private final long cooldown;

    public Kit(String name, ItemStack[] items, long cooldown) {
        this.name = name;
        this.items = items;
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public long getCooldown() {
        return cooldown;
    }
}
