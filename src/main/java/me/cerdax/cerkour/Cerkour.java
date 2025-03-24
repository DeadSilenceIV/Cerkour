package me.cerdax.cerkour;

import me.cerdax.cerkour.commands.*;
import me.cerdax.cerkour.database.DatabaseManager;
import me.cerdax.cerkour.database.MysqlStorage;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.listeners.*;
import me.cerdax.cerkour.managers.InventoryManager;
import me.cerdax.cerkour.map.CheckPoint;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.MapManager;
import me.cerdax.cerkour.map.TickTimer;
import me.cerdax.cerkour.profile.ProfileManager;
import me.cerdax.cerkour.tasks.SaveTask;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.InteractiveInventory;

public final class Cerkour extends JavaPlugin {

    private static Cerkour instance;
    private PAPIExpansion expansion;
    private InventoryManager inventoryManager;
    private MysqlStorage storage;
    private MapManager mapManager;
    private ProfileManager profileManager;
    private BukkitTask task;
    private SaveTask saveTask;
    private BukkitAudiences adventure;

    public BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();

        instance = this;
        expansion = new PAPIExpansion();
        expansion.register();
        ConfigurationSerialization.registerClass(CheckPoint.class,"CheckPoint");
        ConfigurationSerialization.registerClass(TickTimer.class,"TickTimer");
        registerManagers();
        registerCommands();
        registerListeners();

        getConfig().options().copyDefaults(true);

        CustomFiles.setup("maps");
        CustomFiles.getCustomFile("maps").options().copyDefaults(true);

        CustomFiles.setup("profiles");
        CustomFiles.getCustomFile("profiles").options().copyDefaults(true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
        }
        this.adventure = BukkitAudiences.create(this);
        saveTask = new SaveTask();
    }

    @Override
    public void onDisable() {
        expansion.unregister();
        if (task != null) {
            task.cancel();
        }
        for (InteractiveInventory inventory : inventoryManager.getInventories().values()) {
            inventory.closeForViewers();
        }
        for (Map map : mapManager.getAllMaps()) {
            map.save();
        }
        storage.stopServices();
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(getProfileManager()), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerSprintListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(),this);
    }

    public void registerCommands() {
        getCommand("map").setExecutor(new MapCommand());
        getCommand("gg").setExecutor(new GGCommand());
        getCommand("gl").setExecutor(new GLCommand());
        getCommand("rip").setExecutor(new RIPCommand());
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
        getCommand("profile").setExecutor(new ProfileCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("leaderboard").setExecutor(new LeaderboardCommand());
        getCommand("practice").setExecutor(new PracticeCommand());
    }

    public void registerManagers() {
        inventoryManager = new InventoryManager();
        storage = new MysqlStorage();
        mapManager = new MapManager();

        DatabaseManager databaseManager = new DatabaseManager(getConfig());
        profileManager = new ProfileManager(databaseManager);

        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> profileManager.saveAll(), 20, 20);
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

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public BukkitAudiences getAdventure() {
        return this.adventure;
    }

    public MysqlStorage getStorage() {
        return this.storage;
    }
}
