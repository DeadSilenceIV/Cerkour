package me.cerdax.cerkour.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.utils.InventoryUtils;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Map {

    private final UUID uuid;
    private Location startLocation;
    private Location endLocation;
    private final String name;
    private int rankUp;
    private List<CheckPoint> checkpoints;

    public Map(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.startLocation = null;
        this.endLocation = null;
        this.rankUp = 0;
        this.checkpoints = new ArrayList<>();
        serialize();
    }

    public Map(UUID uuid, String name, Location spawnLocation, Location endLocation, int rankUp, List<CheckPoint> checkpoints) {
        this.uuid = uuid;
        this.name = name;
        this.startLocation = spawnLocation;
        this.endLocation = endLocation;
        this.rankUp = rankUp;
        this.checkpoints = checkpoints;
    }

    public String getName() {
        return this.name;
    }

    public UUID getMapUUID() {
        return this.uuid;
    }

    public void setRankUp(int rankUp) {
        this.rankUp = rankUp;
        serialize();
    }

    public boolean getIsRankUp() {
        if (this.rankUp == 0) {
            return false;
        }
        return true;
    }

    public int getRankUp() {
        return this.rankUp;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
        serialize();
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
        serialize();
    }

    public Location getEndLocation() {
        return this.endLocation;
    }

    public List<CheckPoint> getCheckpoints() {
        return this.checkpoints;
    }

    public CheckPoint getCheckPoint(int index) {
        for (CheckPoint cp : this.checkpoints) {
            if (cp.getIndex() == index) {
                return cp;
            }
        }
        return null;
    }

    public void addCheckPoint(int index) {
        this.checkpoints.add(new CheckPoint(index));
        serialize();
    }

    public void teleportToCheckPoint(Player player) {
        boolean exist = false;
        for (CheckPoint c : getCheckpoints()) {
            if (c.getPlayers().contains(player.getName())) {
                player.teleport(c.getTo());
                exist = true;
                break;
            }
        }
        if (!exist) {
            player.teleport(getStartLocation());
        }
    }

    private boolean doesMapExist() {
        FileConfiguration config = CustomFiles.getCustomFile("maps");
        return config.contains("maps." + getMapUUID().toString());
    }

    public void serialize() {
        FileConfiguration config = CustomFiles.getCustomFile("maps");
        if (config != null && !doesMapExist()) {
            config.createSection("maps." + getMapUUID().toString());
        }
        config.set("maps." + getMapUUID().toString() + ".name", getName());
        if (getStartLocation() != null) {
            config.set("maps." + getMapUUID().toString() + ".spawn", LocationUtils.locationToString(getStartLocation()));
        }
        if (getEndLocation() != null) {
            config.set("maps." + getMapUUID().toString() + ".end", LocationUtils.locationToString(getEndLocation()));
        }
        if (getCheckpoints() != null && !getCheckpoints().isEmpty()) {
            config.createSection("maps." + getMapUUID().toString() + ".checkpoints");
            for (CheckPoint c : getCheckpoints()) {
                String checkpointPath = "maps." + getMapUUID().toString() + ".checkpoints.checkpoint" + c.getIndex();
                config.set(checkpointPath + ".index", c.getIndex());
                config.set(checkpointPath + ".ld", c.isLd());
                if (c.getFrom() != null) {
                    config.set(checkpointPath + ".from", LocationUtils.locationToString(c.getFrom()));
                }
                if (c.getTo() != null) {
                    config.set(checkpointPath + ".to", LocationUtils.locationToString(c.getTo()));
                }
                List<String> playerNames = c.getPlayers();
                config.set(checkpointPath + ".players", playerNames);
            }
        }
        config.set("maps." + getMapUUID().toString() + ".rankup", getRankUp());
        CustomFiles.saveCustomFile("maps");
    }

}
