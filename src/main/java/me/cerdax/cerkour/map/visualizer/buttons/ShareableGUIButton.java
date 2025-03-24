package me.cerdax.cerkour.map.visualizer.buttons;


import us.lynuxcraft.deadsilenceiv.dutilities.inventory.InteractiveInventory;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.ShareableButton;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.items.ShareableItem;

public abstract class ShareableGUIButton<T extends InteractiveInventory> extends GUIButton<ShareableItem> implements ShareableButton {
    public ShareableGUIButton(String name) {
        super(name);
    }

    public abstract void loadActions(T inventory);
}
