package p.zestianKits.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import p.zestianKits.ZestianKits;
import p.zestianKits.gui.KitCreate;
import p.zestianKits.gui.KitEditor;
import p.zestianKits.service.editor.KitSession;
import p.zestianKits.utils.messages.LangUtil;

import java.util.Optional;
import java.util.logging.Level;

public class KitSaveService {

    private static final Logger log = LoggerFactory.getLogger(KitSaveService.class);
    private final KitService kitService;
    private final ZestianKits plugin;

    public KitSaveService(KitService kitService, ZestianKits plugin) {
        this.kitService = kitService;
        this.plugin = plugin;
    }

    public void handleSave(Player player, Inventory inventory) {
        Optional<KitSession> session = plugin.getSessionManager().getSession(player.getUniqueId());

        if (!session.isPresent()) {
            player.sendMessage(LangUtil.getString("messages.error-session-not-found", true));
            log.error("Sesión no encontrada para: {}", player.getName());
            return;
        }

        try {
            executeSaveOperation(session.get(), inventory, player);
        } finally {
            plugin.getSessionManager().removeSession(player.getUniqueId());
        }
    }

    private void executeSaveOperation(KitSession session, Inventory inventory, Player player) {
        try {
            switch (session.getOperationType()) {
                case CREATE:
                    handleCreateOperation(session, inventory, player);
                    break;
                case EDIT:
                    handleEditOperation(session, inventory, player);
                    break;
                default:
                    handleInvalidOperation(session, player);
            }
        } catch (Exception e) {
            handleSaveError(player, e);
        }
    }

    private void handleCreateOperation(KitSession session, Inventory inventory, Player player) {
        new KitCreate(
                kitService,
                session.getKitName(),
                session.getCooldown(),
                session.getPermission(),
                session.getId(),
                plugin
        ).handleSave(player, inventory);
    }

    private void handleEditOperation(KitSession session, Inventory inventory, Player player) {
        new KitEditor(
                kitService,
                session.getKitName(),
                session.getCooldown(),
                session.getPermission(),
                plugin
        ).handleSave(player, inventory);
    }

    private void handleInvalidOperation(KitSession session, Player player) {
        String errorMsg = String.format(
                "Operación no soportada: %s | Jugador: %s",
                session.getOperationType(),
                player.getName()
        );

        plugin.getLogger().log(Level.SEVERE, errorMsg);
        player.sendMessage("§cError interno: Tipo de operación no válido");
    }

    private void handleSaveError(Player player, Exception e) {
        player.sendMessage(LangUtil.getString("messages.error-saving-kit", true));
        log.error("Error al guardar kit para {}: {}", player.getName(), e.getMessage());
        plugin.getLogger().log(Level.SEVERE, "Detalle del error:", e);
    }
}