package me.cerdax.cerkour.profile;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.map.CheckPoint;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.TickTimer;
import me.cerdax.cerkour.utils.ActionBarUtils;
import me.cerdax.cerkour.utils.InventoryUtils;
import me.cerdax.cerkour.utils.LocationUtils;
import me.cerdax.cerkour.utils.TitleUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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
        if (map.getStartLocation() != null && map.getEndLocation() != null) {
            this.map = map;
            if ((map.getIsRankUp() && map.getRankUp() <= getRankUp()) || !map.getIsRankUp()) {
                if (map.getCheckPointLocation(player) != map.getStartLocation()) {
                    map.toggleTimer(true, player);
                }
                player.teleport(map.getCheckPointLocation(player));
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                InventoryUtils.gameInventory(player);
                Audience audience = Cerkour.getInstance().getAdventure().player(player);
                audience.showTitle(TitleUtils.getTitleJoinMap(map));
            }
            else if (!map.getIsRankUp()) {
                if (map.getCheckPointLocation(player) != map.getStartLocation()) {
                    map.toggleTimer(true, player);
                }
                player.teleport(map.getCheckPointLocation(player));
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                InventoryUtils.gameInventory(player);
            }
            else {
                player.sendMessage("§6§lCerkour§e> You may not join this map, you need to be a higher rank!");
                this.map = null;
            }
        }
        else {
            player.sendMessage("§6§lCerkour§e> You may not join this map");
        }
    }

    public void leaveMap(Player player) {
        if (this.map != null) {
            if (map.getTimer(player).getIsRunning()) {
                map.toggleTimer(false, player);
            }
            this.map = null;
            player.teleport(LocationUtils.getSpawn());
            player.getInventory().clear();
            InventoryUtils.lobbyInventory(player);
            ActionBarUtils.sendActionbar(player, " ");
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
