package p.zestianKits.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import p.zestianKits.ZestianKits;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private final ZestianKits plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(ZestianKits plugin) {
        this.plugin = plugin;
        setupDataSource();
        createTables();
    }

    private void setupDataSource() {
        DatabaseCredentials credentials = DatabaseCredentials.loadFromConfig(plugin);
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(credentials.getJdbcUrl());
        config.setUsername(credentials.getUsername());
        config.setPassword(credentials.getPassword());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        dataSource = new HikariDataSource(config);
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS player_cooldowns (" +
                "uuid VARCHAR(36) NOT NULL," +
                "kit_name VARCHAR(255) NOT NULL," +
                "last_used TIMESTAMP NOT NULL," +
                "PRIMARY KEY (uuid, kit_name)" +
                ")";
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            plugin.getLogger().severe("Failed to create tables: " + ex.getMessage());
        }
    }

    public void updateLastUsed(UUID uuid, String kitName) throws SQLException {
        String sql = "INSERT INTO player_cooldowns (uuid, kit_name, last_used) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE last_used = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setString(1, uuid.toString());
            ps.setString(2, kitName);
            ps.setTimestamp(3, now);
            ps.setTimestamp(4, now);
            ps.executeUpdate();
        }
    }

    public Timestamp getLastUsed(UUID uuid, String kitName) throws SQLException {
        String sql = "SELECT last_used FROM player_cooldowns WHERE uuid = ? AND kit_name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, kitName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("last_used");
                }
            }
        }
        return null;
    }

    public void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
