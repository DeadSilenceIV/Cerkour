package me.cerdax.cerkour.map.visualizer;

import lombok.Getter;
import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.managers.InventoryManager;
import me.cerdax.cerkour.map.visualizer.buttons.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.lynuxcraft.deadsilenceiv.dutilities.ItemUtils;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.InventoryPage;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.actions.SlotAction;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.items.ShareableButtonItem;

import java.util.HashSet;
import java.util.Set;

public class MapVisualizerPage extends InventoryPage {
    protected final Cerkour plugin;
    protected final InventoryManager manager;
    @Getter protected final MapVisualizer owner;
    private static final ItemStack background = ItemUtils.getInventoryBackground((short)15, " ");
    public MapVisualizerPage(int id, int size, MapVisualizer owner,String title) {
        super(id, size);
        this.plugin = Cerkour.getInstance();
        this.manager = plugin.getInventoryManager();
        this.owner = owner;
    }

    @Override
    public void setupPage() {
        buildInventory();
        register();
    }

    public void buildInventory(){
        int inventorySize = size+9;
        inventory = Bukkit.createInventory(null, inventorySize, owner.getPagesTitle());
        prepareBottomRow();
    }

    public void prepareBottomRow() {
        for (int i = 1; i <= 9; i++) {
            inventory.setItem(inventory.getSize() - i, background);
        }
        loadNextPageButtons(false);
        loadRefreshButton();
        loadSortByDifficultyButton();
    }

    private void loadRefreshButton(){
        RefreshButton refresh = manager.getRefreshButton();
        addButton(refresh);
        refresh.loadActions(this);
        refresh.show("main",this);
    }

    private void loadSortByDifficultyButton(){
        SortByDifficulty sortByPrice = manager.getSortByDifficulty();
        addButton(sortByPrice);
        sortByPrice.loadActions(this);
        sortByPrice.show("main",this);
    }

    protected void loadNextPageButtons(boolean replace){
        int inventorySize = inventory.getSize();
        if(replace) {
            int previousSlot = inventorySize-6;
            int nextSlot = inventorySize-4;
            inventory.setItem(previousSlot,background);
            inventory.setItem(nextSlot,background);
            clearSlotActions(previousSlot,nextSlot);
        }
        if(owner.getPages().size() > 1) {
            if (id == 0) {
                NextPage nextPageButton = manager.getNextPageButton();
                addButton(nextPageButton);
                nextPageButton.loadActions(this);
                nextPageButton.show("main",this);
            }
            if (id > 0 && id < owner.getPages().size() - 1) {
                PreviousPage previousPageButton = manager.getPreviousPageButton();
                addButton(previousPageButton);
                previousPageButton.loadActions(this);
                previousPageButton.show("main",this);
                NextPage nextPageButton = manager.getNextPageButton();
                addButton(nextPageButton);
                nextPageButton.loadActions(this);
                nextPageButton.show("main",this);
            }
            if (id == (owner.getPages().size() - 1)) {
                PreviousPage previousPageButton = manager.getPreviousPageButton();
                addButton(previousPageButton);
                previousPageButton.loadActions(this);
                previousPageButton.show("main",this);
            }
        }
    }

    public void refreshAsync(Set<MapButton> content){
        if(content.isEmpty()){
            return;
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,() -> {
            Set<ShareableButtonItem> toSet = new HashSet<>();
            for (ShareableGUIButton<MapVisualizerPage> button : content) {
                ShareableButtonItem currentItem = (ShareableButtonItem)button.getCurrentItem();
                ItemMeta updated = currentItem.getUpdatedMeta();
                currentItem.getItemStack().setItemMeta(updated);
                toSet.add(currentItem);
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                for (ShareableButtonItem currentItem : toSet) {
                    currentItem.setItem(this);
                    currentItem.setInitiallyRefreshed(true);
                }
            });
        },1L);
    }

    @Override
    public void handleInventoryInteraction(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        int slot = event.getSlot();
        ClickType clickType = event.getClick();
        ItemStack currentItem = event.getCurrentItem();
        event.setCancelled(true);
        if(clickedInventory != null && clickedInventory.equals(getBukkitInventory()) && currentItem != null) {
            SlotAction slotAction = getSlotAction(slot,clickType);
            if(slotAction != null){
                slotAction.execute(player);
            }
        }
    }
}
