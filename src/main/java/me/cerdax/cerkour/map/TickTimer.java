package me.cerdax.cerkour.map;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.ActionBarUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TickTimer {

    private long ticks;
    private long stashedTicks;
    private long best;
    private boolean isRunning;
    private static final double TICKS_PER_SECOND = 20.0;
    private UUID playerUUID;

    public TickTimer(UUID playerUUID) {
        this.ticks = 0;
        this.isRunning = false;
        this.playerUUID = playerUUID;
        this.best = 0;
        this.stashedTicks = 0;
    }

    public long getTicks() {
        return this.ticks;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public String getPlayerName() {
        return Bukkit.getPlayer(this.playerUUID).getName();
    }

    public Profile getProfile(Player player) {
        return Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
    }

    public long getBest() {
        return this.best;
    }

    public long getStashedTicks() {
        return this.stashedTicks;
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

    public void setStashedTicks() {
        this.stashedTicks = getTicks();
    }

    public void clearStashedTicks() {
        this.stashedTicks = 0;
    }

    public void resetTimer() {
        this.ticks = 0;
    }

    public void start(Player player) {
        if (!getIsRunning()) {
            this.isRunning = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (getIsRunning()) {
                        addTick();
                        ActionBarUtils.sendActionbar(player, "§e§l" + getTimeFromTicks(getTicks()));
                    }
                    else {
                        cancel();
                    }
                }
            }.runTaskTimer(Cerkour.getInstance(), 0L, 1L);
        }
    }

    public void stop(Player player) {
        if (getIsRunning()) {
            ActionBarUtils.sendActionbar(player, " ");
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