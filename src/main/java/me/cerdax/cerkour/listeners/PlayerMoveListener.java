package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.CheckPoint;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.TickTimer;
import me.cerdax.cerkour.profile.Practice;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.ActionBarUtils;
import me.cerdax.cerkour.utils.RankUtils;
import me.cerdax.cerkour.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location to = e.getTo();
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Map map = profile.getMap();
        Practice practice = profile.getPractice();
        Location locUnder = player.getLocation();
        locUnder.setY(locUnder.getY() - 1);

        int x = to.getBlockX();
        int y = to.getBlockY();
        int z = to.getBlockZ();

        if (map != null) {
            if (!profile.getPractice().getIsEnabled()) {
                if (player.getLocation().getX() != map.getStartLocation().getX() || player.getLocation().getZ() != map.getStartLocation().getZ() || player.getLocation().getY() != map.getStartLocation().getY()) {
                    if (!map.getTimer(player).getIsRunning()) {
                        map.toggleTimer(true, player);
                    }
                }
                for (Material db : map.getDeathBlocks()) {
                    if (db.equals(locUnder.getBlock().getType())) {
                        player.teleport(map.getCheckPointLocation(player));
                    }
                }
                for (CheckPoint c : map.getCheckpoints()) {
                    if (c.getFrom().getBlockX() == x && c.getFrom().getBlockY() == y && c.getFrom().getBlockZ() == z) {
                        if (!c.getPlayers().contains(player.getName())) {
                            for (CheckPoint prevCP : map.getCheckpoints()) {
                                prevCP.getPlayers().remove(player.getName());
                            }
                            c.addPlayer(player.getName());
                            map.serialize();
                            player.sendMessage("§6§lCerkour§e> You reached checkpoint: §6" + c.getIndex());
                        }
                        break;
                    }
                }
                if (map.getEndLocation().getBlockZ() == z && map.getEndLocation().getBlockX() == x && (map.getEndLocation().getBlockY() <= y && map.getEndLocation().getBlockY() <= y + 1.25)) {
                    map.toggleTimer(false, player);
                    for (CheckPoint c : map.getCheckpoints()) {
                        if (c.getPlayers().contains(player.getName())) {
                            c.removePlayer(player.getName());
                            map.serialize();
                        }
                    }
                    if (map.isOS() && map.getTimer(player).getBest() == 0) {
                        if (map.getDifficulty() >= 7 && map.getDifficulty() < 9) {
                            SoundUtils.playSoundRankUpAllPlayers(11);
                            Bukkit.broadcastMessage("§6§lCerkour§e> §6" + player.getName() + " §ehas beaten the map " + RankUtils.getColoredDifficulty(map.getDifficulty()) + " §a" + map.getName());
                        }
                        else if (map.getDifficulty() >= 9) {
                            SoundUtils.playSoundRankUpAllPlayers(13);
                            Bukkit.broadcastMessage("§6§lCerkour§e> §6" + player.getName() + " §ehas beaten the map " + RankUtils.getColoredDifficulty(map.getDifficulty()) + " §c" + map.getName());
                        }
                    }
                    if (map.getIsRankUp() && profile.getRankUp() == map.getRankUp()) {
                        profile.setRankUp(profile.getRankUp() + 1);
                        player.sendMessage("§6§lCerkour§e> You have ranked up to " + RankUtils.getColoredRank(profile.getRankUp()) + " §ein §6" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getTicks()));
                        Bukkit.broadcastMessage("§6§lCerkour§e> §6" + player.getName() + " §ehas ranked up to " + RankUtils.getColoredRank(profile.getRankUp()) + "§e!");
                        SoundUtils.playSoundRankUpAllPlayers(profile.getRankUp());
                        player.setPlayerListName(RankUtils.getColoredRank(Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId()).getRankUp()) + "§r " + player.getDisplayName());
                    }
                    if (map.getTimer(player).getBest() > map.getTimer(player).getTicks()) {
                        player.sendMessage("§6§lCerkour§e> You beat the map: §6" + profile.getMap().getName() + " §ein §6" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getTicks()) + "§e and got a new §6§lPERSONAL BEST §e(-§6" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getBest() - map.getTimer(player).getTicks()) + "§e)");
                        map.getTimer(player).setBest(map.getTimer(player).getTicks());
                        map.serialize();
                    }
                    else if (map.getTimer(player).getBest() == 0) {
                        player.sendMessage("§6§lCerkour§e> You beat the map: §6" + profile.getMap().getName() + " §ein §6" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getTicks()));
                        map.getTimer(player).setBest(map.getTimer(player).getTicks());
                        map.serialize();
                    }
                    else {
                        player.sendMessage("§6§lCerkour§e> You beat the map: §6" + profile.getMap().getName() + " §ein §6" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getTicks()));
                    }

                    map.getTimer(player).resetTimer();
                    profile.leaveMap();
                }
                if (map.isOS()) {
                    if (e.getFrom().distance(e.getTo()) > 0.1D) {
                        if (!player.isSprinting()) {
                            Location checkpoint = map.getCheckPointLocation(player);
                            player.teleport(checkpoint);

                            if (checkpoint.equals(map.getStartLocation())) {
                                if (map.getTimer(player).getIsRunning()) {
                                    map.getTimer(player).stop(player);
                                }
                                map.getTimer(player).resetTimer();
                            }
                        }
                    }
                }
            }
            else {
                if (!map.isOS()) {
                    TickTimer timer = map.getTimer(player);
                    timer.resetTimer();
                    if (practice.getStartPoint() != null) {
                        if (player.getLocation().getX() != practice.getStartPoint().getX() || player.getLocation().getZ() != practice.getStartPoint().getZ() && !player.getAllowFlight()) {
                            map.toggleTimer(true, player);
                        }
                    }
                    if (practice.getEndPoint() != null) {
                        if (x == practice.getEndPoint().getBlockX() && z == practice.getEndPoint().getBlockZ() && (practice.getEndPoint().getBlockY() <= y && practice.getEndPoint().getBlockY() <= y + 1.25) && !player.getAllowFlight()) {
                            map.toggleTimer(false, player);
                            player.sendMessage("§6§lCerkour§e> You completed the practice section in §6" + timer.getTimeFromTicks(timer.getTicks()));
                            timer.resetTimer();
                            player.teleport(practice.getStartPoint());
                        }
                    }
                }
                else {
                    if (e.getFrom().distance(e.getTo()) > 0.1D) {
                        if (!player.isSprinting() && practice.getStartPoint() != null && !player.getAllowFlight()) {
                            player.teleport(practice.getStartPoint());
                        }
                        if (player.getLocation().getBlockX() == practice.getEndPoint().getBlockX() && player.getLocation().getBlockZ() == practice.getEndPoint().getBlockZ() && (practice.getEndPoint().getBlockY() <= player.getLocation().getBlockY() && practice.getEndPoint().getBlockY() <= player.getLocation().getBlockY() + 1.25) && !player.getAllowFlight()) {
                            player.sendMessage("§6§lCerkour§e> You completed the section!");
                            player.teleport(practice.getStartPoint());
                        }
                    }
                }
            }
        }
    }
}