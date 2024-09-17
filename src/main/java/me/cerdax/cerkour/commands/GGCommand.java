package me.cerdax.cerkour.commands;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GGCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
            Bukkit.broadcastMessage(RankUtils.getColoredRank(profile.getRankUp()) + " §7" + player.getDisplayName() + "§8: §r" + String.format("%s%sG%s%sG", ChatColor.GOLD, ChatColor.BOLD, ChatColor.YELLOW, ChatColor.BOLD));
        }
        return true;
    }
}
