package me.cerdax.cerkour.database;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseManager {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    public DatabaseManager(FileConfiguration config) {
        host = config.getString("mysql-data.host");
        port = config.getInt("mysql-data.port");
        database = config.getString("mysql-data.database");

        this.username = config.getString("mysql-data.username");
        this.password = config.getString("mysql-data.password");

        connect();
    }

    private void connect() {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}