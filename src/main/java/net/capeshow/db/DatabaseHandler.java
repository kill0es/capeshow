
/**
 * 数据库处理类。
 * 负责管理披风数据的存储和查询。
 * 主要功能包括：
 * - 初始化SQLite数据库
 * - 更新玩家当前披风和历史记录
 * - 查询玩家披风历史
 */
package net.capeshow.db;

import net.fabricmc.loader.api.FabricLoader;
import net.capeshow.CapeShowMod;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

public class DatabaseHandler {
    private static Connection connection;
    
    public static void init() {
        try {
            Path dbPath = FabricLoader.getInstance().getConfigDir().resolve("capeshow.db");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS capes ("
                + "uuid TEXT PRIMARY KEY,"
                + "current_hash TEXT,"
                + "history TEXT)");
        } catch (SQLException e) {
            CapeShowMod.LOGGER.error("Database init failed: " + e.getMessage());
        }
    }
    
    public static void updateCape(UUID uuid, String hash) {
        try (PreparedStatement stmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO capes (uuid, current_hash, history) VALUES (?, ?, ?)"
        )) {
            String history = String.join(",", getHistory(uuid)) + "," + hash;
            stmt.setString(1, uuid.toString());
            stmt.setString(2, hash);
            stmt.setString(3, history);
            stmt.executeUpdate();
        } catch (SQLException e) {
            CapeShowMod.LOGGER.error("Failed to update cape: " + e.getMessage());
        }
    }
    
    public static List<String> getHistory(UUID uuid) {
        try (PreparedStatement stmt = connection.prepareStatement(
            "SELECT history FROM capes WHERE uuid = ?"
        )) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Arrays.asList(rs.getString("history").split(","));
            }
        } catch (SQLException e) {
            CapeShowMod.LOGGER.error("Failed to get history: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}