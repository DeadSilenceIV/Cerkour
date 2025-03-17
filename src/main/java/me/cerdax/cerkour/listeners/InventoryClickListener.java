package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getInventory() != null) {
            if (e.getInventory().getTitle().equals("§8Server Selector")) {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null && itemStack.getItemMeta().getDisplayName().equals("§8* " + "§6OS Rankup")) {
                    Inventory inventory = Bukkit.createInventory(null, 45, "§8Rankup");
                    ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
                    ItemMeta glassMeta = glass.getItemMeta();
                    glassMeta.setDisplayName(" ");
                    glass.setItemMeta(glassMeta);
                    Arrays.<Integer>asList(new Integer[] {
                            Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9),
                            Integer.valueOf(18), Integer.valueOf(27), Integer.valueOf(36), Integer.valueOf(17), Integer.valueOf(26), Integer.valueOf(35), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40),
                            Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44) }).forEach(integer -> inventory.setItem(integer.intValue(), glass));
                    int i = 0;
                    for (Map map : Cerkour.getInstance().getMapManager().getAllRankUpMaps()) {
                        ItemStack mapItem = new ItemStack(Material.PAPER, 1);
                        ItemMeta mapItemMeta = mapItem.getItemMeta();
                        mapItemMeta.setDisplayName(RankUtils.getColoredRank(map.getRankUp()));
                        mapItem.setItemMeta(mapItemMeta);
                        if (i % 7 == 0  && i != 0) {
                            i += 2;
                        }
                        inventory.setItem(10 + i, mapItem);
                        i++;
                    }
                    player.openInventory(inventory);
                }
                if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null && itemStack.getItemMeta().getDisplayName().equals("§8* " + "§6Speedrun")) {
                    Inventory inventory = Bukkit.createInventory(null, 45, "§8Speedrun");
                    ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
                    ItemMeta glassMeta = glass.getItemMeta();
                    glassMeta.setDisplayName(" ");
                    glass.setItemMeta(glassMeta);
                    Arrays.<Integer>asList(new Integer[] {
                            Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9),
                            Integer.valueOf(18), Integer.valueOf(27), Integer.valueOf(36), Integer.valueOf(17), Integer.valueOf(26), Integer.valueOf(35), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40),
                            Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44) }).forEach(integer -> inventory.setItem(integer.intValue(), glass));
                    int i = 0;
                    for (Map map : Cerkour.getInstance().getMapManager().getAllMaps()) {
                        if (!map.isOS()) {
                            ItemStack mapItem = new ItemStack(Material.PAPER, 1);
                            ItemMeta mapItemMeta = mapItem.getItemMeta();
                            mapItemMeta.setDisplayName("§e" + map.getName());
                            mapItem.setItemMeta(mapItemMeta);
                            if (i % 7 == 0  && i != 0) {
                                i += 2;
                            }
                            inventory.setItem(10 + i, mapItem);
                            i++;
                        }
                    }
                    player.openInventory(inventory);
                }
                e.setCancelled(true);
            }
            else if (e.getInventory().getTitle().equals("§8Rankup")) {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null && itemStack.getType() == Material.PAPER) {
                    Map map = null;
                    for (int i = 1; i < 14; i++) {
                        if (Cerkour.getInstance().getMapManager().getMapByRankUp(i) != null) {
                            if (Objects.equals(RankUtils.getColoredRank(Cerkour.getInstance().getMapManager().getMapByRankUp(i).getRankUp()), e.getCurrentItem().getItemMeta().getDisplayName())) {
                                map = Cerkour.getInstance().getMapManager().getMapByRankUp(i);
                            }
                        }
                    }
                    if (map != null) {
                        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
                        profile.joinMap(map);
                    }
                    e.setCancelled(true);
                }
            }
            else if (e.getInventory().getTitle().equals("§8Speedrun")) {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null && itemStack.getType() == Material.PAPER) {
                    Map map = null;
                    for (Map map1 : Cerkour.getInstance().getMapManager().getAllMaps()) {
                        if (itemStack.getItemMeta().getDisplayName().equals("§e" + map1.getName())) {
                            map = map1;
                        }
                    }
                    if (map != null) {
                        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
                        profile.joinMap(map);
                    }
                    e.setCancelled(true);
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
