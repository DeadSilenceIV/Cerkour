package me.cerdax.cerkour.map.visualizer.buttons;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.managers.InventoryManager;
import me.cerdax.cerkour.map.MapManager;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.BaseButton;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.items.InteractiveItem;

public abstract class GUIButton<T extends InteractiveItem> extends BaseButton<T> {
    protected Cerkour plugin;
    protected final InventoryManager inventoryManager;
    protected final MapManager mapManager;
    public GUIButton(String name) {
        super(name);
        plugin = Cerkour.getInstance();
        inventoryManager = plugin.getInventoryManager();
        mapManager = plugin.getMapManager();
    }

    public abstract void build();

    protected void loadPlaceholders(){}

}
