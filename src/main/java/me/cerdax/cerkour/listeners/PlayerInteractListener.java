package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getItem() != null && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR)) {
            if (e.getItem().getType() == Material.COMPASS) {
                if (e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName().equals("§6Server Selector")) {
                    Inventory inventory = Bukkit.createInventory(null, 45, "§8Server Selector");
                    ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
                    ItemMeta glassMeta = glass.getItemMeta();
                    glassMeta.setDisplayName(" ");
                    Arrays.<Integer>asList(new Integer[] {
                            Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9),
                            Integer.valueOf(18), Integer.valueOf(27), Integer.valueOf(36), Integer.valueOf(17), Integer.valueOf(26), Integer.valueOf(35), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40),
                            Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44) }).forEach(integer -> inventory.setItem(integer.intValue(), glass));
                    ItemStack rankUp = new ItemStack(Material.GRASS);
                    ItemMeta rankUpMeta = rankUp.getItemMeta();
                    rankUpMeta.setDisplayName("§8* " + "§6OS Rankup");
                    rankUp.setItemMeta(rankUpMeta);
                    inventory.setItem(22, rankUp);
                    player.openInventory(inventory);
                }
                e.setCancelled(true);
            }
            else if (e.getItem().getType() == Material.QUARTZ) {
                if (e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName().equals("§6Reset")) {
                    Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId()).getMap().teleportToCheckPoint(player);
                }
            }
            else if (e.getItem().getType() == Material.REDSTONE) {
                if (e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName().equals("§6Leave")) {
                    Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId()).leaveMap(player);
                }
            }
        }
    }
}
