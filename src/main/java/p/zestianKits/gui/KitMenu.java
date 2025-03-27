package p.zestianKits.gui;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import p.zestianKits.ZestianKits;
import p.zestianKits.model.Gui;
import p.zestianKits.model.Kit;
import p.zestianKits.model.item.ItemBuilder;
import p.zestianKits.model.item.SimpleItem;
import p.zestianKits.service.KitService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KitMenu {

    private final KitService kitService;
    private final ZestianKits plugin;

    public KitMenu(KitService kitService, ZestianKits plugin) {
        this.kitService = kitService;
        this.plugin = plugin;
    }

    public void open(Player player) {
        Gui gui = Gui.normal()
                .setTitle("Kits Disponibles")
                .setStructure(getStructure())
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build()))
                .build();
        Inventory inv = gui.getInventory();
        int slot = 10;

        // Obtenemos todos los kits y los ordenamos por ID (de menor a mayor)
        Collection<Kit> kits = kitService.getAllKits();
        List<Kit> sortedKits = kits.stream()
                .sorted(Comparator.comparingLong(Kit::getId))
                .collect(Collectors.toList());

        for (Kit kit : sortedKits) {
            if (slot > 16) break;
            inv.setItem(slot, createKitIcon(kit, player));
            slot++;
        }
        player.openInventory(inv);
    }

    private ItemStack createKitIcon(Kit kit, Player player) {
        boolean hasPermission = player.hasPermission(kit.getPermission());
        Material material = hasPermission ? Material.BOOK : Material.BARRIER;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = getItemMeta(kit, item, hasPermission);

        NamespacedKey kitIconKey = new NamespacedKey(plugin, "kit_icon");
        meta.getPersistentDataContainer().set(kitIconKey, PersistentDataType.STRING, kit.getName());

        NamespacedKey guiKey = new NamespacedKey(plugin, "gui_item");
        meta.getPersistentDataContainer().set(guiKey, PersistentDataType.STRING, "true");

        item.setItemMeta(meta);
        return item;
    }

    private static @NotNull ItemMeta getItemMeta(Kit kit, ItemStack item, boolean hasPermission) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName((hasPermission ? "§7" : "§x§F§F§1§2§4§C") + kit.getName());

        List<String> lore = new ArrayList<>();
        lore.add("§8Informacion");
        lore.add(" ");
        lore.add("§7Cooldown: §x§F§F§1§2§4§C" + kit.getCooldown() + " segundos");
        lore.add("§7Permiso requerido: §x§F§F§1§2§4§C" + kit.getPermission());
        lore.add("§7Estado: " + (hasPermission ? "§x§6§E§F§F§5§0✔ Tienes permiso" : "§x§F§F§1§2§4§C✖ No tienes permiso"));
        lore.add(" ");
        lore.add("§x§6§E§F§F§5§0| §7Haga clic izquierdo para abrir el reclamar el kit.");
        lore.add("§x§F§F§1§2§4§C| §7Haga clic derecho para abrir el ver el kit.");
        lore.add(" ");

        meta.setLore(lore);
        return meta;
    }

    public String[] getStructure() {
        return new String[] {
                "# # # # # # # # #",
                "# . . . . . . . #",
                "# # # # # # # # #"
        };
    }
}
