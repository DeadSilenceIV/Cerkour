package me.cerdax.cerkour.listeners;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.managers.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.InteractiveInventory;

public class InventoryListener implements Listener{
    private final InventoryManager inventoryManager;
    public InventoryListener(){
        inventoryManager = Cerkour.getInstance().getInventoryManager();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        InteractiveInventory interactiveInventory = inventoryManager.getInteractiveByBukkit(player.getOpenInventory().getTopInventory());
        if (interactiveInventory != null) {
            interactiveInventory.handleInventoryInteraction(event);
        }
    }
}
