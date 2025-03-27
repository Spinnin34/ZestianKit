package p.zestianKits.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import p.zestianKits.ZestianKits;
import p.zestianKits.model.Gui;
import p.zestianKits.model.Kit;
import p.zestianKits.model.item.ButtonItem;
import p.zestianKits.model.item.SimpleItem;
import p.zestianKits.model.item.ItemBuilder;
import p.zestianKits.service.KitService;
import p.zestianKits.utils.messages.LangUtil;
import org.bukkit.NamespacedKey;

public class KitView {

    private final KitService kitService;
    private final String kitName;
    private final ZestianKits plugin;
    private final NamespacedKey guiItemKey;

    public KitView(KitService kitService, String kitName, ZestianKits plugin) {
        this.kitService = kitService;
        this.kitName = kitName;
        this.plugin = plugin;
        this.guiItemKey = new NamespacedKey(plugin, "gui_item");
    }

    public void open(Player player) {
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage(LangUtil.getString(player, "messages.kit-not-exists", true));
            return;
        }

        Gui gui = Gui.normal()
                .setTitle("Kit: §x§F§B§0§8§3§9" + kitName)
                .setStructure(getStructure())
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build()))
                .addIngredient('X', new ButtonItem(new ItemBuilder(Material.BARRIER)
                        .setDisplayName("§f[§x§F§B§0§8§3§9✘§f] §7Volver").build(), "main", plugin))
                .build();

        Inventory inv = gui.getInventory();
        int index = 0;
        for (int row = 0; row < getStructure().length; row++) {
            String rowStr = getStructure()[row].replace(" ", "");
            for (int col = 0; col < rowStr.length(); col++) {
                if (rowStr.charAt(col) == '.') {
                    int slot = row * 9 + col;
                    if (index < kit.getItems().length) {
                        ItemStack item = kit.getItems()[index++];
                        if (item != null) {
                            item = item.clone();
                            setGuiItemTag(item);
                        } else {
                            item = new ItemStack(Material.AIR);
                        }
                        inv.setItem(slot, item);
                    }
                }
            }
        }
        player.openInventory(inv);
    }

    private void setGuiItemTag(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(guiItemKey, PersistentDataType.STRING, "true");
            item.setItemMeta(meta);
        }
    }

    public String[] getStructure() {
        return new String[] {
                "# # # # # # # # #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# # # # X # # # #"
        };
    }
}