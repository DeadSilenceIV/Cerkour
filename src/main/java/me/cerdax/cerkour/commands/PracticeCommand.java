package me.cerdax.cerkour.commands;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.profile.Practice;
import me.cerdax.cerkour.profile.Profile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PracticeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (args.length == 1) {
                Map map = Cerkour.getInstance().getMapManager().getMapByName(args[0]);
                if (map != null) {
                    Practice practice = profile.getPractice();
                    if (practice.getIsEnabled()) {
                        profile.leavePractice();
                    }
                    else {
                        profile.joinMap(map);
                        profile.enterPractice(map);
                    }
                }
            }
        }
        return true;
    }
}
