package me.cerdax.cerkour.map.visualizer;

import lombok.Getter;
import lombok.Setter;
import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.managers.InventoryManager;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.MapManager;
import me.cerdax.cerkour.map.visualizer.buttons.MapButton;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import us.lynuxcraft.deadsilenceiv.dutilities.Pair;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.MultiPagesInventory;

import java.util.*;
import java.util.stream.Collectors;


public abstract class MapVisualizer extends MultiPagesInventory<MapVisualizerPage>{
    protected final Cerkour plugin;
    protected final MapManager mapManager;
    protected final InventoryManager inventoryManager;
    @Getter protected UUID player;
    @Getter @Setter protected long lastTimeOpened;
    @Getter @Setter protected Long lastTimeSorted;
    @Getter protected final long forceRefreshInterval;
    @Getter protected int contentAmount;
    @Getter @Setter protected boolean forceRefresh;
    public MapVisualizer(UUID player) {
        plugin = Cerkour.getInstance();
        mapManager = plugin.getMapManager();
        inventoryManager = plugin.getInventoryManager();
        this.player = player;
        lastTimeOpened = System.currentTimeMillis();
        this.forceRefreshInterval = 1000L;
        forceRefresh = false;
        contentAmount = getContent().size();
        int initialSize = (int) (Math.ceil((double) contentAmount/45.0)*45);
        createPages(initialSize);
        loadPages();
        refresh(false,SortType.DEFAULT);
    }

    @Override
    public void openPage(HumanEntity entity, int id) {
        lastTimeOpened = System.currentTimeMillis();
        super.openPage(entity, id);
    }

    @Override
    protected MapVisualizerPage newPage(int id, int size) {
        return new MapVisualizerPage(id,size,this,getPagesTitle());
    }

    public void refreshIfPossible(){
        if (forceRefresh || (System.currentTimeMillis() - lastTimeOpened >= forceRefreshInterval
                && getContent().size() != getContentAmount())) {
            refresh();
            forceRefresh = false;
        }
    }

    public void refresh(){
        refresh(true,SortType.DEFAULT);
    }

    public void refresh(SortType sortType){
        refresh(true,sortType);
    }


    protected void refresh(boolean resize,SortType sortType){
        List<Map> maps = getOrderedContent(sortType);
        contentAmount = maps.size();
        if(resize) {
            int newSize = (int) (Math.ceil((double)maps.size() / 45.0)*45);
            MapVisualizerPage lastPageBeforeResize = pages.get(pages.size()-1);
            Pair<ResizeResult, Set<MapVisualizerPage>> pair = resize(newSize);
            ResizeResult result = pair.getKey();
            if(result == ResizeResult.ADDED_PAGES) {
                Set<MapVisualizerPage> modified = pair.getValue();
                for (MapVisualizerPage page : modified) {
                    page.setupPage();
                }
                lastPageBeforeResize.loadNextPageButtons(true);
            }else if(result == ResizeResult.REMOVED_PAGES){
                pages.get(pages.size()-1).loadNextPageButtons(true);
            }
            /*else if(result == ResizeResult.LAST_PAGE_SLOTS_MODIFIED){
                pages.get(pages.size()-1).setupPage();
            }*/
        }
        int mapsSize = maps.size()-1;
        int aux = 0;
        for (Integer id : getPages().keySet()) {
            Set<MapButton> toRefresh = new HashSet<>();
            MapVisualizerPage page = getPageById(id);
            Inventory inventory = page.getBukkitInventory();
            int limit = inventory.getSize()-10;
            for(int i = 0; i <= limit; i++) {
                page.clearSlotActions(i);
                inventory.clear(i);
            }
            for (int i = 0; i <= limit;i++) {
                if(aux <= mapsSize) {
                    Map map = maps.get(aux);
                    MapButton button = getMapButton(map,i);
                    button.loadActions(page);
                    toRefresh.add(button);
                    aux++;
                }else{
                    break;
                }
            }
            page.refreshAsync(toRefresh);
        }
    }

    public List<Map> getOrderedContent(SortType sortType){
        if(sortType == SortType.DIFFICULTY){
            return getContent().stream().sorted(Comparator.comparing(Map::getDifficulty)).collect(Collectors.toList());
        }
        return getContent();
    }

    public abstract List<Map> getContent();

    public abstract String getPagesTitle();

    public abstract MapButton getMapButton(Map map, int slot);

    @Override
    protected InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public enum SortType{
        DEFAULT,
        DIFFICULTY
    }
}

