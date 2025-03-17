package me.cerdax.cerkour.map;

import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Map {

    private final UUID uuid;
    private Location startLocation;
    private Location endLocation;
    private final String name;
    private int rankUp;
    private List<CheckPoint> checkpoints;
    private List<TickTimer> timers;
    private int state; //1 = OS, 2 = Speedrun
    private int difficulty;
    private List<Material> deathBlocks;

    public Map(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.startLocation = null;
        this.endLocation = null;
        this.rankUp = 0;
        this.checkpoints = new ArrayList<>();
        this.timers = new ArrayList<>();
        this.state = 1;
        this.difficulty = 1;
        this.deathBlocks = new ArrayList<>();
        serialize();
    }

    public Map(UUID uuid, String name, Location spawnLocation, Location endLocation, int rankUp, List<CheckPoint> checkpoints, List<TickTimer> timers, int state, int difficulty, List<Material> deathBlocks) {
        this.uuid = uuid;
        this.name = name;
        this.startLocation = spawnLocation;
        this.endLocation = endLocation;
        this.rankUp = rankUp;
        this.checkpoints = checkpoints;
        this.timers = timers;
        this.state = state;
        this.difficulty = difficulty;
        this.deathBlocks = deathBlocks;
    }

    public void toggleTimer(boolean toggle, Player player) {
        TickTimer playerTimer = getTimer(player);
        if (toggle) {
            for (TickTimer timer : getTimers()) {
                if (timer.equals(playerTimer)) {
                    timer.start(player);
                    return;
                }
            }
        } else {
            for (TickTimer timer : getTimers()) {
                if (timer.equals(playerTimer)) {
                    timer.stop(player);
                    return;
                }
            }
        }
    }

    public List<Material> getDeathBlocks() {
        return this.deathBlocks;
    }

    public void addDeathBlock(Material block) {
        if (!getDeathBlocks().contains(block)) {
            this.deathBlocks.add(block);
        }
        serialize();
    }

    public void removeDeathBlocks(Material block) {
        this.deathBlocks.removeIf(db -> db.equals(block));
        serialize();
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        serialize();
    }

    public List<TickTimer> getTimers() {
        return this.timers;
    }

    public TickTimer getTimer(Player player) {
        if (this.timers != null) {
            for (TickTimer timer : getTimers()) {
                if (timer.getPlayerUUID().equals(player.getUniqueId())) {
                    return timer;
                }
            }
        }
        else {
            this.timers = new ArrayList<>();
        }
        TickTimer timer = new TickTimer(player.getUniqueId());
        this.timers.add(timer);
        return timer;
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

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
        serialize();
    }

    public boolean isOS() {
        return this.state == 1;
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

    public Location getCheckPointLocation(Player player) {
        for (CheckPoint c : getCheckpoints()) {
            if (c.getPlayerUUIDs().contains(player.getUniqueId())) {
                return c.getTo();
            }
        }
        return getStartLocation();
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
                config.set(checkpointPath + ".amplifier", c.getAmplifier());
                if (c.getEffectType() != null) {
                    config.set(checkpointPath + ".potion", c.getEffectType().getName());
                }
                List<UUID> playerUUIDs = c.getPlayerUUIDs();
                List<String> playerUUIDString = new ArrayList<>();
                for (UUID uuid : playerUUIDs) {
                    playerUUIDString.add(uuid.toString());
                }
                config.set(checkpointPath + ".players", playerUUIDString);
            }
        }
        if (getDeathBlocks() != null && !getDeathBlocks().isEmpty()) {
            List<String> blockNames = new ArrayList<>();
            for (Material db : getDeathBlocks()) {
                blockNames.add(db.name());
            }
            config.set("maps." + getMapUUID() + ".deathBlocks", blockNames);
        }
        if (getTimers() != null && !getTimers().isEmpty()) {
            config.createSection("maps." + getMapUUID().toString() + ".timers");
            for (TickTimer timer : getTimers()) {
                String timerPath = "maps." + getMapUUID().toString() + ".timers." + timer.getPlayerUUID().toString();
                config.set(timerPath + ".ticks", timer.getTicks());
                config.set(timerPath + ".best", timer.getBest());
            }
        }
        config.set("maps." + getMapUUID().toString() + ".rankup", getRankUp());
        config.set("maps." + getMapUUID().toString() + ".state", getState());
        config.set("maps." + getMapUUID().toString() + ".difficulty", getDifficulty());
        CustomFiles.saveCustomFile("maps");
    }

}
