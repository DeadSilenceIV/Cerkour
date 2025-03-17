package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.TickTimer;
import me.cerdax.cerkour.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerSprintListener implements Listener {

    @EventHandler
    public void onPlayerSprint(PlayerToggleSprintEvent e) {
        Player player = e.getPlayer();
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile.getMap() != null) {
            Map map = profile.getMap();
            TickTimer timer = map.getTimer(player);
            if (!profile.getPractice().getIsEnabled() && timer != null) {
                if (!e.isSprinting() && map.isOS()) {
                    player.teleport(map.getCheckPointLocation(player));
                    if (map.getCheckPointLocation(player) == map.getStartLocation()) {
                        for (PotionEffect p : player.getActivePotionEffects()) {
                            player.removePotionEffect(p.getType());
                        }
                        if (timer.getIsRunning()) {
                            timer.stop(player);
                        }
                        timer.resetTimer();
                    }
                }
            }
            else if (profile.getPractice().getIsEnabled() && profile.getPractice().getStartPoint() != null && !player.getAllowFlight()) {
                if (!e.isSprinting() && map.isOS()) {
                    player.teleport(profile.getPractice().getStartPoint());
                    for (PotionEffect p : player.getActivePotionEffects()) {
                        player.removePotionEffect(p.getType());
                    }
                }
            }
        }
    }
}
