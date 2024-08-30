package me.cerdax.cerkour;

import me.cerdax.cerkour.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cerkour extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
