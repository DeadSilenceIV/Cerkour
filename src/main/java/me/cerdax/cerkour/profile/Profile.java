package me.cerdax.cerkour.profile;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.TickTimer;
import me.cerdax.cerkour.utils.ActionBarUtils;
import me.cerdax.cerkour.utils.InventoryUtils;
import me.cerdax.cerkour.utils.LocationUtils;
import me.cerdax.cerkour.utils.TitleUtils;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

public class Profile {

    private final UUID uuid;
    private Map map;
    private int coins;
    private int rankUp;
    private Practice practice;
    private int points;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.map = null;
        this.coins = 0;
        this.rankUp = 1;
        this.practice = new Practice();
        this.points = 0;
    }

    public Profile(UUID uuid, int coins, int rankUp, int points) {
        this.uuid = uuid;
        this.map = null;
        this.coins = coins;
        this.rankUp = rankUp;
        this.practice = new Practice();
        this.points = points;
    }

    public void addPoints(int amount) {
        this.points += amount;
    }

    public void setPoints(int amount) {
        this.points = amount;
    }

    public int getPoints() {
        return this.points;
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

    public Practice getPractice() {
        return this.practice;
    }

    public void setRankUp(int rankUp) {
        this.rankUp = rankUp;
    }

    public void joinMap(Map map) {
        Player player = Bukkit.getPlayer(getUuid());
        if (map.getStartLocation() != null && map.getEndLocation() != null) {
            this.map = map;
            if ((map.getIsRankUp() && map.getRankUp() <= getRankUp()) || !map.getIsRankUp()) {
                if (map.getCheckPointLocation(player) != map.getStartLocation()) {
                    map.toggleTimer(true, player);
                }
                player.teleport(map.getCheckPointLocation(player));
                player.setGameMode(GameMode.ADVENTURE);
                player.setAllowFlight(false);
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

    public void leaveMap() {
        if (getMap() != null) {
            Player player = Bukkit.getPlayer(getUuid());
            if (getPractice().getIsEnabled()) {
                leavePractice();
            }
            if (getMap().getTimer(player).getIsRunning()) {
                getMap().toggleTimer(false, player);
            }
            this.map.serialize();
            this.map = null;
            for (PotionEffect p : player.getActivePotionEffects()) {
                player.removePotionEffect(p.getType());
            }
            player.teleport(LocationUtils.getSpawn());
            player.getInventory().clear();
            InventoryUtils.lobbyInventory(player);
            ActionBarUtils.sendActionbar(player, " ");
        }
    }

    public void enterPractice(Map map) {
        Player player = Bukkit.getPlayer(getUuid());
        TickTimer timer = map.getTimer(player);
        timer.setStashedTicks();
        player.setAllowFlight(true);
        player.setFlying(true);
        if (getMap() != map) {
            leaveMap();
            joinMap(map);
        }
        else if (getMap() == null) {
            joinMap(map);
        }
        getMap().toggleTimer(false, player);
        if (map.isOS()) {
            InventoryUtils.practiceInventoryOS(player);
        }
        else {
            InventoryUtils.practiceInventory(player);
        }
        getPractice().setIsEnabled(true);
        getPractice().setStartPoint(map.getCheckPointLocation(player));
        getPractice().setEndPoint(map.getEndLocation());
    }

    public void leavePractice() {
        Player player = Bukkit.getPlayer(getUuid());
        InventoryUtils.RemovePracticeInventory(player);
        getPractice().setStartPoint(null);
        getPractice().setEndPoint(null);
        getPractice().setIsEnabled(false);
        TickTimer timer = map.getTimer(player);
        timer.setTicks(timer.getStashedTicks());
        timer.clearStashedTicks();
        map.serialize();
        getMap().toggleTimer(true, player);
        for (PotionEffect p : player.getActivePotionEffects()) {
            player.removePotionEffect(p.getType());
        }
        World world = getMap().getCheckPointLocation(player).getWorld();
        double x = getMap().getCheckPointLocation(player).getX();
        double y = getMap().getCheckPointLocation(player).getY();
        double z = getMap().getCheckPointLocation(player).getZ();
        float yaw = getMap().getCheckPointLocation(player).getYaw();
        float pitch = player.getLocation().getPitch();
        Location loc = new Location(world, x, y, z, yaw, pitch); //So the interact event doesn't double reg

        player.teleport(loc);
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public int getCoins() {
        return this.coins;
    }

    public void serialize() {
        CustomFiles.getCustomFile("profiles").set("profiles." + getUuid().toString() + ".coins", getCoins());
        CustomFiles.getCustomFile("profiles").set("profiles." + getUuid().toString() + ".rankup", getRankUp());
        CustomFiles.getCustomFile("profiles").set("profiles." + getUuid().toString() + ".points", getPoints());
        CustomFiles.saveCustomFile("profiles");
    }
}
