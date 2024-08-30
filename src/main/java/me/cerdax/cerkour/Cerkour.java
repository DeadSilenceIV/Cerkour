package me.cerdax.cerkour;

import me.cerdax.cerkour.commands.GGCommand;
import me.cerdax.cerkour.listeners.PlayerJoinListener;
import me.cerdax.cerkour.listeners.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cerkour extends JavaPlugin {

    Cerkour instance;

    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {

    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }

    public void registerCommands() {
        getCommand("gg").setExecutor(new GGCommand());
    }

    public Cerkour getInstance() {
        return instance;
    }
}
