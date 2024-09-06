package me.cerdax.cerkour.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static String locationToString(Location location) {
        if (location == null) {
            return null;
        }

        World world = location.getWorld();
        String worldName = world != null ? world.getName() : "unknown";

        return String.format("%s,%f,%f,%f,%f,%f",
                worldName,
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch());
    }

    public static Location stringToLocation(String strLocation) {
        if (strLocation == null || strLocation.isEmpty()) {
            return null;
        }

        String[] split = strLocation.split(",");
        if (split.length < 4) {
            return null;
        }

        World world = Bukkit.getWorld(split[0]);
        if (world == null) {
            return null;
        }

        double x, y, z;
        try {
            x = Double.parseDouble(split[1].trim());
            y = Double.parseDouble(split[2].trim());
            z = Double.parseDouble(split[3].trim());
        } catch (NumberFormatException e) {
            return null;
        }

        // Optional: Parse yaw and pitch if available
        float yaw = split.length > 4 ? Float.parseFloat(split[4].trim()) : 0f;
        float pitch = split.length > 5 ? Float.parseFloat(split[5].trim()) : 0f;

        return new Location(world, x, y, z, yaw, pitch);
    }

}
