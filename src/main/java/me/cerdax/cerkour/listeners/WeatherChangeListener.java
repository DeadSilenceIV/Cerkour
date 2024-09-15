package me.cerdax.cerkour.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.getWorld().hasStorm()) {
            e.getWorld().setStorm(false);
        }
        if (e.getWorld().isThundering()) {
            e.getWorld().setThundering(false);
        }
        e.setCancelled(true);
    }
}
