package me.cerdax.cerkour.tasks;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.cerdax.cerkour.Cerkour;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PluginTask extends BukkitRunnable {
    protected Cerkour plugin;
    @Getter
    protected Integer taskID;
    @Setter(AccessLevel.PACKAGE)
    private boolean sync;

    public PluginTask() {
        plugin = Cerkour.getInstance();
        sync = true;
    }

    protected void runTask(long executeTime, long delay) {
        if (sync) {
            taskID = runTaskTimer(plugin, delay, executeTime).getTaskId();
        } else {
            taskID = runTaskTimerAsynchronously(plugin, delay, executeTime).getTaskId();
        }
    }

    /**
     * Stops the task if it was previously initiated.
     */
    public void stopTask() {
        if (taskID != null) plugin.getServer().getScheduler().cancelTask(taskID);
    }

}
