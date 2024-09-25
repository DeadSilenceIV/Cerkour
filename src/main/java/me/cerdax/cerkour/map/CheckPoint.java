package me.cerdax.cerkour.map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class CheckPoint {

    private final int index;
    private boolean ld;
    private Location from;
    private Location to;
    private List<String> players;
    private PotionEffectType effect;
    private int amplifier;

    public CheckPoint(int index) {
        this.index = index;
        this.ld = false;
        this.from = null;
        this.to = null;
        this.players = new ArrayList<>();
        this.effect = null;
        this.amplifier = 0;
    }

    public CheckPoint(int index, boolean ld, Location from, Location to, List<String> players, PotionEffectType effect, int amplifier) {
        this.index = index;
        this.ld = ld;
        this.from = from;
        this.to = to;
        this.players = players;
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

    public List<String> getPlayers() {
        return this.players;
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

    public void addPlayer(String player) {
        this.players.add(player);
    }

    public void removePlayer(String player) { this.players.remove(player); }
}
