package p.zestianKits.model;

import org.bukkit.inventory.ItemStack;

public class Kit {
    private final long id;
    private final String name;
    private final ItemStack[] items;
    private final long cooldown;
    private final String permission;

    public Kit(long id, String name, ItemStack[] items, long cooldown, String permission) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.cooldown = cooldown;
        this.permission = permission;
    }

    public long getId() {
        return id;
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

    public String getPermission() {
        return permission;
    }
}
