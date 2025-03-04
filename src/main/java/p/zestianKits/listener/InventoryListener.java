package p.zestianKits.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import p.zestianKits.ZestianKits;
import p.zestianKits.handler.registry.HandlerRegistry;

public class InventoryListener implements Listener {

    private final HandlerRegistry handlerRegistry;

    public InventoryListener(ZestianKits plugin, HandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        handlerRegistry.getHandlers().forEach(handler -> {
            if (handler.shouldHandle(event)) {
                handler.handle(event);
            }
        });
    }
}