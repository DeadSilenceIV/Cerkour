package me.cerdax.cerkour.files;

import me.cerdax.cerkour.Cerkour;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.function.Supplier;

public class Configuration {
    public static Supplier<String> SCOREBOARD_DEFAULT_TITLE = () -> getConfig().getString("scoreboards.default.title");
    public static Supplier<List<String>> SCOREBOARD_DEFAULT_LINES = () -> getConfig().getStringList("scoreboards.default.lines");
    public static Supplier<String> SCOREBOARD_IN_GAME_TITLE = () -> getConfig().getString("scoreboards.in-game.title");
    public static Supplier<List<String>> SCOREBOARD_IN_GAME_LINES = () -> getConfig().getStringList("scoreboards.in-game.lines");
    private static FileConfiguration getConfig() {
        return Cerkour.getInstance().getConfig();
    }

}