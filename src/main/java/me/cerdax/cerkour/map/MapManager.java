package me.cerdax.cerkour.map;

import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
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

    public String getLeaderboardString(Map map) {
        String message = "§eMap §6" + map.getName() + " §eLeaderboard:\n";
        if (map != null) {
            List<TickTimer> timers = new ArrayList<>(map.getTimers());
            timers.sort((t1, t2) -> Long.compare(t2.getBest(), t1.getBest()));
            StringBuilder result = new StringBuilder();
            Collections.reverse(timers);
            int placement = 1;
            if (!timers.isEmpty()) {
                for (int i = 0; i < timers.size(); i++) {
                    if (timers.get(i).getBest() == 0) {
                        continue;
                    }
                    if (placement == 1) {
                        result.append(message);
                    }
                    if (placement == 11) {
                        break;
                    }
                    result.append("§e").append(placement).append(". ").append("§6").append(timers.get(i).getTimeFromTicks(timers.get(i).getBest())).append("§e - ").append("§6").append(Bukkit.getOfflinePlayer(timers.get(i).getPlayerUUID()).getName()).append("§e\n");
                    placement++;
                }
                message = result.toString();
            }
        }
        return message;
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
                int state = mapSection.getInt("state");
                int difficulty = mapSection.getInt("difficulty");


                Location spawnLoc = strSpawn != null ? LocationUtils.stringToLocation(strSpawn) : null;
                Location endLoc = strEnd != null ? LocationUtils.stringToLocation(strEnd) : null;

                List<CheckPoint> checkPoints = new ArrayList<>();

                List<TickTimer> timers = new ArrayList<>();

                List<Material> deathBlocks = new ArrayList<>();

                ConfigurationSection checkPointSection = mapSection.getConfigurationSection("checkpoints");

                if (checkPointSection != null) {
                    for(String checkpointKey : checkPointSection.getKeys(false)){
                        ConfigurationSection checkpointSection = checkPointSection.getConfigurationSection(checkpointKey);

                        if (checkpointSection != null) {
                            int index = checkpointSection.getInt("index");
                            boolean ld = checkpointSection.getBoolean("ld");
                            String strLocFrom = checkpointSection.getString("from");
                            String strLocTo = checkpointSection.getString("to");
                            int potionAmplifier = checkpointSection.getInt("amplifier", 0);
                            String potionName = checkpointSection.getString("potion", "defaultPotion");

                            PotionEffectType potionType = null;

                            if (potionName != null && !potionName.isEmpty()) {
                                potionType = PotionEffectType.getByName(potionName);
                            }

                            Location fromLoc = strLocFrom != null ? LocationUtils.stringToLocation(strLocFrom) : null;
                            Location toLoc = strLocTo != null ? LocationUtils.stringToLocation(strLocTo) : null;

                            List<String> players = checkpointSection.getStringList("players");
                            List<UUID> playerUUIDs = new ArrayList<>();
                            for (String p : players) {
                                playerUUIDs.add(UUID.fromString(p));

                            }
                            checkPoints.add(new CheckPoint(index, ld, fromLoc, toLoc, playerUUIDs, potionType, potionAmplifier));
                        }
                    }
                }

                ConfigurationSection timerSections = mapSection.getConfigurationSection("timers");

                if (timerSections != null) {
                    for (String timerKey : timerSections.getKeys(false)) {
                        ConfigurationSection timerSection = timerSections.getConfigurationSection(timerKey);

                        if (timerSection != null) {
                            long ticks = timerSection.getLong("ticks");
                            long best = timerSection.getLong("best");
                            TickTimer playerTimer = new TickTimer(UUID.fromString(timerKey));
                            playerTimer.setTicks(ticks);
                            playerTimer.setBest(best);
                            timers.add(playerTimer);
                        }
                    }
                }

                List<String> blockNames = mapSection.getStringList("deathBlocks");
                for (String bName : blockNames) {
                    deathBlocks.add(Material.getMaterial(bName));
                }
                Map map = new Map(mapUUID, name, spawnLoc, endLoc, rankUp, checkPoints, timers, state, difficulty, deathBlocks);
                maps.add(map);
            }
        }
    }

    private Map getMapByUUID(UUID uuid) {
        return maps.stream()
                .filter(map -> map.getMapUUID().equals(uuid))
                .findFirst()
                .orElse(null);
    }

}
