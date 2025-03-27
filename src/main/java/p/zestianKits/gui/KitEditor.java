package p.zestianKits.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import p.zestianKits.ZestianKits;
import p.zestianKits.model.Gui;
import p.zestianKits.model.Kit;
import p.zestianKits.model.OperationType;
import p.zestianKits.model.item.ButtonItem;
import p.zestianKits.model.item.ItemBuilder;
import p.zestianKits.model.item.SimpleItem;
import p.zestianKits.service.KitService;
import p.zestianKits.service.editor.KitSession;
import p.zestianKits.utils.messages.LangUtil;

import java.util.ArrayList;
import java.util.List;

public class KitEditor {

    private final KitService kitService;
    private final String kitName;
    private final long cooldown;
    private final String permission;
    private final ZestianKits plugin;

    public KitEditor(KitService kitService, String kitName, long cooldown, String permission, ZestianKits plugin) {
        this.kitService = kitService;
        this.kitName = kitName;
        this.cooldown = cooldown;
        this.permission = permission;
        this.plugin = plugin;
    }

    public void open(Player player) {
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage("§cEl kit no existe.");
            return;
        }

        KitSession session = new KitSession(
                OperationType.EDIT,
                kitName,
                cooldown,
                permission,
                kit.getId()
        );

        Gui gui = Gui.normal()
                .setTitle("Editar Kit: " + kitName + " | CD: " + cooldown + " | Perm: " + permission + " | ID: " + kit.getId())
                .setStructure(getStructure())
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build()))
                .addIngredient('S', new ButtonItem(new ItemBuilder(Material.EMERALD_BLOCK)
                        .setDisplayName("§a✔ Guardar Cambios").build(), "save", plugin))
                .addIngredient('C', new ButtonItem(new ItemBuilder(Material.BARRIER)
                        .setDisplayName("§c✘ Cancelar").build(), "cancel", plugin))
                .build();

        Inventory inv = gui.getInventory();
        ItemStack[] items = kit.getItems();
        List<Integer> editableSlots = getEditableSlots();

        for (int i = 0; i < editableSlots.size(); i++) {
            inv.setItem(editableSlots.get(i), i < items.length ? items[i] : new ItemStack(Material.AIR));
        }

        player.openInventory(inv);
        ZestianKits.getInstance().getSessionManager().createSession(player.getUniqueId(), session);
    }

    public void handleSave(Player player, Inventory inv) {
        Kit existingKit = kitService.getKit(kitName);
        if (existingKit == null) {
            player.sendMessage("§cEl kit no existe.");
            return;
        }

        List<Integer> editableSlots = getEditableSlots();
        ItemStack[] kitItems = new ItemStack[editableSlots.size()];

        for (int i = 0; i < editableSlots.size(); i++) {
            ItemStack item = inv.getItem(editableSlots.get(i));
            kitItems[i] = item != null ? item.clone() : new ItemStack(Material.AIR);
        }

        Kit updatedKit = new Kit(existingKit.getId(), kitName, kitItems, cooldown, permission);
        if (kitService.updateKit(kitName, updatedKit)) {
            player.sendMessage(LangUtil.getString(player, "messages.kit-updated", true));
            kitService.reloadKits();
            new KitMenu(kitService, plugin).open(player);
        } else {
            player.sendMessage("§cError al editar el kit.");
        }
        player.closeInventory();
    }

    private List<Integer> getEditableSlots() {
        List<Integer> editableSlots = new ArrayList<>();
        int columns = 9;
        String[] structure = getStructure();

        for (int row = 0; row < structure.length; row++) {
            String rowStr = structure[row].replace(" ", "");
            for (int col = 0; col < rowStr.length(); col++) {
                if (rowStr.charAt(col) == '.') {
                    editableSlots.add(row * columns + col);
                }
            }
        }
        return editableSlots;
    }

    public String[] getStructure() {
        return new String[]{
                "# # # # # # # # #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# S # # # # # C #"
        };
    }
}
