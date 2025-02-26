package me.cerdax.cerkour.map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckPoint {

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
}
