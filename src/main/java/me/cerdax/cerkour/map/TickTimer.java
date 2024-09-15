package me.cerdax.cerkour.map;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.ActionBarUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TickTimer {

    private long ticks;
    private long best;
    private boolean isRunning;
    private static final double TICKS_PER_SECOND = 20.0;
    private String playerName;

    public TickTimer(String playerName) {
        this.ticks = 0;
        this.isRunning = false;
        this.playerName = playerName;
        this.best = 0;
    }

    public long getTicks() {
        return this.ticks;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Profile getProfile(Player player) {
        return Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
    }

    public long getBest() {
        return this.best;
    }

    public void setBest(long ticks) {
        this.best = ticks;
    }

    public void addTick() {
        this.ticks++;
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public void resetTimer() {
        this.ticks = 0;
    }

    public void start(Player player) {
        if (!isRunning) {
            this.isRunning = true;
            new BukkitRunnable() {
                @Override

                public void run() {
                    if (isRunning) {
                        TickTimer.this.addTick();
                        ActionBarUtils.sendActionbar(player, "§e§l" + getTimeFromTicks(getTicks()));
                    }
                    else {
                        cancel();
                    }
                }
            }.runTaskTimer((Plugin) Cerkour.getInstance(), 0L, 1L);
        }
    }

    public void stop(Player player) {
        if (isRunning) {
            ActionBarUtils.sendActionbar(player, "§e§l" + getTimeFromTicks(getTicks()));
            this.isRunning = false;
        }
    }

    public String getTimeFromTicks(long ticks) {
        long totalHours = 0;
        long totalMinutes = 0;
        long totalSeconds = (long) ((ticks / TICKS_PER_SECOND));
        long remainingTicks = ticks % (int) TICKS_PER_SECOND;
        remainingTicks *= 5;

        if (ticks >= 20*60) {
            totalMinutes = totalSeconds / 60;
            totalSeconds = totalSeconds % 60;
            if (ticks >= 20*60*60) {
                totalHours = totalMinutes / 60;
                totalMinutes = totalMinutes % 60;
                return String.format("%04d:%02d:%02d.%02d", totalHours, totalMinutes, totalSeconds, remainingTicks);
            }
            else if (totalMinutes < 10) {
                return String.format("%01d:%02d.%02d", totalMinutes, totalSeconds, remainingTicks);
            }
            else {
                return String.format("%02d:%02d.%02d", totalMinutes, totalSeconds, remainingTicks);
            }
        }
        else {
            if (totalSeconds < 10) {
                return String.format("%01d.%02d", totalSeconds, remainingTicks);
            }
            else {
                return String.format("%02d.%02d", totalSeconds, remainingTicks);
            }
        }
    }

    public boolean getIsRunning() {
        return isRunning;
    }

}