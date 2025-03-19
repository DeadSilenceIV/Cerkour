package me.cerdax.cerkour.map;

import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.Map;

@SerializableAs("CheckPoint")
public class CheckPoint implements ConfigurationSerializable {

    private final int index;
    private boolean ld;
    private Location from;
    private Location to;
    private List<UUID> playerUUIDs;
    private PotionEffectType effect;
    private int amplifier;

    public CheckPoint(int index) {
        this.index = index;
        this.ld = false;
        this.from = null;
        this.to = null;
        this.playerUUIDs = new ArrayList<>();
        this.effect = null;
        this.amplifier = 0;
    }

    public CheckPoint(int index, boolean ld, Location from, Location to, List<UUID> players, PotionEffectType effect, int amplifier) {
        this.index = index;
        this.ld = ld;
        this.from = from;
        this.to = to;
        this.playerUUIDs = players;
        this.effect = effect;
        this.amplifier = amplifier;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public PotionEffectType getEffectType() {
        return this.effect;
    }

    public void setEffect(PotionEffectType effect, int amplifier) {
        this.effect = effect;
        this.amplifier = amplifier;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isLd() {
        return this.ld;
    }

    public Location getFrom() {
        if (this.from != null) {
            World world = this.from.getWorld();
            int x = this.from.getBlockX();
            int y = this.from.getBlockY();
            int z = this.from.getBlockZ();
            return new Location(world, x, y, z);
        }
        return null;
    }

    public Location getTo() {
        if (this.to != null) {
            return this.to;
        }
        return null;
    }

    public List<UUID> getPlayerUUIDs() {
        return this.playerUUIDs;
    }

    public void setLd(boolean ld) {
        this.ld = ld;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public void addPlayer(UUID uuid) {
        this.playerUUIDs.add(uuid);
    }

    public void removePlayer(UUID uuid) { this.playerUUIDs.remove(uuid); }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("index", this.index);
        map.put("ld", this.ld);
        if (this.from != null) {
            map.put("from", LocationUtils.locationToString(this.from));
        }
        if (this.to != null) {
            map.put("to", LocationUtils.locationToString(this.to));
        }
        map.put("amplifier", this.amplifier);
        if (this.effect != null) {
            map.put("potion", this.effect.getName());
        }
        List<String> playerUUIDs = new ArrayList<>();
        for (UUID uuid : this.playerUUIDs) {
            playerUUIDs.add(uuid.toString());
        }
        map.put("players", playerUUIDs);
        return map;
    }

    public static CheckPoint deserialize(Map<String, Object> map) {
        int index = (int) map.get("index");
        boolean ld = (boolean) map.get("ld");
        Location from = map.get("from") != null ? LocationUtils.stringToLocation((String) map.get("from")) : null;
        Location to = map.get("to") != null ? LocationUtils.stringToLocation((String) map.get("to")) : null;
        int amplifier = (int) map.get("amplifier");
        String potionName = (String) map.get("potion");
        PotionEffectType potionType = null;
        if (potionName != null && !potionName.isEmpty()) {
            potionType = PotionEffectType.getByName(potionName);
        }
        List<String> playerUUIDs = (List<String>) map.get("players");
        List<UUID> players = new ArrayList<>();
        for (String p : playerUUIDs) {
            players.add(UUID.fromString(p));
        }
        return new CheckPoint(index, ld, from, to, players, potionType, amplifier);
    }
}
