package me.cerdax.cerkour.commands;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    Map map = Cerkour.getInstance().getMapManager().createMap(args[1]);
                    player.sendMessage("§6§lCerkour§e> You created the map: §6" + args[1]);
                }
                else if (args[0].equalsIgnoreCase("rankup")) {
                    Map map = Cerkour.getInstance().getMapManager().getMapByName(args[1]);
                    if (map != null) {
                        if (args.length < 3) {
                            if (map.getIsRankUp()) {
                                map.setRankUp(0);
                                player.sendMessage("§6§lCerkour§e> The map §6" + map.getName() + " §eis no longer rankup!");
                            }
                            else {
                                player.sendMessage("§6§lCerkour§e> Invalid usage!");
                            }
                        }
                        else {
                            map.setRankUp(Integer.parseInt(args[2]));
                            player.sendMessage("§6§lCerkour§e> The map §6" + map.getName() + " §eis now the rankup: §6" + map.getRankUp());
                        }
                    }
                    else {
                        player.sendMessage("§6§lCerkour§e> Map not found!");
                    }
                }
                else if (args[0].equalsIgnoreCase("setstart")) {
                    Map map = Cerkour.getInstance().getMapManager().getMapByName(args[1]);
                    if (map != null) {
                        map.setStartLocation(player.getLocation());
                        player.sendMessage("§6§lCerkour§e> You set the start of map: §6" + map.getName());
                    }
                    else {
                        player.sendMessage("§6§lCerkour§e> Map not found!");
                    }
                }
                else if (args[0].equalsIgnoreCase("setend")) {
                    Map map = Cerkour.getInstance().getMapManager().getMapByName(args[1]);
                    if (map != null) {
                        int x = player.getLocation().getBlockX();
                        int y = player.getLocation().getBlockY();
                        int z = player.getLocation().getBlockZ();
                        World world = player.getWorld();
                        map.setEndLocation(new Location(world, x, y, z));
                        player.sendMessage("§6§lCerkour§e> You set the end of map: §6" + map.getName());
                    }
                    else {
                        player.sendMessage("§6§lCerkour§e> Map not found!");
                    }
                }
                else if (args[0].equalsIgnoreCase("remove")) {
                    Map map = Cerkour.getInstance().getMapManager().getMapByName(args[1]);
                    if (map != null) {
                        player.sendMessage("§6§lCerkour§e> You removed the map §6" + map.getName());
                        Cerkour.getInstance().getMapManager().removeMap(map.getName());
                    }
                    else {
                        player.sendMessage("§6§lCerkour§e> Map not found!");
                    }
                }
                else if (args[0].equalsIgnoreCase("list")) {
                    CustomFiles.reloadAllCustomFiles();
                    List<String> mapNames = new ArrayList<>();
                    for (Map m : Cerkour.getInstance().getMapManager().getAllMaps()) {
                        mapNames.add(m.getName());
                    }
                    player.sendMessage("§6§lCerkour§e> Maps: §6" + String.join("§e, §6", mapNames) + " ");
                }
                else if (args[0].equalsIgnoreCase("setspawn")) {
                    Cerkour.getInstance().getConfig().set("spawn", LocationUtils.locationToString(player.getLocation()));
                    Cerkour.getInstance().saveDefaultConfig();
                    player.sendMessage("§6§lCerkour§e> You set the server spawn!");
                }
            }
            else {
                player.sendMessage("§6§lCerkour§e> Please specify subcommand!");
            }
        }
        return true;
    }
}
