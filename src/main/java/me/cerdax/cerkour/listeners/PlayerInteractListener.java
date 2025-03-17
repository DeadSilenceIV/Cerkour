package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.profile.Practice;
import me.cerdax.cerkour.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;


public class PlayerInteractListener implements Listener {

    public void interactLogic(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (e.getItem().getType() == Material.COMPASS) {
            if (e.getItem().getItemMeta().getDisplayName().equals("§6Server Selector")) {
                Inventory inventory = Bukkit.createInventory(null, 45, "§8Server Selector");
                ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
                ItemMeta glassMeta = glass.getItemMeta();
                glassMeta.setDisplayName(" ");
                glass.setItemMeta(glassMeta);
                Arrays.<Integer>asList(new Integer[] {
                        Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9),
                        Integer.valueOf(18), Integer.valueOf(27), Integer.valueOf(36), Integer.valueOf(17), Integer.valueOf(26), Integer.valueOf(35), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40),
                        Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44) }).forEach(integer -> inventory.setItem(integer.intValue(), glass));
                ItemStack os = new ItemStack(Material.DIRT);
                ItemMeta osMeta = os.getItemMeta();
                osMeta.setDisplayName("§8* " + "§6OS");
                osMeta.setLore(Collections.singletonList("§eOnlySprint maps of all difficulties!"));
                os.setItemMeta(osMeta);
                inventory.setItem(24, os);
                ItemStack rankUp = new ItemStack(Material.GRASS);
                ItemMeta rankUpMeta = rankUp.getItemMeta();
                rankUpMeta.setDisplayName("§8* " + "§6OS Rankup");
                rankUpMeta.setLore(Collections.singletonList("§eComplete maps to rankup, test your skill!"));
                rankUp.setItemMeta(rankUpMeta);
                inventory.setItem(22, rankUp);
                ItemStack speedRun = new ItemStack(Material.STONE);
                ItemMeta speedRunMeta = speedRun.getItemMeta();
                speedRunMeta.setDisplayName("§8* " + "§6Speedrun");
                speedRunMeta.setLore(Collections.singletonList("§eSee how far you can push your best time!"));
                speedRun.setItemMeta(speedRunMeta);
                inventory.setItem(20, speedRun);
                player.openInventory(inventory);
            }
            e.setCancelled(true);
        }
        else if (e.getItem().getType() == Material.QUARTZ) {
            if (e.getItem().getItemMeta().getDisplayName().equals("§6Reset")) {
                Map map = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId()).getMap();
                if (!profile.getPractice().getIsEnabled()) {
                    if (map.getCheckPointLocation(player) == map.getStartLocation()) {
                        if (map.getTimer(player).getIsRunning()) {
                            map.getTimer(player).stop(player);
                            map.getTimer(player).resetTimer();
                        }
                        else {
                            map.getTimer(player).resetTimer();
                        }
                    }
                    player.teleport(profile.getMap().getCheckPointLocation(player));
                }
                else {
                    if (map.getTimer(player).getIsRunning()) {
                        map.getTimer(player).stop(player);
                        map.getTimer(player).resetTimer();
                    }
                    else {
                        map.getTimer(player).resetTimer();
                    }
                    player.teleport(profile.getPractice().getStartPoint());
                }
            }
        }
        else if (e.getItem().getType() == Material.REDSTONE) {
            if (e.getItem().getItemMeta().getDisplayName().equals("§6Leave")) {
                profile.leaveMap();
            }
        }
        else if (e.getItem().getType() == Material.FIREWORK_CHARGE) {
            if (e.getItem().getItemMeta().getDisplayName().equals("§6Practice")) {
                Map map = profile.getMap();
                Practice practice = profile.getPractice();
                if (practice.getIsEnabled()) {
                    profile.leavePractice();
                    if (map.isOS()) {
                        if (map.getTimer(player).getIsRunning() && map.getCheckPointLocation(player) == map.getStartLocation()) {
                            map.toggleTimer(false, player);
                        }
                    }
                    else {
                        if (!map.getTimer(player).getIsRunning() && map.getCheckPointLocation(player) == map.getStartLocation()) {
                            map.toggleTimer(true, player);
                        }
                    }

                    player.sendMessage("§6§lCerkour§e> You left practice mode!");
                }
                else {
                    profile.enterPractice(map);
                    if (map.getTimer(player).getIsRunning()) {
                        map.toggleTimer(false, player);
                    }
                    player.sendMessage("§6§lCerkour§e> You entered practice mode!");
                }
            }
        }
        else if (e.getItem().getType() == Material.EMERALD_BLOCK) {
            if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Start Location")) {
                profile.getPractice().setStartPoint(player.getLocation());
                player.sendMessage("§6§lCerkour§e> You set your start location!");
            }
        }
        else if (e.getItem().getType() == Material.REDSTONE_BLOCK) {
            if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6End Location")) {
                profile.getPractice().setEndPoint(player.getLocation());
                player.sendMessage("§6§lCerkour§e> You set your end location!");
            }
        }
        else if (e.getItem().getType() == Material.BONE) {
            if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Toggle Flight")) {
                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    player.setFlying(false);

                    World world = profile.getMap().getStartLocation().getWorld();
                    double x = profile.getPractice().getStartPoint().getX();
                    double y = profile.getPractice().getStartPoint().getY();
                    double z = profile.getPractice().getStartPoint().getZ();
                    float yaw = profile.getPractice().getStartPoint().getYaw();
                    float pitch = player.getLocation().getPitch();
                    Location loc = new Location(world, x, y, z, yaw, pitch); //So the interact event doesn't double reg

                    player.teleport(loc);
                    player.sendMessage("§6§lCerkour§e> You §cDISABLED§e flight");
                }
                else {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage("§6§lCerkour§e> You §aENABLED§e flight");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if (e.getItem().hasItemMeta()) {
                switch(e.getAction()) {
                    case RIGHT_CLICK_AIR:
                    case RIGHT_CLICK_BLOCK:
                    case LEFT_CLICK_AIR:
                    case LEFT_CLICK_BLOCK:
                        interactLogic(e);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
