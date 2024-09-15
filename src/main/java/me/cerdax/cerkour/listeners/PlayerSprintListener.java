package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.CheckPoint;
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
            boolean exist = false;
            if (map.getCheckpoints() != null) {
                for (CheckPoint c : map.getCheckpoints()) {
                    if (c.getPlayers().contains(player.getName())) {
                        player.teleport(c.getTo());
                        exist = true;
                    }
                }
            }
            if (!exist) {
                player.teleport(map.getStartLocation());
            }
        }
    }
}
