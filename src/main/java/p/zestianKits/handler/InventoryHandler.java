package p.zestianKits.handler;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface InventoryHandler {
    boolean shouldHandle(InventoryClickEvent event);
    void handle(InventoryClickEvent event);
}