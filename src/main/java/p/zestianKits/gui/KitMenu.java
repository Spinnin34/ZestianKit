package p.zestianKits.gui;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import p.zestianKits.ZestianKits;
import p.zestianKits.model.Gui;
import p.zestianKits.model.Kit;
import p.zestianKits.model.item.ItemBuilder;
import p.zestianKits.model.item.SimpleItem;
import p.zestianKits.service.KitService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KitMenu {

    private final KitService kitService;
    private final ZestianKits plugin;

    public KitMenu(KitService kitService, ZestianKits plugin) {
        this.kitService = kitService;
        this.plugin = plugin;
    }

    public void open(Player player) {
        // Definimos una estructura de 3 filas x 9 columnas:
        // Fila 0 y 2: borde, Fila 1: área para los íconos de los kits
        Gui gui = Gui.normal()
                .setTitle("Kits Disponibles")
                .setStructure(getStructure())
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build()))
                .build();
        Inventory inv = gui.getInventory();
        int slot = 10; // fila 1 empieza en slot 9
        Collection<Kit> kits = kitService.getAllKits();
        for (Kit kit : kits) {
            if (slot > 16) break; // máximo 9 kits en la fila central
            inv.setItem(slot, createKitIcon(kit));
            slot++;
        }
        player.openInventory(inv);
    }

    private ItemStack createKitIcon(Kit kit) {
        ItemStack item;
        /* if (kit.getItems().length > 0 && kit.getItems()[0] != null) {
            item = kit.getItems()[0].clone();
        } else {
            item = new ItemStack(Material.CHEST);
        } */

        item = new ItemStack(Material.BOOK);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a" + kit.getName());
        List<String> lore = new ArrayList<>();
        lore.add("§7Cooldown: §e" + kit.getCooldown() + " segundos");
        lore.add("§eClick para reclamar");
        meta.setLore(lore);

        NamespacedKey kitIconKey = new NamespacedKey(plugin, "kit_icon");
        meta.getPersistentDataContainer().set(kitIconKey, PersistentDataType.STRING, kit.getName());

        NamespacedKey guiKey = new NamespacedKey(plugin, "gui_item");
        meta.getPersistentDataContainer().set(guiKey, PersistentDataType.STRING, "true");
        item.setItemMeta(meta);
        return item;
    }

    public String[] getStructure() {
        String[] structure = {
                "# # # # # # # # #",
                "# . . . . . . . #",
                "# # # # # # # # #"
        };
        return structure;
    }
}
