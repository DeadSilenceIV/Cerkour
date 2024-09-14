package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class PlayerSprintListener implements Listener {

    @EventHandler
    public void onPlayerSprint(PlayerToggleSprintEvent e) {
        Player player = e.getPlayer();
        Map map = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId()).getMap();
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (map != null && !e.isSprinting()) {
            player.teleport(map.getStartLocation());
        }
    }
}
