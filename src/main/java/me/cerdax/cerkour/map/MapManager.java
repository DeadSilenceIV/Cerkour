package me.cerdax.cerkour.map;

import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapManager {

    private List<Map> maps;

    public MapManager() {
        this.maps = new ArrayList<>();
        deserialize();
    }

    public Map createMap(String name) {
        Map map = new Map(name);
        this.maps.add(map);
        return map;
    }

    public void removeMap(String name) {
        maps.removeIf(map -> map.getName().equalsIgnoreCase(name));
    }

    public Map getMapByName(String name) {
        return this.maps.stream().filter(map -> map.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public List<Map> getAllMaps() {
        return this.maps;
    }

    public void deserialize() {
        FileConfiguration config = CustomFiles.getCustomFile("maps");

        if (config == null) {
            Bukkit.broadcastMessage("NULL");
            CustomFiles.setup("maps");
            return;
        }

        if (!config.contains("maps")) {
            config.createSection("maps");
        }

        ConfigurationSection section = config.getConfigurationSection("maps");

        if (section == null) {
            return;
        }

        for (String uuid : section.getKeys(false)) {
            ConfigurationSection mapSection = config.getConfigurationSection("maps." + uuid);

            if (mapSection != null) {
                UUID mapUUID = UUID.fromString(uuid);
                String name = mapSection.getString("name");
                String strSpawn = mapSection.getString("spawn");
                String strEnd = mapSection.getString("end");
                int rankUp = mapSection.getInt("rankup");

                Location spawnLoc = LocationUtils.stringToLocation(strSpawn);
                Location endLoc = LocationUtils.stringToLocation(strEnd);

                Map map = new Map(mapUUID, name, spawnLoc, endLoc, rankUp);
                maps.add(map);
            }
        }
        CustomFiles.saveCustomFile("maps");
    }


    public List<Map> getMaps() {
        return this.maps;
    }

    private Map getMapByUUID(UUID uuid) {
        return maps.stream()
                .filter(map -> map.getMapUUID().equals(uuid))
                .findFirst()
                .orElse(null);
    }

}
