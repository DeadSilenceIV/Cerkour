package me.cerdax.cerkour.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomFiles {

    private static Map<String, FileConfiguration> customFiles = new HashMap<>();
    private static Map<String, File> files = new HashMap<>();

    public static void setup(String name) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("Cerkour").getDataFolder(), name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        customFiles.put(name, config);
        files.put(name, file);
    }

    public static FileConfiguration getCustomFile(String fileName) {
        FileConfiguration custom = customFiles.get(fileName.toLowerCase());
        if (custom == null) {
            setup(fileName);
        }
        return customFiles.get(fileName.toLowerCase());
    }

    public static boolean isInitialized(String fileName) {
        return customFiles.containsKey(fileName.toLowerCase());
    }

    public static void saveCustomFile(String fileName) {
        FileConfiguration config = getCustomFile(fileName);
        if (config != null) {
            try {
                config.save(files.get(fileName.toLowerCase()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveAllCustomFiles() {
        for (Map.Entry<String, FileConfiguration> entry : customFiles.entrySet()) {
            try {
                entry.getValue().save(files.get(entry.getKey()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void reloadCustomFile(String fileName) {
        if (isInitialized(fileName)) {
            File file = files.get(fileName.toLowerCase());
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            customFiles.put(fileName.toLowerCase(), config);
        }
    }

    public static void reloadAllCustomFiles() {
        for (Map.Entry<String, File> entry : files.entrySet()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(entry.getValue());
            customFiles.put(entry.getKey(), config);
        }
    }
}
