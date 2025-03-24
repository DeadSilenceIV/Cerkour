package me.cerdax.cerkour;

import me.cerdax.cerkour.map.TickTimer;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.RankUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PAPIExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "cerkour";
    }

    @Override
    public String getAuthor() {
        return "DeadSilenceIV";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player != null) {
            Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (params.equalsIgnoreCase("rankup")) {
                return String.valueOf(profile.getRankUp());
            }
            if(params.equalsIgnoreCase("colored_rankup")) {
                return RankUtils.getColoredRank(profile.getRankUp());
            }
            if(params.equalsIgnoreCase("ingame")){
                return profile.getMap() != null ? "true" : "false";
            }
            if(params.equalsIgnoreCase("map_name")){
                return profile.getMap().getName();
            }
            if(params.equalsIgnoreCase("map_difficulty")){
                return String.valueOf(profile.getMap().getDifficulty());
            }
            if(params.equalsIgnoreCase("map_fastest_time")){
                TickTimer timer = profile.getMap().getTimer(player);
                return timer.getTimeFromTicks(timer.getBest());
            }
        }
        return "";
    }

}
