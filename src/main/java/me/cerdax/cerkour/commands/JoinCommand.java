package me.cerdax.cerkour.commands;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.profile.Profile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (args.length > 0) {
                Map map = Cerkour.getInstance().getMapManager().getMapByName(args[0]);
                if (map != null) {
                    if ((map.getIsRankUp() && map.getRankUp() <= profile.getRankUp()) || !map.getIsRankUp()) {
                        profile.joinMap(map, player);
                    }
                    else {
                        player.sendMessage("§6§lCerkour§e> You may not join this map");
                    }
                }
                else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
            }
        }
        return true;
    }
}
