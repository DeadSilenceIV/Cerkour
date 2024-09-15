package me.cerdax.cerkour.map;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
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
        Map removeMap = this.maps.stream().filter(map -> map.getName().equalsIgnoreCase(name)).findAny().orElse(null);
        this.maps.removeIf(map -> map.getName().equalsIgnoreCase(name));
        if (removeMap != null) {
            CustomFiles.getCustomFile("maps").set("maps." + removeMap.getMapUUID().toString(), null);
            CustomFiles.saveCustomFile("maps");
        }
    }

    public Map getMapByName(String name) {
        return this.maps.stream().filter(map -> map.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public Map getMapByRankUp(int rankUp) {
        for (Map map : this.maps) {
            if (map.getRankUp() == rankUp) {
                return map;
            }
        }
        return null;
    }

    public List<Map> getAllMaps() {
        return this.maps;
    }

    public List<Map> getAllRankUpMaps() {
        List<Map> rankUpMaps = new ArrayList<>();
        for (Map map : this.maps) {
            if  (map.getIsRankUp()) {
                rankUpMaps.add(map);
            }
        }
        return rankUpMaps;
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
            CustomFiles.saveCustomFile("maps");
        }

        ConfigurationSection section = config.getConfigurationSection("maps");

        if (section == null) {
            Bukkit.getLogger().warning("No 'maps' section found in the configuration file.");
            return;
        }

        for (String uuidStr : section.getKeys(false)) {
            ConfigurationSection mapSection = config.getConfigurationSection("maps." + uuidStr);

            if (mapSection != null) {
                UUID mapUUID = UUID.fromString(uuidStr);
                String name = mapSection.getString("name");
                String strSpawn = mapSection.getString("spawn");
                String strEnd = mapSection.getString("end");
                int rankUp = mapSection.getInt("rankup", 0);

                Location spawnLoc = strSpawn != null ? LocationUtils.stringToLocation(strSpawn) : null;
                Location endLoc = strEnd != null ? LocationUtils.stringToLocation(strEnd) : null;

                List<CheckPoint> checkPoints = new ArrayList<>();

                ConfigurationSection checkPointSection = mapSection.getConfigurationSection("checkpoints");

                if (checkPointSection != null) {
                    for(String checkpointKey : checkPointSection.getKeys(false)){
                        ConfigurationSection checkpointSection = checkPointSection.getConfigurationSection(checkpointKey);

                        if (checkpointSection != null) {
                            int index = checkpointSection.getInt("index");
                            boolean ld = checkpointSection.getBoolean("ld");
                            String strLocFrom = checkpointSection.getString("from");
                            String strLocTo = checkpointSection.getString("to");

                            Location fromLoc = strLocFrom != null ? LocationUtils.stringToLocation(strLocFrom) : null;
                            Location toLoc = strLocTo != null ? LocationUtils.stringToLocation(strLocTo) : null;

                            List<String> players = checkpointSection.getStringList("players");

                            checkPoints.add(new CheckPoint(index, ld, fromLoc, toLoc, players));
                        }
                    }
                }
                Map map = new Map(mapUUID, name, spawnLoc, endLoc, rankUp, checkPoints);
                maps.add(map);
            }
        }
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
