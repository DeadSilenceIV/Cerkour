package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.managers.InventoryManager;
import me.cerdax.cerkour.map.visualizer.RankupMapsVisualizer;
import me.cerdax.cerkour.map.visualizer.SpeedRunMapsVisualizer;
import me.cerdax.cerkour.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerQuitListener implements Listener {
    private InventoryManager inventoryManager;
    public PlayerQuitListener() {
        inventoryManager = Cerkour.getInstance().getInventoryManager();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage("§e[§c§l-§e] §6" + player.getName());
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile.getMap() != null) {
            profile.leaveMap();
        }
        SpeedRunMapsVisualizer speedRunMapsVisualizer = inventoryManager.getSpeedRunMapsVisualizer(player.getUniqueId());
        if(speedRunMapsVisualizer != null) {
            inventoryManager.unRegister(speedRunMapsVisualizer);
        }
        RankupMapsVisualizer rankupMapsVisualizer = inventoryManager.getRankupMapsVisualizer(player.getUniqueId());
        if(rankupMapsVisualizer != null) {
            inventoryManager.unRegister(rankupMapsVisualizer);
        }
    }
}
