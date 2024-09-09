package me.cerdax.cerkour.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtils {

    public static void lobbyInventory(Player player) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName("§6Server Selector");
        compass.setItemMeta(compassMeta);
        player.getInventory().setItem(4, compass);
    }

    public static void gameInventory(Player player) {
        ItemStack quartz = new ItemStack(Material.QUARTZ);
        ItemMeta quartzMeta = quartz.getItemMeta();
        quartzMeta.setDisplayName("§6Reset");
        quartz.setItemMeta(quartzMeta);
        player.getInventory().setItem(0,  quartz);

        //MORE ITEMS HERE, PERHAPS PRACTICE ITEM IN THE FUTURE

        ItemStack redstone = new ItemStack(Material.REDSTONE);
        ItemMeta redstoneMeta = redstone.getItemMeta();
        redstoneMeta.setDisplayName("§6Leave");
        redstone.setItemMeta(redstoneMeta);
        player.getInventory().setItem(8, redstone);
    }
}
