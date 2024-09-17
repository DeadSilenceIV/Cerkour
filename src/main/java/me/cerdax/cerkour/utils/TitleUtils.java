package me.cerdax.cerkour.utils;

import me.cerdax.cerkour.map.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;

public class TitleUtils {

    public static Title getTitleJoinMap(Map map) {
        final Component mainTitle = Component.text(map.getName(), NamedTextColor.GOLD);
        if (map.getIsRankUp()) {
            final Component subtitle = Component.text("Rankup", NamedTextColor.YELLOW);
            return Title.title(mainTitle, subtitle);
        }
        else if (map.isOS() && !map.getIsRankUp()) {
            final Component subtitle = Component.text("Only Sprint", NamedTextColor.YELLOW);
            return Title.title(mainTitle, subtitle);
        }
        else {
            final Component subtitle = Component.text("Speedrun", NamedTextColor.YELLOW);
            return Title.title(mainTitle, subtitle);
        }
    }
}
