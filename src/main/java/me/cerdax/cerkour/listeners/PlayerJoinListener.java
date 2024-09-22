package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.utils.InventoryUtils;
import me.cerdax.cerkour.utils.LocationUtils;
import me.cerdax.cerkour.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
            Bukkit.getScheduler().runTaskLater(Cerkour.getInstance(), () -> {
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                InventoryUtils.lobbyInventory(player);
                player.teleport(LocationUtils.getSpawn());
                player.setPlayerListName(RankUtils.getColoredRank(Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId()).getRankUp()) + "§r " + player.getDisplayName());
            }, 3L);
        e.setJoinMessage("§e[§a§l+§e] §6" + player.getName());
        player.setAllowFlight(false);
    }
}
