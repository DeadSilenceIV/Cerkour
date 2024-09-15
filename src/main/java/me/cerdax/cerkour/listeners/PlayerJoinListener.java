package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.utils.InventoryUtils;
import me.cerdax.cerkour.utils.LocationUtils;
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
        if (!player.hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLater(Cerkour.getInstance(), () -> {
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                InventoryUtils.lobbyInventory(player);
                player.teleport(LocationUtils.getSpawn());
            }, 3L);
        }
        else {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            InventoryUtils.lobbyInventory(player);
            player.teleport(LocationUtils.getSpawn());
        }
        e.setJoinMessage("§e[§a§l+§e] §6" + player.getName());
    }
}
