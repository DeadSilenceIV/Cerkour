package me.cerdax.cerkour.commands;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaderboardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 1) {
                Map map = Cerkour.getInstance().getMapManager().getMapByName(args[0]);
                if (map != null) {
                    player.sendMessage(Cerkour.getInstance().getMapManager().getLeaderboardString(map));
                }
                else {
                    player.sendMessage("§6§lCerkour§e> Invalid map!");
                }
            }
            else {
                player.sendMessage("§6§lCerkour§e> Please specify map");
            }

        }
        return true;
    }
}
