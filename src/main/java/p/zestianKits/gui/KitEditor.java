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

public class KitEditor {

    private final KitService kitService;
    private final String kitName;
    private final long cooldown;
    private final ZestianKits plugin;

    public KitEditor(KitService kitService, String kitName, long cooldown, ZestianKits plugin) {
        this.kitService = kitService;
        this.kitName = kitName;
        this.cooldown = cooldown;
        this.plugin = plugin;
    }

    public void open(Player player) {
        Gui gui = Gui.normal()
                .setTitle("Editar Kit: " + kitName + " - CD: " + cooldown)
                .setStructure(getStructure())
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build()))
                .addIngredient('S', new ButtonItem(new ItemBuilder(Material.EMERALD_BLOCK)
                        .setDisplayName("§aGuardar Cambios").build(), "save", plugin))
                .addIngredient('C', new ButtonItem(new ItemBuilder(Material.BARRIER)
                        .setDisplayName("§cCancelar").build(), "cancel", plugin))
                .build();

        Inventory inv = gui.getInventory();

        Kit kit = kitService.getKit(kitName);
        if (kit != null) {
            ItemStack[] items = kit.getItems();
            int index = 0;
            for (int row = 0; row < getStructure().length; row++) {
                String rowStr = getStructure()[row].replace(" ", "");
                for (int col = 0; col < rowStr.length(); col++) {
                    if (rowStr.charAt(col) == '.') {
                        int slot = row * 9 + col;
                        if (index < items.length) {
                            ItemStack item = items[index++];
                            inv.setItem(slot, item != null ? item.clone() : new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
        player.openInventory(inv);
    }


    public void handleSave(Player player, Inventory inv) {
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

        ItemStack[] kitItems = new ItemStack[editableSlots.size()];
        for (int i = 0; i < editableSlots.size(); i++) {
            kitItems[i] = inv.getItem(editableSlots.get(i));
        }

        Kit kit = new Kit(kitName, kitItems, cooldown);
        if (kitService.updateKit(kitName, kit)) {
            player.sendMessage("§aKit editado exitosamente!");
        } else {
            player.sendMessage("§cError al editar el kit.");
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
