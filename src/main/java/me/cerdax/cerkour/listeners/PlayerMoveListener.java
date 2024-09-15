package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.CheckPoint;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.RankUtils;
import me.cerdax.cerkour.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

        int x = to.getBlockX();
        int y = to.getBlockY();
        int z = to.getBlockZ();

        if (map != null) {
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
                for (CheckPoint c : map.getCheckpoints()) {
                    if (c.getPlayers().contains(player.getName())) {
                        c.removePlayer(player.getName());
                        map.serialize();
                    }
                }
                if (map.getIsRankUp() && profile.getRankUp() == map.getRankUp()) {
                    profile.setRankUp(profile.getRankUp() + 1);
                    player.sendMessage("§6§lCerkour§e> You have ranked up to " + RankUtils.getColoredRank(profile.getRankUp()));
                    Bukkit.broadcastMessage("§6§lCerkour§e> §6" + player.getName() + " §ehas ranked up to " + RankUtils.getColoredRank(profile.getRankUp()) + "§e!");
                    SoundUtils.playSoundRankUpAllPlayers(profile.getRankUp());
                }
                else {
                    player.sendMessage("§6§lCerkour§e> You beat the map: §6" + profile.getMap().getName());
                }
                profile.leaveMap(player);
            }
            if (e.getFrom().distance(e.getTo()) > 0.1D) {
                if (!player.isSprinting()) {
                    map.teleportToCheckPoint(player);
                }
            }
        }
    }
}
