package me.cerdax.cerkour.scoreboard;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.profile.Profile;
import me.cerdax.cerkour.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Board implements Runnable{

    public final static Board instance = new Board();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getScoreboard() != null && player.getScoreboard().getObjective("Cerkour") != null) {
                updateScoreBoard(player);
            }
            else {
                createNewScoreBoard(player);
            }
        }
    }

    private void createNewScoreBoard(Player player) {
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Cerkour", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lCerkour");

        objective.getScore(ChatColor.WHITE + "").setScore(10);
        objective.getScore("§6§lName").setScore(9);
        objective.getScore("§e" + player.getName()).setScore(8);
        objective.getScore(ChatColor.RED + "").setScore(7);
        objective.getScore("§6§lRank").setScore(6);
        objective.getScore(ChatColor.BLACK + "").setScore(4);
        objective.getScore("§6§lOnline").setScore(3);
        objective.getScore(ChatColor.DARK_GREEN + "").setScore(1);
        objective.getScore("§6play.cerkour.net").setScore(0);

        Team team1 = scoreboard.registerNewTeam("team1");
        String teamKey = ChatColor.GOLD.toString();

        Team team2 = scoreboard.registerNewTeam("team2");
        String teamKey2 = ChatColor.AQUA.toString();

        team1.addEntry(teamKey);
        team1.setPrefix("");
        team1.setSuffix("§e" + Bukkit.getOnlinePlayers().size());

        team2.addEntry(teamKey2);
        team2.setPrefix("");
        team2.setSuffix(RankUtils.getColoredRank(profile.getRankUp()));

        objective.getScore(teamKey2).setScore(5);
        objective.getScore(teamKey).setScore(2);

        player.setScoreboard(scoreboard);
    }

    private void updateScoreBoard(Player player) {
        Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Scoreboard scoreboard = player.getScoreboard();
        Team team1 = scoreboard.getTeam("team1");
        Team team2 = scoreboard.getTeam("team2");

        team1.setSuffix("§e" + Bukkit.getOnlinePlayers().size());
        team2.setSuffix(RankUtils.getColoredRank(profile.getRankUp()));
    }

    public static Board getInstance() {
        return instance;
    }
}
