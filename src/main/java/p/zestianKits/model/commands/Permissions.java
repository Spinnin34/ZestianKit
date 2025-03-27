package p.zestianKits.model.commands;

public enum Permissions {

    COMMAND_DEFUALT("use"),
    COMMAND_CREATE("admin.reload"),
    COMMAND_DELETE("admin.delete"),
    COMMAND_EDIT("admin.edit"),
    COMMAND_RELOAD("admin.reload"),
    COMMAND_SET_DEFAULT("admin.setdefaultkit");

    private final String perm;

    Permissions(String perm) {
        this.perm = perm;
    }

    public final String getPermission() {
        return "zestian.kits." + this.perm;
    }
}
