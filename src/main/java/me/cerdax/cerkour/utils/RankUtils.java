package me.cerdax.cerkour.utils;

import org.bukkit.ChatColor;

public class RankUtils {
    public static String getColoredRank(int i) {
        if (i == 1) {
            return ChatColor.GRAY + "[" + ChatColor.YELLOW + "I" + ChatColor.GRAY + "]";
        }
        if (i == 2) {
            return ChatColor.GRAY + "[" + ChatColor.YELLOW + "II" + ChatColor.GRAY + "]";
        }
        if (i == 3) {
            return ChatColor.GRAY + "[" + ChatColor.YELLOW + "III" + ChatColor.GRAY + "]";
        }
        if (i == 4) {
            return ChatColor.GRAY + "[" + ChatColor.GREEN + "IV" + ChatColor.GRAY + "]";
        }
        if (i == 5) {
            return ChatColor.GRAY + "[" + ChatColor.GREEN + "V" + ChatColor.GRAY + "]";
        }
        if (i == 6) {
            return ChatColor.GRAY + "[" + ChatColor.GREEN + "VI" + ChatColor.GRAY + "]";
        }
        if (i == 7) {
            return "§d[" + ChatColor.DARK_PURPLE + "<" + ChatColor.LIGHT_PURPLE + "VII" + ChatColor.DARK_PURPLE + ">" + "§d]";
        }
        if (i == 8) {
            return "§d[" + ChatColor.DARK_PURPLE + "<" + ChatColor.LIGHT_PURPLE + "VIII" + ChatColor.DARK_PURPLE + ">" + "§d]";
        }
        if (i == 9) {
            return "§d[" + ChatColor.DARK_PURPLE + "<" + ChatColor.LIGHT_PURPLE + "IX" + ChatColor.DARK_PURPLE + ">" + "§d]";
        }
        if (i == 10) {
            return "§c[" + ChatColor.DARK_RED + "<" + ChatColor.RED + "X" + ChatColor.DARK_RED + ">" + "§c]";
        }
        if (i == 11) {
            return "§c[" + ChatColor.DARK_RED + "<" + ChatColor.RED + "XI" + ChatColor.DARK_RED + ">" + "§c]";
        }
        if (i == 12) {
            return "§c[" + ChatColor.DARK_RED + "<" + ChatColor.RED + "XII" + ChatColor.DARK_RED + ">" + "§c]";
        }
        if (i == 13) {
            return "§9[" + "§1<<" + "§9§n§lXIII" + "§1>>" + "§9]";
        }
        return null;
    }
}
