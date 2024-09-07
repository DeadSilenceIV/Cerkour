package me.cerdax.cerkour.utils;

import me.cerdax.cerkour.Cerkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class LocationUtils {

    public static String locationToString(final Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location
                .getYaw() + "," + location.getPitch();
    }

    public static Location stringToLocation(final String string) {
        final String[] split = string.split(",");
        World world = Bukkit.getWorld(split[0]);
        WorldCreator load = new WorldCreator(split[0]);
        world = load.createWorld();
        return new Location(world, Double.parseDouble(split[1]),
                Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    public static Location getSpawn() {
        String spawnStr = Cerkour.getInstance().getConfig().getString("spawn");
        return stringToLocation(spawnStr);
    }

}
