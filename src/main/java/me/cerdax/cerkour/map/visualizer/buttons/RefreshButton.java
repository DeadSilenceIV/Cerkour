package me.cerdax.cerkour.map.visualizer.buttons;

import me.cerdax.cerkour.map.Map;
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

import java.util.List;

public class RefreshButton extends ShareableGUIButton<MapVisualizerPage> {
    public RefreshButton() {
        super("refresh");
        build();
    }

    @Override
    public void build() {
        ItemStack main = new ItemBuilder(Material.DOUBLE_PLANT).setDamage((short)0).setName("&6&lRefresh").build();
        ShareableButtonItem mainItem = new ShareableButtonItem(this,"main",main);
        addItem(mainItem);
    }

    @Override
    public void loadActions(MapVisualizerPage page) {
        MapVisualizer inventory = page.getOwner();
        int slot = getSlot(page);
        page.addAction(new SlotAction(ClickType.LEFT,slot) {
            @Override
            public void execute(Player player) {
                boolean forceRefresh = inventory.isForceRefresh();
                long lastTimeOpened = inventory.getLastTimeOpened();
                long forceRefreshInterval = inventory.getForceRefreshInterval();
                if (forceRefresh || (System.currentTimeMillis() - lastTimeOpened >= forceRefreshInterval)) {
                    List<Map> maps = inventory.getContent();
                    if(!maps.isEmpty()) {
                        if(maps.size() == inventory.getContentAmount()) {
                            return;
                        }
                        inventory.refresh();
                        inventory.setForceRefresh(false);
                        inventory.openPage(player, 0);
                    }else{
                        player.closeInventory();
                    }
                }
            }
        });
    }

    @Override
    public int getSlot(InteractiveInventory inventory) {
        return inventory.getBukkitInventory().getSize()-5;
    }
}
