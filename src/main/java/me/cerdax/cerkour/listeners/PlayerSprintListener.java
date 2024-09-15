package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.CheckPoint;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.ActionBarUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class PlayerSprintListener implements Listener {

    @EventHandler
    public void onPlayerSprint(PlayerToggleSprintEvent e) {
        Player player = e.getPlayer();
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Map map = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId()).getMap();
        if (map != null) {
            ActionBarUtils.sendActionbar(player, "§e§l" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getTicks()));
            if (!e.isSprinting() && map.isOS()) {
                player.teleport(map.getCheckPointLocation(player));
                if (map.getCheckPointLocation(player) == map.getStartLocation()) {
                    if (map.getTimer(player).getIsRunning()) {
                        map.getTimer(player).stop(player);
                    }
                    else {
                        map.getTimer(player).resetTimer();
                    }
                    ActionBarUtils.sendActionbar(player, "§e§l" + map.getTimer(player).getTimeFromTicks(map.getTimer(player).getTicks()));
                }
            }
        }
    }
}
