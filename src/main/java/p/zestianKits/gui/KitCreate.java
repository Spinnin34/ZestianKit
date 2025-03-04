package p.zestianKits.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import p.zestianKits.ZestianKits;
import p.zestianKits.model.Gui;
import p.zestianKits.model.Kit;
import p.zestianKits.model.item.ButtonItem;
import p.zestianKits.model.item.ItemBuilder;
import p.zestianKits.model.item.SimpleItem;
import p.zestianKits.service.KitService;

import java.util.ArrayList;
import java.util.List;

public class KitCreate {

    private final KitService kitService;
    private final String kitName;
    private final long cooldown;
    private final ZestianKits plugin;

    public KitCreate(KitService kitService, String kitName, long cooldown, ZestianKits plugin) {
        this.kitService = kitService;
        this.kitName = kitName;
        this.cooldown = cooldown;
        this.plugin = plugin;
    }

    public void open(Player player) {
        Gui gui = Gui.normal()
                .setTitle("Crear Kit: " + kitName + " - CD: " + cooldown)
                .setStructure(getStructure())
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build()))
                .addIngredient('S', new ButtonItem(new ItemBuilder(Material.EMERALD_BLOCK)
                        .setDisplayName("§aGuardar Kit").build(), "save", plugin))
                .addIngredient('C', new ButtonItem(new ItemBuilder(Material.BARRIER)
                        .setDisplayName("§cCancelar").build(), "cancel", plugin))
                .build();
        player.openInventory(gui.getInventory());
    }

    public void handleSave(Player player, Inventory inv) {
        List<Integer> editableSlots = new ArrayList<>();
        int rows = 6;
        int columns = 9;

        for (int row = 0; row < rows; row++) {
            String rowStr = getStructure()[row].replace(" ", "");
            for (int col = 0; col < rowStr.length(); col++) {
                int slot = row * columns + col;
                char slotType = rowStr.charAt(col);

                if (slotType == '.') {
                    editableSlots.add(slot);
                }
            }
        }

        ItemStack[] kitItems = new ItemStack[editableSlots.size()];

        for (int i = 0; i < editableSlots.size(); i++) {
            kitItems[i] = inv.getItem(editableSlots.get(i));
        }

        Kit kit = new Kit(kitName, kitItems, cooldown);
        if (kitService.createKit(kitName, kit)) {
            player.sendMessage("§aKit creado exitosamente!");
        } else {
            player.sendMessage("§cError al crear el kit (quizá ya existe).");
        }
        player.closeInventory();
    }


    public String[] getStructure() {
        String[] structure = {
                "# # # # # # # # #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# S # # # # # C #"
        };
        return structure;
    }
}
