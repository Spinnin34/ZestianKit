package p.zestianKits.service;

import p.zestianKits.model.Kit;
import p.zestianKits.repository.KitRepository;

import java.util.Collection;

public class KitService {

    private final KitRepository kitRepository;

    public KitService(KitRepository kitRepository) {
        this.kitRepository = kitRepository;
    }

    public boolean createKit(String name, Kit kit) {
        return kitRepository.saveKit(name, kit);
    }

    public boolean updateKit(String name, Kit kit) {
        return kitRepository.updateKit(name, kit);
    }

    public Kit getKit(String name) {
        return kitRepository.getKit(name);
    }

    public Collection<Kit> getAllKits() {
        return kitRepository.getAllKits();
    }

    public boolean deleteKit(String name) {
        return kitRepository.deleteKit(name);
    }

    public void reloadKits() {
        kitRepository.reload();
    }
}
