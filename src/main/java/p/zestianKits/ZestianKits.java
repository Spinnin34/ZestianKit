package p.zestianKits;

import org.bukkit.plugin.java.JavaPlugin;
import p.zestianKits.commands.KitCommand;
import p.zestianKits.commands.tabcompleter.KitTabCompleter;
import p.zestianKits.database.DatabaseManager;
import p.zestianKits.handler.item.*;
import p.zestianKits.handler.registry.HandlerRegistry;
import p.zestianKits.listener.FirstJoinListener;
import p.zestianKits.listener.InventoryListener;
import p.zestianKits.repository.FileKitRepository;
import p.zestianKits.service.*;
import p.zestianKits.service.editor.KitSessionManager;
import p.zestianKits.utils.messages.LangUtil;

public final class ZestianKits extends JavaPlugin {

    private DatabaseManager databaseManager;
    private KitSessionManager sessionManager;
    private HandlerRegistry handlerRegistry;
    private static ZestianKits instance;

    @Override
    public void onEnable() {
        instance = this;

        initializeLang();
        initializeCommand();
        initializeDatabase();
        initializeService();
        initializeRegistry();
        initializeListener();


        getLogger().info("ZestianKits ha sido habilitado.");
    }

    public void initializeLang() {
        saveDefaultConfig();
        LangUtil langUtil = new LangUtil();
        langUtil.saveDefaultLang();
    }

    private void initializeService() {
        this.sessionManager = new KitSessionManager();

    }

    public void initializeRegistry() {
        FileKitRepository kitRepository = new FileKitRepository(getDataFolder());
        KitService kitService = new KitService(kitRepository);

        handlerRegistry = new HandlerRegistry();

        handlerRegistry.registerHandler(new ActionHandler(this, new KitSaveService(kitService, this), new KitMenuService(kitService, this)));
        handlerRegistry.registerHandler(new KitClaimHandler(this, new KitClaimService(kitService, databaseManager)));
        handlerRegistry.registerHandler(new KitViewHandler(this, new KitViewService(kitService, this)));
        handlerRegistry.registerHandler(new GuiItemHandler(this));

        new InventoryListener(this, handlerRegistry);
    }

    private void initializeListener() {
        new InventoryListener(this, handlerRegistry);
        new FirstJoinListener(new KitService(new FileKitRepository(getDataFolder())));
    }

    public void initializeDatabase() {
        databaseManager = new DatabaseManager(this);
    }

    public void initializeCommand() {
        FileKitRepository kitRepository = new FileKitRepository(getDataFolder());
        KitService kitService = new KitService(kitRepository);

        if (getCommand("kit") != null) {
            getCommand("kit").setExecutor(new KitCommand(kitService));
            getCommand("kit").setTabCompleter(new KitTabCompleter(kitService, KitCommand.getSubCommands()));
        }
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

    public KitSessionManager getSessionManager() {
        return sessionManager;
    }
}
