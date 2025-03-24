package me.cerdax.cerkour.managers;


import me.cerdax.cerkour.map.visualizer.MapVisualizer;
import me.cerdax.cerkour.map.visualizer.MapVisualizerPage;
import me.cerdax.cerkour.map.visualizer.RankupMapsVisualizer;
import me.cerdax.cerkour.map.visualizer.SpeedRunMapsVisualizer;
import me.cerdax.cerkour.map.visualizer.buttons.NextPage;
import me.cerdax.cerkour.map.visualizer.buttons.PreviousPage;
import me.cerdax.cerkour.map.visualizer.buttons.RefreshButton;
import me.cerdax.cerkour.map.visualizer.buttons.SortByDifficulty;
import us.lynuxcraft.deadsilenceiv.dutilities.managers.InteractiveInventoryManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager extends InteractiveInventoryManager {
    private NextPage NextPageButton;
    private PreviousPage previousPageButton;
    private RefreshButton refreshButton;
    private SortByDifficulty sortByDifficulty;
    private Map<UUID, SpeedRunMapsVisualizer> speedRunMapsVisualizers;
    private Map<UUID, RankupMapsVisualizer> rankupMapsVisualizers;
    public InventoryManager() {
        speedRunMapsVisualizers = new HashMap<>();
        rankupMapsVisualizers = new HashMap<>();
    }

    public void register(SpeedRunMapsVisualizer visualizer){
        speedRunMapsVisualizers.put(visualizer.getPlayer(), visualizer);
    }

    public void unRegister(MapVisualizer visualizer){
        speedRunMapsVisualizers.remove(visualizer.getPlayer());
        for (MapVisualizerPage page : visualizer.getPages().values()) {
            unRegister(page);
        }
    }

    public SpeedRunMapsVisualizer getOrLoadSpeedRunMapsVisualizer(UUID player){
        SpeedRunMapsVisualizer visualizer = speedRunMapsVisualizers.get(player);
        if(visualizer == null){
            visualizer = new SpeedRunMapsVisualizer(player);
            register(visualizer);
        }
        return visualizer;
    }

    public SpeedRunMapsVisualizer getSpeedRunMapsVisualizer(UUID player){
        return speedRunMapsVisualizers.get(player);
    }

    public void register(RankupMapsVisualizer visualizer){
        rankupMapsVisualizers.put(visualizer.getPlayer(), visualizer);
    }

    public void unRegister(RankupMapsVisualizer visualizer){
        rankupMapsVisualizers.remove(visualizer.getPlayer());
        for (MapVisualizerPage page : visualizer.getPages().values()) {
            unRegister(page);
        }
    }

    public RankupMapsVisualizer getOrLoadRankupMapsVisualizer(UUID player){
        RankupMapsVisualizer visualizer = rankupMapsVisualizers.get(player);
        if(visualizer == null){
            visualizer = new RankupMapsVisualizer(player);
            register(visualizer);
        }
        return visualizer;
    }

    public RankupMapsVisualizer getRankupMapsVisualizer(UUID player){
        return rankupMapsVisualizers.get(player);
    }

    public NextPage getNextPageButton(){
        if(NextPageButton == null){
            NextPageButton = new NextPage();
        }
        return NextPageButton;
    }

    public PreviousPage getPreviousPageButton(){
        if(previousPageButton == null){
            previousPageButton = new PreviousPage();
        }
        return previousPageButton;
    }

    public RefreshButton getRefreshButton(){
        if(refreshButton == null){
            refreshButton = new RefreshButton();
        }
        return refreshButton;
    }

    public SortByDifficulty getSortByDifficulty(){
        if(sortByDifficulty == null){
            sortByDifficulty = new SortByDifficulty();
        }
        return sortByDifficulty;
    }

}
