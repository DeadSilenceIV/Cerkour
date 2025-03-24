package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.managers.InventoryManager;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.MapManager;
import me.cerdax.cerkour.map.visualizer.RankupMapsVisualizer;
import me.cerdax.cerkour.map.visualizer.SpeedRunMapsVisualizer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    private Cerkour plugin;
    private InventoryManager inventoryManager;
    private MapManager mapManager;
    public InventoryClickListener() {
        this.plugin = Cerkour.getInstance();
        this.inventoryManager = plugin.getInventoryManager();
        this.mapManager = plugin.getMapManager();
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getInventory() != null) {
            if (e.getInventory().getTitle().equals("§8Server Selector")) {
                e.setCancelled(true);
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null && itemStack.getItemMeta().getDisplayName().equals("§8* " + "§6OS Rankup")) {
                    if(mapManager.getAllRankUpMaps().isEmpty()){
                        player.sendMessage("§cThere are no maps to show to show!");
                        return;
                    }
                    RankupMapsVisualizer visualizer = inventoryManager.getOrLoadRankupMapsVisualizer(player.getUniqueId());
                    visualizer.refreshIfPossible();
                    visualizer.openPage(player, 0);
                    return;
                }
                if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null && itemStack.getItemMeta().getDisplayName().equals("§8* " + "§6Speedrun")) {
                    if(mapManager.getAllMaps().stream().allMatch(Map::isOS)){
                        player.sendMessage("§cThere are no maps to show to show!");
                        return;
                    }
                    SpeedRunMapsVisualizer visualizer = inventoryManager.getOrLoadSpeedRunMapsVisualizer(player.getUniqueId());
                    visualizer.refreshIfPossible();
                    visualizer.openPage(player, 0);
                    return;
                }
            }
        }
        if (e.getWhoClicked() instanceof Player) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
            }
        }
    }
}
