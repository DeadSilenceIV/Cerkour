package me.cerdax.cerkour.commands;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length > 1) {
            Player targetPlayer = null;
            targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer != null) {
                Profile profile = Cerkour.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId());
                if (args[0].equalsIgnoreCase("setrankup")) {
                    try {
                        profile.setRankUp(Integer.parseInt(args[2]));
                        player.sendMessage("§6§lCerkour§e> You set: §6" + targetPlayer.getName() + " §erank to: §6" + profile.getRankUp());
                        targetPlayer.setPlayerListName(RankUtils.getColoredRank(Cerkour.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId()).getRankUp()) + "§r " + targetPlayer.getDisplayName());
                    }
                    catch (NumberFormatException e) {
                        player.sendMessage("§6§lCerkour§e> You may only use whole numbers!");
                    }
                }
                else if (args[0].equalsIgnoreCase("setpoints")) {
                    try {
                        profile.setPoints(Integer.parseInt(args[2]));
                        player.sendMessage("§6§lCerkour§e> You set: §6" + targetPlayer.getName() + " §epoints to: §6" + profile.getPoints());
                    }
                    catch (NumberFormatException e) {
                        player.sendMessage("§6§lCerkour§e> You may only use whole numbers!");
                    }
                }
            }
            else {
                player.sendMessage("§6§lCerkour§e> Invalid player!");
            }
        }
        else {
            player.sendMessage("§6§lCerkour§e> Profile Subcommands: \n§e- §6§lsetrankup §e(player) (rank) \n§e- §6§lsetpoints §e(player) (points)");
        }
        return true;
    }
}
