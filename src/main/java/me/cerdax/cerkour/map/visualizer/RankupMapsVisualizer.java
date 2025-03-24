package me.cerdax.cerkour.map.visualizer;

import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.visualizer.buttons.MapButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RankupMapsVisualizer extends MapVisualizer{

    public RankupMapsVisualizer(UUID player) {
        super(player);
    }
    @Override
    public List<Map> getContent() {
        return new ArrayList<>(mapManager.getAllRankUpMaps());
    }

    @Override
    public String getPagesTitle() {
        return "§8Rankup";
    }

    @Override
    public MapButton getMapButton(Map map, int slot) {
        return map.getRankupMapButton(slot);
    }
}
