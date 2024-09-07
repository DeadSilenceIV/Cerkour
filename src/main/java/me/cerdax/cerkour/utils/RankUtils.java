package me.cerdax.cerkour.utils;

import org.bukkit.ChatColor;

public class RankUtils {
    public static String getColoredRank(int i) {
        if (i == 1) {
            return "§eI";
        }
        if (i == 2) {
            return "§eII";
        }
        if (i == 3) {
            return "§eIII";
        }
        if (i == 4) {
            return "§aIV";
        }
        if (i == 5) {
            return "§aV";
        }
        if (i == 6) {
            return "§aVI";
        }
        if (i == 7) {
            return "§d§lVII";
        }
        if (i == 8) {
            return "§d§lVIII";
        }
        if (i == 9) {
            return "§d§lIX";
        }
        if (i == 10) {
            return "§c§lX";
        }
        if (i == 11) {
            return "§c§lXI";
        }
        if (i == 12) {
            return "§c§lXII";
        }
        if (i == 13) {
            return "§9§lXIII";
        }
        return null;
    }
}
