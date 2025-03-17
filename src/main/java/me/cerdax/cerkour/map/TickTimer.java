package me.cerdax.cerkour.map;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.ActionBarUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

public class TickTimer {
    private long ticks;
    private long stashedTicks;
    private long best;
    private boolean isRunning;
    private SimpleDateFormat secondsFormat;
    private SimpleDateFormat minuteFormat;
    private SimpleDateFormat hourFormat;
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

    public void tickTimer() {
        ticks++;
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
                        tickTimer();
                        if(ticks%3 == 0) {
                            ActionBarUtils.sendActionbar(player, "§e§l" + getTimeFromTicks(getTicks()));
                        }
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimerAsynchronously(Cerkour.getInstance(), 1L, 1L);
        }
    }

    public void stop(Player player) {
        if (getIsRunning()) {
            this.isRunning = false;
            ActionBarUtils.sendActionbar(player, " ");
        }
    }

    public String getTimeFromTicks(long ticks) {
        long millis = (5*(Math.round((double)ticks/5)))*50;
        String formatted;
        if (ticks >= 20*60) {
            if (ticks >= 20*60*60) {
                formatted = getHourFormat().format(millis);
            }else{
                formatted = getMinuteFormat().format(millis);
            }
        }else{
            formatted = getSecondFormat().format(millis);
        }
        return formatted.substring(0, formatted.length()-1);
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    private SimpleDateFormat getSecondFormat(){
        if(secondsFormat == null){
            secondsFormat = new SimpleDateFormat("ss:SSS");
            secondsFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return secondsFormat;
    }

    private SimpleDateFormat getMinuteFormat(){
        if(minuteFormat == null){
            minuteFormat = new SimpleDateFormat("mm:ss:SSS");
            minuteFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return minuteFormat;
    }

    private SimpleDateFormat getHourFormat(){
        if(hourFormat == null){
            hourFormat = new SimpleDateFormat("HH:mm:ss:SSS");
            hourFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return hourFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TickTimer tickTimer = (TickTimer) o;
        return Objects.equals(playerUUID, tickTimer.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerUUID);
    }
}