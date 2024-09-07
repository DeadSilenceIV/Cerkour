package me.cerdax.cerkour.map;

import java.util.Objects;
import java.util.UUID;

import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Map {

    private final UUID uuid;
    private Location startLocation;
    private Location endLocation;
    private final String name;
    private int rankUp;


    public Map(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.startLocation = null;
        this.endLocation = null;
        this.rankUp = 0;
        serialize();
    }

    public Map(UUID uuid, String name, Location spawnLocation, Location endLocation, int rankUp) {
        this.uuid = uuid;
        this.name = name;
        this.startLocation = spawnLocation;
        this.endLocation = endLocation;
        this.rankUp = rankUp;
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

    public void serialize() {
        FileConfiguration config = CustomFiles.getCustomFile("maps");
        if (config != null) {
            config.set("maps." + getMapUUID().toString(), null); // Clear existing data
            config.set("maps." + getMapUUID().toString() + ".name", getName());
            if (getStartLocation() != null) {
                config.set("maps." + getMapUUID().toString() + ".spawn", LocationUtils.locationToString(getStartLocation()));
            }
            if (getEndLocation() != null) {
                config.set("maps." + getMapUUID().toString() + ".end", LocationUtils.locationToString(getEndLocation()));
            }
            config.set("maps." + getMapUUID().toString() + ".rankup", getRankUp());
            CustomFiles.saveCustomFile("maps");
        }
    }
}
