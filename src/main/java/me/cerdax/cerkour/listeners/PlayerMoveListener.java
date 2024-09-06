package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
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

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location to = e.getTo();
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Map map = profile.getMap();
        if (map != null) {
            if (map.getEndLocation().getBlockZ() == to.getBlockZ() && map.getEndLocation().getBlockX() == to.getBlockX() && map.getEndLocation().getBlockY() == to.getBlockY()) {
                if (map.getIsRankUp() && profile.getRankUp() == map.getRankUp()) {
                    profile.setRankUp(profile.getRankUp() + 1);
                    player.sendMessage("§6§lCerkour§e> You have ranked up to " + RankUtils.getColoredRank(profile.getRankUp()));
                    Bukkit.broadcastMessage("§6§lCerkour§e> §6" + player.getName() + " §ehas ranked up to " + RankUtils.getColoredRank(profile.getRankUp()) + "§e!");
                    SoundUtils.playSoundRankUpAllPlayers(profile.getRankUp());
                }
                else {
                    player.sendMessage("§6§lCerkour§e> You beat the map: §6" + profile.getMap().getName());
                }
                profile.leaveMap();
            }
            if (e.getFrom().distance(e.getTo()) > 0.1D) {
                if (!player.isSprinting()) {
                    player.teleport(map.getStartLocation());
                }
            }
        }
    }
}
