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
    // Lista de filas que representan la estructura
    private final List<String> structure = new ArrayList<>();
    // Mapa de ingredientes: símbolo -> GuiItem
    private final Map<Character, GuiItem> ingredients = new HashMap<>();
    // Título de la GUI (puedes agregar un método para modificarlo si lo deseas)
    private String title = "Custom GUI";

    public GuiBuilder setStructure(String... rows) {
        structure.clear();
        for (String row : rows) {
            // Se eliminan espacios para trabajar con un string continuo
            structure.add(row.replace(" ", ""));
        }
        return this;
    }

    /**
     * Asigna un ingrediente a un símbolo dado.
     * Por ejemplo, puedes mapear el símbolo '#' a un SimpleItem con un ítem de vidrio negro.
     */
    public GuiBuilder addIngredient(char symbol, GuiItem item) {
        ingredients.put(symbol, item);
        return this;
    }

    /**
     * Permite modificar el título de la GUI.
     */
    public GuiBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Construye la GUI (Inventory) basado en la estructura y los ingredientes definidos.
     */
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
            // Verifica que todas las filas tengan la misma cantidad de columnas
            if (line.length() != columns) {
                throw new IllegalStateException("Todas las filas deben tener el mismo número de caracteres.");
            }
            for (int col = 0; col < columns; col++) {
                int slot = row * columns + col;
                char symbol = line.charAt(col);
                // Si el símbolo está mapeado, coloca el ítem; de lo contrario, deja el slot vacío (AIR)
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

