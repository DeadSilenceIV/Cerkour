package me.cerdax.cerkour.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {
    public static Sound determineSoundRankUp(int i) {
        if (i < 7) {
            return Sound.EXPLODE;
        }
        else if (i < 9) {
            return Sound.WITHER_SPAWN;
        }
        else {
            return Sound.ENDERDRAGON_DEATH;
        }
    }

    public static void playSoundRankUpPlayer(Player player, int rankUp) {
        player.playSound(player.getLocation(), determineSoundRankUp(rankUp), Float.MAX_VALUE, 1.0F);
    }
    public static void playSoundRankUpAllPlayers(int rankUp) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), determineSoundRankUp(rankUp), Float.MAX_VALUE, 1.0F);
        }
    }
}
