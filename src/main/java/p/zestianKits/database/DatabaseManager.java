package p.zestianKits.database;

import p.zestianKits.ZestianKits;
import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private final ZestianKits plugin;
    private Connection connection;

    public DatabaseManager(ZestianKits plugin) {
        this.plugin = plugin;
        openConnection();
        createTables();
    }

    private void openConnection() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdirs();
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + new File(dataFolder, "kits.db").getAbsolutePath()
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS player_cooldowns (" +
                "uuid TEXT NOT NULL," +
                "kit_name TEXT NOT NULL," +
                "last_used TIMESTAMP NOT NULL," +
                "PRIMARY KEY (uuid, kit_name))";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateLastUsed(UUID uuid, String kitName) throws SQLException {
        String sql = "INSERT OR REPLACE INTO player_cooldowns (uuid, kit_name, last_used) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, kitName);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    public Timestamp getLastUsed(UUID uuid, String kitName) throws SQLException {
        String sql = "SELECT last_used FROM player_cooldowns WHERE uuid = ? AND kit_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, kitName);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getTimestamp("last_used") : null;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
