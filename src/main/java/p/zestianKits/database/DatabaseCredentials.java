package p.zestianKits.database;

import p.zestianKits.ZestianKits;

public class DatabaseCredentials {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DatabaseCredentials(String jdbcUrl, String usernanme, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = usernanme;
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static DatabaseCredentials loadFromConfig(ZestianKits plugin) {
        String host = plugin.getConfig().getString("storage.mysql.host", "localhost");
        String database = plugin.getConfig().getString("storage.mysql.database", "default_db");
        String user = plugin.getConfig().getString("storage.mysql.user", "root");
        String password = plugin.getConfig().getString("storage.mysql.password", "root");
        String jdbcUrl = "jdbc:mysql://" + host + "/" + database + "?useSSL=false&serverTimezone=UTC";

        return new DatabaseCredentials(jdbcUrl, user, password);
    }
}
