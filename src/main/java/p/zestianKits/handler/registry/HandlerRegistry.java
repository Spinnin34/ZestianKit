package p.zestianKits.handler.registry;

import p.zestianKits.handler.InventoryHandler;

import java.util.ArrayList;
import java.util.List;

public class HandlerRegistry {
    private final List<InventoryHandler> handlers = new ArrayList<>();

    public void registerHandler(InventoryHandler handler) {
        handlers.add(handler);
    }

    public List<InventoryHandler> getHandlers() {
        return new ArrayList<>(handlers);
    }
}
