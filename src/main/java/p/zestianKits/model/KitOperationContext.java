package p.zestianKits.model;

public class KitOperationContext {
    private final OperationType operationType;
    private final String kitName;
    private final long cooldown;

    public KitOperationContext(OperationType operationType, String kitName, long cooldown) {
        this.operationType = operationType;
        this.kitName = kitName;
        this.cooldown = cooldown;
    }

    public OperationType getOperationType() { return operationType; }
    public String getKitName() { return kitName; }
    public long getCooldown() { return cooldown; }
}