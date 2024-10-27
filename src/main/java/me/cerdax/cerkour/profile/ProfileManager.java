package me.cerdax.cerkour.profile;

import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    List<Profile> profiles;

    public ProfileManager() {
        this.profiles = new ArrayList<>();
        deserialize();
    }

    public Profile getProfile(UUID uuid) {
        Profile profile = this.profiles.stream().filter(profile1 -> profile1.getUuid().toString().equals(uuid.toString())).findAny().orElse(null);
        if (profile == null) {
            profile = new Profile(uuid);
            this.profiles.add(profile);
        }
        return profile;
    }

    public void deserialize() {
        if (!CustomFiles.isInitialized("profiles")) {
            CustomFiles.setup("profiles");
        }

        FileConfiguration config = CustomFiles.getCustomFile("profiles");

        if (config == null) {
            Bukkit.getLogger().severe("Failed to load profiles configuration file.");
            return;
        }

        if (!config.contains("profiles")) {
            config.createSection("profiles");
            CustomFiles.saveCustomFile("profiles");
        }

        ConfigurationSection section = config.getConfigurationSection("profiles");

        if (section == null) {
            Bukkit.getLogger().severe("No 'profiles' section found in the configuration file.");
            return;
        }

        for (String uuidStr : section.getKeys(false)) {
            ConfigurationSection profileSection = section.getConfigurationSection(uuidStr);
            if (profileSection != null) {
                UUID uuid = UUID.fromString(uuidStr);
                int coins = profileSection.getInt("coins", 0);
                int rankUp = profileSection.getInt("rankup", 1);
                int points = profileSection.getInt("points", 0);
                Profile profile = new Profile(uuid, coins, rankUp, points);
                profiles.add(profile);
            }
        }
    }
}
