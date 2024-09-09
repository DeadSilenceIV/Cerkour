package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.utils.InventoryUtils;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.teleport(LocationUtils.getSpawn());
        player.setGameMode(GameMode.ADVENTURE);
        e.setJoinMessage("§e[§a§l+§e] §6" + player.getName());
        player.getInventory().clear();
        InventoryUtils.lobbyInventory(player);
    }
}
