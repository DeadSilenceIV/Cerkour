package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.profile.ProfileManager;
import me.cerdax.cerkour.utils.InventoryUtils;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoinListener implements Listener {

    ProfileManager profileManager;

    public PlayerJoinListener(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
            Bukkit.getScheduler().runTaskLater(Cerkour.getInstance(), () -> {
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                InventoryUtils.lobbyInventory(player);
                player.teleport(LocationUtils.getSpawn());
            }, 3L);
        e.setJoinMessage("§e[§a§l+§e] §6" + player.getName());
        player.setAllowFlight(false);
        if(profileManager.getProfile(e.getPlayer().getUniqueId()) == null) {
            Profile profile = new Profile(e.getPlayer().getUniqueId());
            profileManager.saveProfile(profile);
        }
    }
}
