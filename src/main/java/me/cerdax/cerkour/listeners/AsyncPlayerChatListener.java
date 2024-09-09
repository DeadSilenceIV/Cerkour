package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Bukkit.broadcastMessage(RankUtils.getColoredRank(profile.getRankUp()) + " §7" + player.getDisplayName() + "§8: §r" + e.getMessage());
        e.setCancelled(true);
    }
}
