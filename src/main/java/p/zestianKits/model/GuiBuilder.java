package p.zestianKits.model;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import p.zestianKits.model.item.GuiItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiBuilder {
    private final List<String> structure = new ArrayList<>();
    private final Map<Character, GuiItem> ingredients = new HashMap<>();

    private String title = "Custom GUI";

    public GuiBuilder setStructure(String... rows) {
        structure.clear();
        for (String row : rows) {

            structure.add(row.replace(" ", ""));
        }
        return this;
    }

    public GuiBuilder addIngredient(char symbol, GuiItem item) {
        ingredients.put(symbol, item);
        return this;
    }

    public GuiBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public Gui build() {
        if (structure.isEmpty()) {
            throw new IllegalStateException("No se ha definido la estructura de la GUI.");
        }
        int rowsCount = structure.size();
        int columns = structure.get(0).length();
        int size = rowsCount * columns;
        Inventory inv = Bukkit.createInventory(null, size, title);

        for (int row = 0; row < rowsCount; row++) {
            String line = structure.get(row);
            if (line.length() != columns) {
                throw new IllegalStateException("Todas las filas deben tener el mismo número de caracteres.");
            }
            for (int col = 0; col < columns; col++) {
                int slot = row * columns + col;
                char symbol = line.charAt(col);

                if (ingredients.containsKey(symbol)) {
                    inv.setItem(slot, ingredients.get(symbol).getItemStack());
                } else {
                    inv.setItem(slot, new ItemStack(Material.AIR));
                }
            }
        }
        return new Gui(inv);
    }
}

