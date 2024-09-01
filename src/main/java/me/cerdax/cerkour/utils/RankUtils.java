package me.cerdax.cerkour.utils;

import org.bukkit.ChatColor;

public class RankUtils {
    public static String getColoredRank(int i) {
        if (i == 1) {
            return ChatColor.GOLD + "[" + ChatColor.YELLOW + "I" + ChatColor.GOLD + "]";
        }
        if (i == 2) {
            return ChatColor.GOLD + "[" + ChatColor.YELLOW + "II" + ChatColor.GOLD + "]";
        }
        if (i == 3) {
            return ChatColor.GOLD + "[" + ChatColor.YELLOW + "III" + ChatColor.GOLD + "]";
        }
        if (i == 4) {
            return ChatColor.GOLD + "[" + ChatColor.GREEN + "IV" + ChatColor.GOLD + "]";
        }
        if (i == 5) {
            return ChatColor.GOLD + "[" + ChatColor.GREEN + "V" + ChatColor.GOLD + "]";
        }
        if (i == 6) {
            return ChatColor.GOLD + "[" + ChatColor.GREEN + "VI" + ChatColor.GOLD + "]";
        }
        if (i == 7) {
            return ChatColor.GOLD + "[" + ChatColor.DARK_PURPLE + "<" + ChatColor.LIGHT_PURPLE + "VII" + ChatColor.DARK_PURPLE + ">" + ChatColor.GOLD + "]";
        }
        if (i == 8) {
            return ChatColor.GOLD + "[" + ChatColor.DARK_PURPLE + "<" + ChatColor.LIGHT_PURPLE + "VIII" + ChatColor.DARK_PURPLE + ">" + ChatColor.GOLD + "]";
        }
        if (i == 9) {
            return ChatColor.GOLD + "[" + ChatColor.DARK_PURPLE + "<" + ChatColor.LIGHT_PURPLE + "IX" + ChatColor.DARK_PURPLE + ">" + ChatColor.GOLD + "]";
        }
        if (i == 10) {
            return ChatColor.GOLD + "[" + ChatColor.DARK_RED + "<" + ChatColor.RED + "X" + ChatColor.DARK_RED + ">" + ChatColor.GOLD + "]";
        }
        if (i == 11) {
            return ChatColor.GOLD + "[" + ChatColor.DARK_RED + "<" + ChatColor.RED + "XI" + ChatColor.DARK_RED + ">" + ChatColor.GOLD + "]";
        }
        if (i == 12) {
            return ChatColor.GOLD + "[" + ChatColor.DARK_RED + "<" + ChatColor.RED + "XII" + ChatColor.DARK_RED + ">" + ChatColor.GOLD + "]";
        }
        if (i == 13) {
            return ChatColor.GOLD + "[" + ChatColor.DARK_BLUE + "<<" + ChatColor.BLUE + "XIII" + ChatColor.DARK_BLUE + ">>" + ChatColor.GOLD + "]";
        }
        return null;
    }
}
