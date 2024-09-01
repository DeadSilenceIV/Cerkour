package me.cerdax.cerkour.map;
;
import java.util.UUID;
import org.bukkit.Location;

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
    }

    public String getName() {
        return this.name;
    }

    public UUID getMapUUID() {
        return this.uuid;
    }

    public void setRankUp(int rankUp) {
        this.rankUp = rankUp;
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
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Location getEndLocation() {
        return this.endLocation;
    }
}
