package p.zestianKits;

import org.bukkit.plugin.java.JavaPlugin;
import p.zestianKits.commands.KitCommand;
import p.zestianKits.commands.tabcompleter.KitTabCompleter;
import p.zestianKits.database.DatabaseManager;
import p.zestianKits.handler.KitClaimHandler;
import p.zestianKits.handler.item.ActionHandler;
import p.zestianKits.handler.item.GuiItemHandler;
import p.zestianKits.handler.registry.HandlerRegistry;
import p.zestianKits.listener.InventoryListener;
import p.zestianKits.repository.FileKitRepository;
import p.zestianKits.service.KitClaimService;
import p.zestianKits.service.KitSaveService;
import p.zestianKits.service.KitService;

public final class ZestianKits extends JavaPlugin {

    private DatabaseManager databaseManager;
    private static ZestianKits instance;

    @Override
    public void onEnable() {
        instance = this;
        databaseManager = new DatabaseManager(this);

        FileKitRepository kitRepository = new FileKitRepository(getDataFolder());
        KitService kitService = new KitService(kitRepository);

        if (getCommand("kit") != null) {
            getCommand("kit").setExecutor(new KitCommand(kitService));
            getCommand("kit").setTabCompleter(new KitTabCompleter(kitService, KitCommand.getSubCommands()));
        }

        HandlerRegistry handlerRegistry = new HandlerRegistry();

        handlerRegistry.registerHandler(new ActionHandler(this, new KitSaveService(kitService, this)));
        handlerRegistry.registerHandler(new KitClaimHandler(this, new KitClaimService(kitService, databaseManager)));
        handlerRegistry.registerHandler(new GuiItemHandler(this));

        new InventoryListener(this, handlerRegistry);

        getLogger().info("ZestianKits ha sido habilitado.");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
        getLogger().info("ZestianKits ha sido deshabilitado.");
    }

    public static ZestianKits getInstance() {
        return instance;
    }
}
