package me.cerdax.cerkour.tasks;

import me.cerdax.cerkour.map.Map;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class SaveTask extends PluginTask {

    public SaveTask() {
        setSync(true);
        runTask(6000L, 1200L);
    }

    @Override
    public void run() {
        List<Map> maps = new ArrayList<>(plugin.getMapManager().getAllMaps());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Map map : maps) {
                map.save();
            }
        });
    }

}
