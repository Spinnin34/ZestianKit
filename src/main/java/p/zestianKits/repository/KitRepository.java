package p.zestianKits.repository;

import p.zestianKits.model.Kit;
import java.util.Collection;

public interface KitRepository {
    boolean saveKit(String name, Kit kit);
    Kit getKit(String name);
    Collection<Kit> getAllKits();
    boolean deleteKit(String name);
    void reload();
    boolean updateKit(String name, Kit kit);
}

