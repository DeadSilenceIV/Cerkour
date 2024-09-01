package me.cerdax.cerkour.map;

import java.util.ArrayList;
import java.util.List;

public class MapManager {

    private List<Map> maps;

    public MapManager() {
        this.maps = new ArrayList<>();
    }

    public Map createMap(String name) {
        Map map = new Map(name);
        this.maps.add(map);
        return map;
    }

    public void removeMap(String name) {
        maps.removeIf(map -> map.getName().equalsIgnoreCase(name));
    }

    public Map getMapByName(String name) {
        return this.maps.stream().filter(map -> map.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }
}
