package me.cerdax.cerkour.utils;

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

        ItemStack fireCharge = new ItemStack(Material.FIREWORK_CHARGE);
        ItemMeta fireChargeMeta = fireCharge.getItemMeta();
        fireChargeMeta.setDisplayName("§6Practice");
        fireCharge.setItemMeta(fireChargeMeta);
        player.getInventory().setItem(7, fireCharge);

        ItemStack redstone = new ItemStack(Material.REDSTONE);
        ItemMeta redstoneMeta = redstone.getItemMeta();
        redstoneMeta.setDisplayName("§6Leave");
        redstone.setItemMeta(redstoneMeta);
        player.getInventory().setItem(8, redstone);
    }

    public static void practiceInventory(Player player) {
        practiceInventoryOS(player);

        ItemStack redstoneBlock = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta redstoneBlockMeta = redstoneBlock.getItemMeta();
        redstoneBlockMeta.setDisplayName("§6End Location");
        redstoneBlock.setItemMeta(redstoneBlockMeta);
        player.getInventory().setItem(5, redstoneBlock);
    }

    public static void practiceInventoryOS(Player player) {
        ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta emeraldBlockMeta = emeraldBlock.getItemMeta();
        emeraldBlockMeta.setDisplayName("§6Start Location");
        emeraldBlock.setItemMeta(emeraldBlockMeta);
        player.getInventory().setItem(4, emeraldBlock);

        ItemStack bone = new ItemStack(Material.BONE);
        ItemMeta boneMeta = bone.getItemMeta();
        boneMeta.setDisplayName("§6Toggle Flight");
        bone.setItemMeta(boneMeta);
        player.getInventory().setItem(6, bone);
    }

    public static void RemovePracticeInventory(Player player) {
        player.getInventory().clear(4);
        player.getInventory().clear(5);
        player.getInventory().clear(6);
    }
}
