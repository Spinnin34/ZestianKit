package p.zestianKits.service.editor;

import java.util.*;

public class KitSessionManager {
    private final Map<UUID, KitSession> sessions = new HashMap<>();

    public void createSession(UUID playerId, KitSession session) {
        sessions.put(playerId, session);
    }

    public Optional<KitSession> getSession(UUID playerId) {
        return Optional.ofNullable(sessions.get(playerId));
    }

    public void removeSession(UUID playerId) {
        sessions.remove(playerId);
    }
}
