package me.cerdax.cerkour.map.visualizer;

import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.visualizer.buttons.MapButton;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpeedRunMapsVisualizer extends MapVisualizer{

    public SpeedRunMapsVisualizer(UUID player) {
        super(player);
    }
    @Override
    public List<Map> getContent() {
        return mapManager.getAllMaps().stream().filter(map -> !map.isOS()).collect(Collectors.toList());
    }

    @Override
    public String getPagesTitle() {
        return "§8Speedrun";
    }

    @Override
    public MapButton getMapButton(Map map, int slot) {
        return map.getDefaultMapButton(slot);
    }
}
