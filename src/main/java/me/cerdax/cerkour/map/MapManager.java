package me.cerdax.cerkour.map;

import me.cerdax.cerkour.Cerkour;
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
        Bukkit.getScheduler().runTaskLater(Cerkour.getInstance(), () -> Cerkour.getInstance().getStorage().getMaps().thenAccept(maps -> this.maps.addAll(maps)), 5L);
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
            Cerkour.getInstance().getStorage().delete(removeMap);
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
        List<TickTimer> timers = new ArrayList<>(map.getTimers());
        timers.sort((t1, t2) -> Long.compare(t2.getBest(), t1.getBest()));
        StringBuilder result = new StringBuilder();
        Collections.reverse(timers);
        int placement = 1;
        if (!timers.isEmpty()) {
            for (TickTimer timer : timers) {
                if (timer.getBest() == 0) {
                    continue;
                }
                if (placement == 1) {
                    result.append(message);
                }
                if (placement == 11) {
                    break;
                }
                result.append("§e").append(placement).append(". ").append("§6").append(timer.getTimeFromTicks(timer.getBest())).append("§e - ").append("§6").append(Bukkit.getOfflinePlayer(timer.getPlayerUUID()).getName()).append("§e\n");
                placement++;
            }
            message = result.toString();
        }
        return message;
    }

    private Map getMapByUUID(UUID uuid) {
        return maps.stream()
                .filter(map -> map.getMapUUID().equals(uuid))
                .findFirst()
                .orElse(null);
    }

}
