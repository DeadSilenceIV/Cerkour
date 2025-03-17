package me.cerdax.cerkour.profile;

import me.cerdax.cerkour.database.DatabaseManager;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    private final List<Profile> profiles;
    private final DatabaseManager databaseManager;

    public ProfileManager(DatabaseManager databaseManager) {
        this.profiles = new ArrayList<>();
        this.databaseManager = databaseManager;
        createTable();
        loadProfiles();
    }

    public Profile getProfile(UUID uuid) {


        return this.profiles.stream()
                .filter(p -> p.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS profiles (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "coins INT DEFAULT 0, " +
                "rankup INT DEFAULT 1, " +
                "points INT DEFAULT 0" +
                ");";

        try (Connection connection = databaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[ProfileManager] Error creating profile table: " + e.getMessage());
        }
    }

    private void loadProfiles() {
        String query = "SELECT * FROM profiles;";

        try (Connection connection = databaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                int coins = resultSet.getInt("coins");
                int rankUp = resultSet.getInt("rankup");
                int points = resultSet.getInt("points");

                Profile profile = new Profile(uuid, coins, rankUp, points);
                profiles.add(profile);
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe("[ProfileManager] Error loading profile: " + e.getMessage());
        }
    }

    public void saveAll(){
        for(Profile profiles : profiles){
            saveProfile(profiles);
        }
    }

    public void saveProfile(Profile profile) {
        String query = "INSERT INTO profiles (uuid, coins, rankup, points) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE coins = VALUES(coins), rankup = VALUES(rankup), points = VALUES(points);";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, profile.getUuid().toString());
            statement.setInt(2, profile.getCoins());
            statement.setInt(3, profile.getRankUp());
            statement.setInt(4, profile.getPoints());

            statement.executeUpdate();

        } catch (SQLException e) {
            Bukkit.getLogger().severe("[ProfileManager] Error saving profile: " + e.getMessage());
        }
        if(!profiles.contains(profile)){
            profiles.add(profile);
        }
    }
}
