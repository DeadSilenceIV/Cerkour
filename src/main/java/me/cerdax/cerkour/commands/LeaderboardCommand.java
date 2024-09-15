package me.cerdax.cerkour.commands;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.TickTimer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class LeaderboardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Map map = Cerkour.getInstance().getMapManager().getMapByName(args[0]);
            if (map != null) {
                player.sendMessage(Cerkour.getInstance().getMapManager().getLeaderboardString(map));
            }
            else {
                player.sendMessage("§6§lCerkour§e> Invalid map!");
            }
        }
        return true;
    }
}
