package p.zestianKits.repository;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import p.zestianKits.model.Kit;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileKitRepository implements KitRepository {

    private final File kitsFolder;
    private final Map<String, Kit> kits = new HashMap<>();

    public FileKitRepository(File pluginFolder) {
        this.kitsFolder = new File(pluginFolder, "kits");
        if (!kitsFolder.exists()) {
            kitsFolder.mkdirs();
        }
        reload();
    }

    @Override
    public boolean saveKit(String name, Kit kit) {
        File file = new File(kitsFolder, name + ".yml");
        if (file.exists()) {
            return false;
        }
        YamlConfiguration config = new YamlConfiguration();
        config.set("id", kit.getId());
        config.set("cooldown", kit.getCooldown());
        config.set("items", Arrays.asList(kit.getItems()));
        config.set("permission", kit.getPermission());

        try {
            config.save(file);
            reload();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Kit getKit(String name) {
        return kits.get(name);
    }

    @Override
    public Collection<Kit> getAllKits() {
        return kits.values().stream()
                .sorted(Comparator.comparingLong(Kit::getId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteKit(String name) {
        File file = new File(kitsFolder, name + ".yml");
        if (file.delete()) {
            reload();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateKit(String name, Kit kit) {
        File file = new File(kitsFolder, name + ".yml");
        if (!file.exists()) {
            return false;
        }
        YamlConfiguration config = new YamlConfiguration();
        config.set("id", kit.getId());
        config.set("cooldown", kit.getCooldown());
        config.set("items", Arrays.asList(kit.getItems()));
        config.set("permission", kit.getPermission());

        try {
            config.save(file);
            reload();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void reload() {
        kits.clear();
        File[] files = kitsFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;
        for (File file : files) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String kitName = file.getName().replace(".yml", "");
            String permission = config.getString("permission", "zestiankits.kit." + kitName);
            long cooldown = config.getLong("cooldown", 0);
            long id = config.getLong("id", 0);
            List<ItemStack> items = (List<ItemStack>) config.getList("items");
            kits.put(kitName, new Kit(id, kitName, items != null ? items.toArray(new ItemStack[0]) : new ItemStack[0], cooldown, permission));
        }
    }
}
