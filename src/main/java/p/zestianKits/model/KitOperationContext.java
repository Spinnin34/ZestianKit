package p.zestianKits.model;

public class KitOperationContext {
    private final OperationType operationType;
    private final String kitName;
    private final long cooldown;
    private final String permission;
    private final long id;

    public KitOperationContext(OperationType operationType, String kitName, long cooldown, String permission, long id) {
        this.operationType = operationType;
        this.kitName = kitName;
        this.cooldown = cooldown;
        this.permission = permission;
        this.id = id;
    }

    public OperationType getOperationType() { return operationType; }
    public String getKitName() { return kitName; }
    public long getCooldown() { return cooldown; }
    public String getPermission() { return permission; }
    public long getId() { return id; }
}