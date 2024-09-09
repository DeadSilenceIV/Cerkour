package me.cerdax.cerkour.profile;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.utils.InventoryUtils;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
        serialize();
    }

    public Profile(UUID uuid, int coins, int rankUp) {
        this.uuid = uuid;
        this.map = null;
        this.coins = coins;
        this.rankUp = rankUp;
        serialize();
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
        serialize();
    }

    public void joinMap(Map map, Player player) {
        if (map.getStartLocation() != null) {
            this.map = map;
            player.teleport(map.getStartLocation());
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            InventoryUtils.gameInventory(player);
        }
    }

    public void leaveMap(Player player) {
        if (this.map != null) {
            this.map = null;
            player.teleport(LocationUtils.getSpawn());
            player.getInventory().clear();
            InventoryUtils.lobbyInventory(player);
        }
    }

    public int getCoins() {
        return this.coins;
    }

    public void serialize() {
        CustomFiles.getCustomFile("profiles").set("profiles." + getUuid().toString() + ".coins", getCoins());
        CustomFiles.getCustomFile("profiles").set("profiles." + getUuid().toString() + ".rankup", getRankUp());
        CustomFiles.saveCustomFile("profiles");
    }
}
