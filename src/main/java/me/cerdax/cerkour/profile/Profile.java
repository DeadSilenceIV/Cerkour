package me.cerdax.cerkour.profile;

import me.cerdax.cerkour.map.Map;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {

    private final UUID uuid;
    private Map map;
    private int coins;
    private int rankUp;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.map = null;
        this.coins = 0;
        this.rankUp = 1;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Map getMap() {
        return this.map;
    }

    public int getRankUp() {
        return this.rankUp;
    }

    public void setRankUp(int rankUp) {
        this.rankUp = rankUp;
    }

    public void joinMap(Map map, Player player) {
        if (map.getStartLocation() != null) {
            this.map = map;
            player.teleport(map.getStartLocation());
        }
    }

    public void leaveMap() {
        if (this.map != null) {
            this.map = null;
        }
    }
}
