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
import p.zestianKits.utils.KitIDGenerator;
import p.zestianKits.utils.messages.LangUtil;

import java.util.ArrayList;
import java.util.List;

public class KitCreate {

    private final KitService kitService;
    private final String kitName;
    private final long cooldown;
    private final String permission;
    private final long kitId;
    private final ZestianKits plugin;

    public KitCreate(KitService kitService, String kitName, long cooldown, String permission, long id, ZestianKits plugin) {
        this.kitService = kitService;
        this.kitName = kitName;
        this.cooldown = cooldown;
        this.permission = permission;
        this.kitId = id;
        this.plugin = plugin;
    }

    public void open(Player player) {
        KitSession session = new KitSession(
                OperationType.CREATE,
                kitName,
                cooldown,
                permission,
                kitId
        );

        Gui gui = Gui.normal()
                .setTitle("Crear Kit: " + kitName)
                .setStructure(getStructure())
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build()))
                .addIngredient('S', new ButtonItem(new ItemBuilder(Material.EMERALD_BLOCK)
                        .setDisplayName("§a✔ Guardar Kit").build(), "save", plugin))
                .addIngredient('C', new ButtonItem(new ItemBuilder(Material.BARRIER)
                        .setDisplayName("§c✘ Cancelar").build(), "cancel", plugin))
                .build();
        player.openInventory(gui.getInventory());

        ZestianKits.getInstance().getSessionManager().createSession(player.getUniqueId(), session);
    }

    public void handleSave(Player player, Inventory inv) {
        List<Integer> editableSlots = getEditableSlots();
        ItemStack[] kitItems = new ItemStack[editableSlots.size()];

        for (int i = 0; i < editableSlots.size(); i++) {
            ItemStack item = inv.getItem(editableSlots.get(i));
            kitItems[i] = item != null ? item.clone() : new ItemStack(Material.AIR);
        }

        Kit kit = new Kit(kitId, kitName, kitItems, cooldown, permission);

        if (kitService.createKit(kitName, kit)) {
            player.sendMessage(LangUtil.getString(player, "messages.kit-created", true));
            kitService.reloadKits();
            new KitMenu(kitService, plugin).open(player);
        } else {
            player.sendMessage(LangUtil.getString(player, "messages.error-kit-created", true));
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
