package me.cerdax.cerkour.map.visualizer.buttons;

import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.utils.RankUtils;

public class RankupMapButton extends MapButton {
    public RankupMapButton(Map map) {
        super(map);
    }

    @Override
    public String getItemName() {
        return RankUtils.getColoredRank(map.getRankUp());
    }
}
