package me.cerdax.cerkour.utils;

import org.bukkit.ChatColor;

import javax.management.remote.rmi._RMIConnection_Stub;

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
        if (i == 14) {
            return "§9§lXIV";
        }
        return null;
    }
    public static String getColoredDifficulty(int i) {
        switch(i) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return "§8[§e" + i + "§8]";
            case 7:
            case 8:
                return "§8[§a" + i + "§8]";
            default:
                return "§8[§c" + i + "§8]";

        }
    }
}
