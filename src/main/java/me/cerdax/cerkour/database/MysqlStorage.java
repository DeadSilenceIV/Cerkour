package me.cerdax.cerkour.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Cleanup;
import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.CheckPoint;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.TickTimer;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MysqlStorage {
    private Cerkour plugin;
    private FileConfiguration config;
    private HikariDataSource hikari;
    private final ExecutorService executor;
    private RowSetFactory rowSetFactory;
    public MysqlStorage(){
        plugin = Cerkour.getInstance();
        config = plugin.getConfig();
        executor = Executors.newSingleThreadExecutor();
        try {
            rowSetFactory = RowSetProvider.newFactory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connect();
    }

    /**
     * Establishes the connection to the database.
     */
    private void connect() {
        HikariConfig config = getHikariConfig();
        hikari = new HikariDataSource(config);
        try(Connection connection = hikari.getConnection()) {
            executeQuery("CREATE TABLE IF NOT EXISTS maps(uuid CHAR(36) NOT NULL,name VARCHAR(255) NOT NULL,rankup INT NOT NULL,spawn VARCHAR(255),end VARCHAR(255),checkpoints BLOB NOT NULL,state INT NOT NULL,difficulty INT NOT NULL,timers BLOB NOT NULL,death_blocks BLOB NOT NULL,PRIMARY KEY (uuid))");
            plugin.getServer().getConsoleSender().sendMessage("§aConnection to the database established!");
        } catch (Exception e) {
            plugin.getServer().getConsoleSender().sendMessage("§4Error trying to connect to the database!");
            Bukkit.getScheduler().runTaskLater(plugin,() -> plugin.getServer().getPluginManager().disablePlugin(plugin),1L);
        }
    }

    private @NotNull HikariConfig getHikariConfig() {
        Properties properties = new Properties();
        properties.setProperty("dataSource.user", config.getString("mysql-data.username"));
        properties.setProperty("dataSource.password", config.getString("mysql-data.password"));
        HikariConfig config = new HikariConfig(properties);
        config.setJdbcUrl("jdbc:mysql://"+this.config.getString("mysql-data.host")+":"+this.config.getInt("mysql-data.port")+"/"+this.config.getString("mysql-data.database"));
        config.setMaximumPoolSize(10);
        config.addDataSourceProperty("properties", "useUnicode=true;characterEncoding=utf8");
        return config;
    }

    public void save(Map map){
        String spawn = map.getStartLocation() != null ? LocationUtils.locationToString(map.getStartLocation()) : null;
        String end = map.getEndLocation() != null ? LocationUtils.locationToString(map.getEndLocation()) : null;
        byte[] serializedCheckPoints = serializeCheckPoint(map);
        byte[] serializedTimers = serializeTickTimers(map);
        byte[] deathBlocks = serializeDeathBlocks(map);
        executeQuery("INSERT INTO maps(uuid,name,rankup,spawn,end,checkpoints,state,difficulty,timers,death_blocks) VALUES (?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE uuid = ?,name = ?,rankup = ?,spawn = ?,end = ?,checkpoints = ?,state = ?,difficulty = ?,timers = ?,death_blocks = ?",
                        map.getMapUUID().toString(),map.getName(),map.getRankUp(),spawn,end,serializedCheckPoints,map.getState(),map.getDifficulty(),serializedTimers,deathBlocks,map.getMapUUID().toString(),map.getName(),map.getRankUp(),spawn,end,serializedCheckPoints,map.getState(),map.getDifficulty(),serializedTimers,deathBlocks);
    }

    public byte[] serializeCheckPoint(Map map) {
        try {
            @Cleanup ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            @Cleanup BukkitObjectOutputStream bukkitOutputStream = new BukkitObjectOutputStream(outputStream);
            bukkitOutputStream.writeObject(map.getCheckpoints());
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CheckPoint> deserializeCheckPoint(byte[] serialized) {
        try {
            @Cleanup ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
            @Cleanup BukkitObjectInputStream bukkitInputStream = new BukkitObjectInputStream(inputStream);
            return (List<CheckPoint>)bukkitInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] serializeTickTimers(Map map) {
        try {
            @Cleanup ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            @Cleanup BukkitObjectOutputStream bukkitOutputStream = new BukkitObjectOutputStream(outputStream);
            bukkitOutputStream.writeObject(map.getTimers());
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TickTimer> deserializeTickTimers(byte[] serialized) {
        try {
            @Cleanup ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
            @Cleanup BukkitObjectInputStream bukkitInputStream = new BukkitObjectInputStream(inputStream);
            return (List<TickTimer>)bukkitInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] serializeDeathBlocks(Map map) {
        try {
            @Cleanup ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            @Cleanup BukkitObjectOutputStream bukkitOutputStream = new BukkitObjectOutputStream(outputStream);
            List<String> materials = new ArrayList<>();
            for (Material material : map.getDeathBlocks()) {
                materials.add(material.name());
            }
            bukkitOutputStream.writeObject(materials);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Material> deserializeDeathBlocks(byte[] serialized) {
        try {
            @Cleanup ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
            @Cleanup BukkitObjectInputStream bukkitInputStream = new BukkitObjectInputStream(inputStream);
            List<Material> materials = new ArrayList<>();
            List<String> strings = (List<String>)bukkitInputStream.readObject();
            for (String string : strings) {
                materials.add(Material.valueOf(string));
            }
            return materials;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Map map){
        CompletableFuture.runAsync(() -> executeQuery(true,"DELETE FROM maps WHERE uuid = ?",map.getMapUUID().toString()));
    }

    public CompletableFuture<List<Map>> getMaps(){
        return CompletableFuture.supplyAsync(() -> {
            List<Map> maps = new ArrayList<>();
            CachedRowSet result = executeQuery("SELECT * FROM maps");
            try {
                while(result.next()){
                    UUID uniqueId = UUID.fromString(result.getString("uuid"));
                    String name = result.getString("name");
                    int rankUp = result.getInt("rankup");
                    String spawn = result.getString("spawn");
                    String end = result.getString("end");
                    List<CheckPoint> checkPoints = deserializeCheckPoint(result.getBytes("checkpoints"));
                    int state = result.getInt("state");
                    int difficulty = result.getInt("difficulty");
                    List<TickTimer> timers = deserializeTickTimers(result.getBytes("timers"));
                    List<Material> deathBlocks = deserializeDeathBlocks(result.getBytes("death_blocks"));
                    maps.add(new Map(uniqueId,name,spawn != null ? LocationUtils.stringToLocation(spawn) : null,end != null ? LocationUtils.stringToLocation(end) : null,rankUp,checkPoints,timers,state,difficulty,deathBlocks));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            return maps;
        });
    }

    /**
     * Executes a defined query, this method won't ignore exceptions
     * {@link #executeQuery(boolean, String, Object...)}
     *
     * @param query the query to execute
     * @return a cached row set in case there is a result from the query, null otherwise.
     */
    private CachedRowSet executeQuery(String query, Object... objects){
        return executeQuery(false,query,objects);
    }

    /**
     * Executes a defined query.
     *
     * @param ignoreExceptions if the exceptions should be ignored or not.
     * @param query the query to execute
     * @return a cached row set in case there is a result from the query, null otherwise.
     */
    private CachedRowSet executeQuery(boolean ignoreExceptions,String query, Object... objects){
        CachedRowSet cachedRowSet = null;
        try (Connection connection = hikari.getConnection();PreparedStatement pStatement = connection.prepareStatement(query)){
            for (int i = 0; i < objects.length; i++) {
                pStatement.setObject(i+1,objects[i]);
            }
            pStatement.execute();
            ResultSet resultSet = pStatement.getResultSet();
            if(resultSet != null) {
                cachedRowSet = rowSetFactory.createCachedRowSet();
                cachedRowSet.populate(resultSet);
            }
        }catch (Exception e){
            if(!ignoreExceptions) {
                e.printStackTrace();
            }
        }
        return cachedRowSet;
    }

    public void stopServices() {
        hikari.close();
        executor.shutdown();
    }

}
