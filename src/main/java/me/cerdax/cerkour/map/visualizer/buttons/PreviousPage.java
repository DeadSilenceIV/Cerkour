package me.cerdax.cerkour.map.visualizer.buttons;

import me.cerdax.cerkour.map.visualizer.MapVisualizer;
import me.cerdax.cerkour.map.visualizer.MapVisualizerPage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.lynuxcraft.deadsilenceiv.dutilities.builders.SkullBuilder;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.InteractiveInventory;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.actions.SlotAction;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.items.ShareableButtonItem;

public class PreviousPage extends ShareableGUIButton<MapVisualizerPage> {
    public PreviousPage() {
        super("previous-page");
        build();
    }

    @Override
    public void build() {
        ItemStack main = new SkullBuilder("MHF_ArrowLeft").setName("&6&lPrevious page").build();
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
                inventory.openPage(player,  page.getId()-1);
            }
        });
        page.addAction(new SlotAction(ClickType.SHIFT_LEFT,slot) {
            @Override
            public void execute(Player player) {
                inventory.openPage(player, 0);
            }
        });
    }

    @Override
    public int getSlot(InteractiveInventory inventory) {
        return inventory.getBukkitInventory().getSize()-6;
    }
}
