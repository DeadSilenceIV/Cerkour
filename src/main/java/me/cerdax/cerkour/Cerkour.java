package me.cerdax.cerkour;

import me.cerdax.cerkour.commands.*;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.listeners.*;
import me.cerdax.cerkour.map.MapManager;
import me.cerdax.cerkour.profile.ProfileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cerkour extends JavaPlugin {

    private static Cerkour instance;
    private MapManager mapManager;
    private ProfileManager profileManager;

    @Override
    public void onEnable() {
        instance = this;
        registerManagers();
        registerCommands();
        registerListeners();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        CustomFiles.setup("maps");
        CustomFiles.getCustomFile("maps").options().copyDefaults(true);

        CustomFiles.saveAllCustomFiles();
    }

    @Override
    public void onDisable() {

    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerSprintListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
    }

    public void registerCommands() {
        getCommand("map").setExecutor(new MapCommand());
        getCommand("gg").setExecutor(new GGCommand());
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
    }

    public void registerManagers() {
        mapManager = new MapManager();
        profileManager = new ProfileManager();
    }

    public static Cerkour getInstance() {
        return instance;
    }

    public MapManager getMapManager() {
        return this.mapManager;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }
}
