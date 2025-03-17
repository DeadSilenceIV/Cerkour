package me.cerdax.cerkour.utils;

import java.util.HashMap;
import java.util.Map;

public class RankUtils {

    private static final Map<Integer, String> RANK_MAP = new HashMap<>();

    static {
        RANK_MAP.put(1, "§eI");
        RANK_MAP.put(2, "§eII");
        RANK_MAP.put(3, "§eIII");
        RANK_MAP.put(4, "§aIV");
        RANK_MAP.put(5, "§aV");
        RANK_MAP.put(6, "§aVI");
        RANK_MAP.put(7, "§d§lVII");
        RANK_MAP.put(8, "§d§lVIII");
        RANK_MAP.put(9, "§d§lIX");
        RANK_MAP.put(10, "§c§lX");
        RANK_MAP.put(11, "§c§lXI");
        RANK_MAP.put(12, "§c§lXII");
        RANK_MAP.put(13, "§9§lXIII");
        RANK_MAP.put(14, "§9§lXIV");
    }

    public static String getColoredRank(int i) {
        return RANK_MAP.getOrDefault(i, "§7Unknown");
    }

    public static String getColoredDifficulty(int i) {
        String color = (i <= 6) ? "§e" : (i <= 8) ? "§a" : "§c";
        return "§8[" + color + i + "§8]";
    }
}