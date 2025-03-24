package me.cerdax.cerkour.map.visualizer.buttons;

import me.cerdax.cerkour.map.Map;

public class DefaultMapButton extends MapButton {
    public DefaultMapButton(Map map) {
        super(map);
    }

    @Override
    public String getItemName() {
        return "&e"+map.getName();
    }
}
