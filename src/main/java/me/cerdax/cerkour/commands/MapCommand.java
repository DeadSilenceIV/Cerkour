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
                if (args.length > 1) {
                    Map map = Cerkour.getInstance().getMapManager().getMapByName(args[1]);
                    if (args[0].equalsIgnoreCase("create")) {
                         Map create = Cerkour.getInstance().getMapManager().createMap(args[1]);
                        player.sendMessage("§6§lCerkour§e> You created the map: §6" + create.getName());
                    }
                    else if (args[0].equalsIgnoreCase("rankup")) {
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
                        if (map != null) {
                            map.setStartLocation(player.getLocation());
                            player.sendMessage("§6§lCerkour§e> You set the start of map: §6" + map.getName());
                        }
                        else {
                            player.sendMessage("§6§lCerkour§e> Map not found!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setend")) {
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
                        if (map != null) {
                            player.sendMessage("§6§lCerkour§e> You removed the map §6" + map.getName());
                            Cerkour.getInstance().getMapManager().removeMap(map.getName());
                        }
                        else {
                            player.sendMessage("§6§lCerkour§e> Map not found!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("addcp")) {
                        if (map != null) {
                            map.addCheckPoint(Integer.parseInt(args[2]));
                            player.sendMessage("§6§lCerkour§e> You added checkpoint: §6" + args[2] + "§e to map: §6" + args[1] + "§e!");
                        }
                        else {
                            player.sendMessage("§6§lCerkour§e> Map not found!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setcpld")) {
                        if (map != null) {
                            if (map.getCheckPoint(Integer.parseInt(args[2])).isLd()) {
                                map.getCheckPoint(Integer.parseInt(args[2])).setLd(false);
                                map.serialize();
                                player.sendMessage("§6§lCerkour§e> The checkpoint: §6" + args[2] + " §eis no longer l/d!");
                            }
                            else {
                                map.getCheckPoint(Integer.parseInt(args[2])).setLd(true);
                                map.serialize();
                                player.sendMessage("§6§lCerkour§e> The checkpoint: §6" + args[2] + " §eis now l/d!");
                            }
                        }
                        else {
                            player.sendMessage("§6§lCerkour§e> Map not found!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setcpfrom")) {
                        if (map != null) {
                            map.getCheckPoint(Integer.parseInt(args[2])).setFrom(player.getLocation());
                            map.serialize();
                            player.sendMessage("§6§lCerkour§e> You set the FROM Location");
                        }
                        else {
                            player.sendMessage("§6§lCerkour§e> Map not found!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setcpto")) {
                        if (map != null) {
                            map.getCheckPoint(Integer.parseInt(args[2])).setTo(player.getLocation());
                            player.sendMessage("§6§lCerkour§e> You set the TO Location");
                            map.serialize();
                        }
                        else {
                            player.sendMessage("§6§lCerkour§e> Map not found!");
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("list")) {
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
                player.sendMessage("§6§lCerkour§e> Map Subcommands: \n§e- §6§lcreate §e(map name) \n§e- §6§lsetstart §e(map name) \n§e- §6§lsetend §e(map name) \n§e- §6§lrankup §e(map name) (rank) \n§e- §6§lremove §e(map name) \n§e- §6§laddcp §e(map name) (checkpoint number) \n§e- §6§lsetcpfrom §e(map name) (checkpoint number) \n§e- §6§lsetcpto §e(map name) (checkpoint number) \n§e- §6§lsetcpld §e(map name) (checkpoint number) \n§e- §6§llist \n§e- §6§lsetspawn §e// sets lobby spawn, dont use this for map creation");
            }
        }
        return true;
    }
}
