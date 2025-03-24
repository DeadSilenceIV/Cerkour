package me.cerdax.cerkour.map.visualizer.buttons;

import me.cerdax.cerkour.map.visualizer.MapVisualizer;
import me.cerdax.cerkour.map.visualizer.MapVisualizerPage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.lynuxcraft.deadsilenceiv.dutilities.builders.ItemBuilder;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.InteractiveInventory;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.actions.SlotAction;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.items.ShareableButtonItem;

import java.util.ArrayList;
import java.util.Arrays;

public class SortByDifficulty extends ShareableGUIButton<MapVisualizerPage> {

    public SortByDifficulty() {
        super("sort-by-price");
        build();
    }

    @Override
    public void build() {
        ItemStack item = new ItemBuilder(Material.HOPPER).setName("&6&lSort by difficulty").setLore(new ArrayList<>(Arrays.asList("&7Click here to sort by difficulty."))).build();
        ShareableButtonItem mainItem = new ShareableButtonItem(this,"main",item);
        addItem(mainItem);
    }

    @Override
    public int getSlot(InteractiveInventory inventory) {
        return inventory.getBukkitInventory().getSize()-1;
    }

    @Override
    public void loadActions(MapVisualizerPage page) {
        MapVisualizer inventory = page.getOwner();
        int slot = getSlot(page);
        page.addAction(new SlotAction(ClickType.LEFT,slot) {
            @Override
            public void execute(Player player) {
                Long current = System.currentTimeMillis();
                Long lastTime = inventory.getLastTimeSorted();
                if(lastTime == null || current-lastTime >= 10000){
                    inventory.refresh(MapVisualizer.SortType.DIFFICULTY);
                    inventory.openPage(player, 0);
                    inventory.setLastTimeSorted(current);
                }
            }
        });
    }

}
