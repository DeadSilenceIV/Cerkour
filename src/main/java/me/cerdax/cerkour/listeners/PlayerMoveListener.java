package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.CheckPoint;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.TickTimer;
import me.cerdax.cerkour.profile.Practice;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.PointsUtil;
import me.cerdax.cerkour.utils.RankUtils;
import me.cerdax.cerkour.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location to = e.getTo();
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Map map = profile.getMap();
        Practice practice = profile.getPractice();
        Location locUnder = player.getLocation().subtract(0, 1, 0);

        int x = to.getBlockX();
        int y = to.getBlockY();
        int z = to.getBlockZ();

        if (map != null) {
            handleCheckpoints(player, map, practice, x, y, z);
            handleMapTimer(player, map, profile, locUnder, x, y, z);
        }
    }

    private void handleCheckpoints(Player player, Map map, Practice practice, int x, int y, int z) {
        for (CheckPoint c : map.getCheckpoints()) {
            if (c.getFrom().getBlockX() == x && c.getFrom().getBlockY() == y && c.getFrom().getBlockZ() == z) {
                if (!c.getPlayerUUIDs().contains(player.getUniqueId())) {
                    applyCheckpointEffects(player, c, practice,map);
                }
                break;
            }
        }
    }

    private void applyCheckpointEffects(Player player, CheckPoint c, Practice practice,Map map) {
        player.getActivePotionEffects().forEach(p -> player.removePotionEffect(p.getType()));
        if (c.getEffectType() != null) {
            player.addPotionEffect(new PotionEffect(c.getEffectType(), Integer.MAX_VALUE, c.getAmplifier() - 1));
        } else if (!practice.getIsEnabled()) {
            map.getCheckpoints().forEach(cp -> cp.getPlayerUUIDs().remove(player.getUniqueId()));
            c.addPlayer(player.getUniqueId());
            player.sendMessage("§6§lCerkour§e> You reached the checkpoint!");
        } else {
            practice.setCheckPoint(c);
        }
    }

    private void handleMapTimer(Player player, Map map, Profile profile, Location locUnder, int x, int y, int z) {
        if (!profile.getPractice().getIsEnabled()) {
            startMapTimer(player, map);
            handleDeathBlocks(player, map, locUnder);
            handleEndLocation(player, map, profile, x, y, z);
        } else {
            handlePracticeMode(player, map, profile, x, y, z);
        }
    }

    private void startMapTimer(Player player, Map map) {
        if (!map.getTimer(player).getIsRunning() && !player.getLocation().equals(map.getStartLocation())) {
            map.toggleTimer(true, player);
        }
    }

    private void handleDeathBlocks(Player player, Map map, Location locUnder) {
        if (map.getDeathBlocks().contains(locUnder.getBlock().getType())) {
            player.teleport(map.getCheckPointLocation(player));
        }
    }

    private void handleEndLocation(Player player, Map map, Profile profile, int x, int y, int z) {
        Location endLoc = map.getEndLocation();
        if (endLoc.getBlockX() == x && endLoc.getBlockZ() == z && endLoc.getBlockY() <= y && endLoc.getBlockY() <= y + 1.25) {
            map.toggleTimer(false, player);
            map.getCheckpoints().forEach(c -> c.removePlayer(player.getUniqueId()));
            handlePersonalBest(player, map, profile);
            handleRankUp(player, map, profile);
            profile.leaveMap();
            map.getTimer(player).resetTimer();
        }
    }

    private void handlePersonalBest(Player player, Map map, Profile profile) {
        if (map.getTimer(player).getBest() > map.getTimer(player).getTicks()) {
            player.sendMessage("§6§lCerkour§e> You beat the map: §6" + profile.getMap().getName() + " §ein §6" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getTicks()) + "§e and got a new §6§lPERSONAL BEST §e(-§6" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getBest() - map.getTimer(player).getTicks()) + "§e)");
            map.getTimer(player).setBest(map.getTimer(player).getTicks());
        } else {
            player.sendMessage("§6§lCerkour§e> You beat the map: §6" + profile.getMap().getName() + " §ein §6" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getTicks()));
            if (map.getTimer(player).getBest() == 0 && !map.isOS()) {
                map.getTimer(player).setBest(map.getTimer(player).getTicks());
            }
        }
    }

    private void handleRankUp(Player player, Map map, Profile profile) {
        if (map.isOS() && map.getTimer(player).getBest() == 0) {
            if (!map.getIsRankUp()) {
                broadcastRankUp(player, map, profile);
            }
            map.getTimer(player).setBest(map.getTimer(player).getTicks());
            profile.addPoints(PointsUtil.getPointsForDifficulty(map.getDifficulty()));
        }
        if (map.getIsRankUp() && profile.getRankUp() == map.getRankUp()) {
            profile.setRankUp(profile.getRankUp() + 1);
            Bukkit.broadcastMessage("§6§lCerkour§e> §6" + player.getName() + " §ehas ranked up to " + RankUtils.getColoredRank(profile.getRankUp()) + "§e!");
            profile.leaveMap();
            SoundUtils.playSoundRankUpAllPlayers(profile.getRankUp());
            player.setPlayerListName(RankUtils.getColoredRank(profile.getRankUp()) + "§r " + player.getDisplayName());
        }
    }

    private void broadcastRankUp(Player player, Map map, Profile profile) {
        if (map.getDifficulty() >= 7) {
            Bukkit.broadcastMessage("§6§lCerkour§e> §6" + player.getName() + " §ehas beaten the map " + RankUtils.getColoredDifficulty(map.getDifficulty()) + " §6" + map.getName());
            profile.addPoints(PointsUtil.getPointsForDifficulty(map.getDifficulty()));
            profile.leaveMap();
            SoundUtils.playSoundRankUpAllPlayers(map.getDifficulty() >= 9 ? 10 : 7);
        }
    }

    private void handlePracticeMode(Player player, Map map, Profile profile, int x, int y, int z) {
        if (!map.isOS()) {
            handlePracticeTimer(player, map, profile, x, y, z);
        } else {
            handlePracticeOS(player, profile, x, y, z);
        }
    }

    private void handlePracticeTimer(Player player, Map map, Profile profile, int x, int y, int z) {
        TickTimer timer = map.getTimer(player);
        timer.resetTimer();
        Practice practice = profile.getPractice();
        if (practice.getStartPoint() != null && !player.getLocation().equals(practice.getStartPoint()) && !player.getAllowFlight()) {
            map.toggleTimer(true, player);
        }
        if (practice.getEndPoint() != null && practice.getEndPoint().getBlockX() == x && practice.getEndPoint().getBlockZ() == z && practice.getEndPoint().getBlockY() <= y && practice.getEndPoint().getBlockY() <= y + 1.25 && !player.getAllowFlight()) {
            map.toggleTimer(false, player);
            player.sendMessage("§6§lCerkour§e> You completed the practice section in §6" + timer.getTimeFromTicks(timer.getTicks()));
            timer.resetTimer();
            player.teleport(practice.getStartPoint());
        }
    }

    private void handlePracticeOS(Player player, Profile profile, int x, int y, int z) {
        Practice practice = profile.getPractice();
        if (practice.getStartPoint() != null && !player.isSprinting() && !player.getAllowFlight()) {
            player.teleport(practice.getStartPoint());
            player.getActivePotionEffects().forEach(p -> player.removePotionEffect(p.getType()));
        }
        if (practice.getEndPoint() != null && practice.getEndPoint().getBlockX() == x && practice.getEndPoint().getBlockZ() == z && practice.getEndPoint().getBlockY() <= y && practice.getEndPoint().getBlockY() <= y + 1.25 && !player.getAllowFlight()) {
            player.sendMessage("§6§lCerkour§e> You completed the section!");
            player.teleport(practice.getStartPoint());
            player.getActivePotionEffects().forEach(p -> player.removePotionEffect(p.getType()));
        }
    }
}