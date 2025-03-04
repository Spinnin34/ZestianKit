package p.zestianKits.model;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Gui {
    private final Inventory inventory;

    public Gui(Inventory inventory) {
        this.inventory = inventory;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public static GuiBuilder normal() {
        return new GuiBuilder();
    }

    public Inventory getInventory() {
        return inventory;
    }
}

