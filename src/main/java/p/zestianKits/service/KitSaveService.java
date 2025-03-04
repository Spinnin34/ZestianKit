package p.zestianKits.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import p.zestianKits.ZestianKits;
import p.zestianKits.gui.KitCreate;
import p.zestianKits.gui.KitEditor;
import p.zestianKits.model.KitOperationContext;
import p.zestianKits.model.OperationType;

import java.util.Optional;
import java.util.logging.Level;

public class KitSaveService {

    private static final String CREATE_PREFIX = "Crear Kit: ";
    private static final String EDIT_PREFIX = "Editar Kit: ";
    private static final String TITLE_SEPARATOR = " - CD: ";

    private final KitService kitService;
    private final ZestianKits plugin;

    public KitSaveService(KitService kitService, ZestianKits plugin) {
        this.kitService = kitService;
        this.plugin = plugin;
    }

    /**
     * Handles save operation based on inventory title context
     * @param title Inventory title
     * @param inventory Inventory instance
     * @param player Player performing the action
     */
    public void handleSave(String title, Inventory inventory, Player player) {
        Optional<KitOperationContext> context = parseTitle(title);

        if(!context.isPresent()) {
            player.sendMessage("§cError: Formato de título inválido");
            logParsingError(title);
            return;
        }

        executeSaveOperation(context.get(), inventory, player);
    }

    private void executeSaveOperation(KitOperationContext context, Inventory inventory, Player player) {
        try {
            switch(context.getOperationType()) {
                case CREATE:
                    new KitCreate(kitService, context.getKitName(), context.getCooldown(), plugin)
                            .handleSave(player, inventory);
                    break;
                case EDIT:
                    new KitEditor(kitService, context.getKitName(), context.getCooldown(), plugin)
                            .handleSave(player, inventory);
                    break;
                default:
                    handleInvalidOperation(context, player);
            }
        } catch (Exception e) {
            handleSaveError(player, e);
        }
    }

    private Optional<KitOperationContext> parseTitle(String title) {
        if(title == null || title.isEmpty()) return Optional.empty();

        String[] parts = title.split(TITLE_SEPARATOR);
        if(parts.length != 2) return Optional.empty();

        Optional<OperationType> operationType = detectOperationType(parts[0]);
        if(!operationType.isPresent()) return Optional.empty();

        Optional<String> kitName = extractKitName(parts[0], operationType.get());
        if(!kitName.isPresent()) return Optional.empty();

        long cooldown = parseCooldown(parts[1].trim());

        return Optional.of(new KitOperationContext(
                operationType.get(),
                kitName.get(),
                cooldown
        ));
    }

    private Optional<OperationType> detectOperationType(String titlePart) {
        if(titlePart.startsWith(CREATE_PREFIX)) {
            return Optional.of(OperationType.CREATE);
        } else if(titlePart.startsWith(EDIT_PREFIX)) {
            return Optional.of(OperationType.EDIT);
        }
        return Optional.empty();
    }

    private Optional<String> extractKitName(String titlePart, OperationType operationType) {
        String prefix = operationType == OperationType.CREATE ? CREATE_PREFIX : EDIT_PREFIX;
        String kitName = titlePart.replace(prefix, "").trim();
        return kitName.isEmpty() ? Optional.empty() : Optional.of(kitName);
    }

    private long parseCooldown(String cooldownStr) {
        try {
            return Long.parseLong(cooldownStr);
        } catch(NumberFormatException e) {
            plugin.getLogger().log(Level.WARNING, "Cooldown inválido: {0}", cooldownStr);
            return 0;
        }
    }

    private void handleInvalidOperation(KitOperationContext context, Player player) {
        String errorMsg = String.format("Operación no soportada: %s", context.getOperationType());
        plugin.getLogger().log(Level.SEVERE, errorMsg);
        player.sendMessage("§cError interno: Operación no válida");
    }

    private void handleSaveError(Player player, Exception e) {
        player.sendMessage("§cError al guardar el kit");
        plugin.getLogger().log(Level.SEVERE, "Error al procesar guardado de kit", e);
    }

    private void logParsingError(String invalidTitle) {
        plugin.getLogger().log(Level.WARNING, "Error analizando título del inventario: {0}", invalidTitle);
    }
}