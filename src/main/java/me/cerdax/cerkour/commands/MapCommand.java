package me.cerdax.cerkour.commands;

import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.files.CustomFiles;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class MapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length == 0) {
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String mapName = args.length > 1 ? args[1] : null;
        Map map = mapName != null ? Cerkour.getInstance().getMapManager().getMapByName(mapName) : null;

        switch (subCommand) {
            case "create":
                if (mapName != null) {
                    if(map != null) {
                        player.sendMessage("§6§lCerkour§e> Map already exists!");
                        return true;
                    }
                    Map create = Cerkour.getInstance().getMapManager().createMap(mapName);
                    player.sendMessage("§6§lCerkour§e> You created the map: §6" + create.getName());
                }
                break;
            case "rankup":
                if (map != null) {
                    if (args.length < 3) {
                        if (map.getIsRankUp()) {
                            map.setRankUp(0);
                            player.sendMessage("§6§lCerkour§e> The map §6" + map.getName() + " §eis no longer rankup!");
                        } else {
                            player.sendMessage("§6§lCerkour§e> Invalid usage!");
                        }
                    } else {
                        try {
                            map.setRankUp(Integer.parseInt(args[2]));
                            player.sendMessage("§6§lCerkour§e> The map §6" + map.getName() + " §eis now the rankup: §6" + map.getRankUp());
                        } catch (NumberFormatException e) {
                            player.sendMessage("§6§lCerkour§e> §cInvalid rankup value!");
                        }
                    }
                } else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
                break;
            case "setstart":
                if (map != null) {
                    map.setStartLocation(player.getLocation());
                    player.sendMessage("§6§lCerkour§e> You set the start of map: §6" + map.getName());
                } else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
                break;
            case "setend":
                if (map != null) {
                    Location loc = player.getLocation();
                    map.setEndLocation(new Location(player.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                    player.sendMessage("§6§lCerkour§e> You set the end of map: §6" + map.getName());
                } else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
                break;
            case "swapstate":
                if (map != null) {
                    map.setState(map.isOS() ? 2 : 1);
                    player.sendMessage("§6§lCerkour§e> The map: §6" + map.getName() + "§e is now " + (map.isOS() ? "Speedrun" : "OS") + "!");
                }
                break;
            case "remove":
                if (map != null) {
                    player.sendMessage("§6§lCerkour§e> You removed the map §6" + map.getName());
                    Cerkour.getInstance().getMapManager().removeMap(map.getName());
                } else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
                break;
            case "addcp":
                if (map != null && args.length > 2) {
                    try {
                        map.addCheckPoint(Integer.parseInt(args[2]));
                        player.sendMessage("§6§lCerkour§e> You added checkpoint: §6" + args[2] + "§e to map: §6" + mapName + "§e!");
                    } catch (NumberFormatException e) {
                        player.sendMessage("§6§lCerkour§e> §cInvalid checkpoint number!");
                    }
                } else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
                break;
            case "setcpld":
                if (map != null && args.length > 2) {
                    try {
                        int cpIndex = Integer.parseInt(args[2]);
                        map.getCheckPoint(cpIndex).setLd(!map.getCheckPoint(cpIndex).isLd());
                        player.sendMessage("§6§lCerkour§e> The checkpoint: §6" + cpIndex + " §eis now " + (map.getCheckPoint(cpIndex).isLd() ? "l/d" : "no longer l/d") + "!");
                    } catch (NumberFormatException e) {
                        player.sendMessage("§6§lCerkour§e> §cInvalid checkpoint number!");
                    }
                } else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
                break;
            case "setcpfrom":
                if (map != null && args.length > 2) {
                    try {
                        map.getCheckPoint(Integer.parseInt(args[2])).setFrom(player.getLocation());
                        player.sendMessage("§6§lCerkour§e> You set the FROM Location");
                    } catch (NumberFormatException e) {
                        player.sendMessage("§6§lCerkour§e> §cInvalid checkpoint number!");
                    }
                } else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
                break;
            case "setcpto":
                if (map != null && args.length > 2) {
                    try {
                        map.getCheckPoint(Integer.parseInt(args[2])).setTo(player.getLocation());
                        player.sendMessage("§6§lCerkour§e> You set the TO Location");
                    } catch (NumberFormatException e) {
                        player.sendMessage("§6§lCerkour§e> §cInvalid checkpoint number!");
                    }
                } else {
                    player.sendMessage("§6§lCerkour§e> Map not found!");
                }
                break;
            case "setcpeffect":
                if (map != null && args.length > 4) {
                    try {
                        int cpIndex = Integer.parseInt(args[2]);
                        map.getCheckPoint(cpIndex).setEffect(PotionEffectType.getByName(args[3]), Integer.parseInt(args[4]));
                        player.sendMessage("§6§lCerkour§e> You set the checkpoint to the effect: §6" + map.getCheckPoint(cpIndex).getEffectType().getName() + " " + map.getCheckPoint(cpIndex).getAmplifier());
                    } catch (NumberFormatException e) {
                        player.sendMessage("§6§lCerkour§e> §cInvalid effect or amplifier value!");
                    }
                }
                break;
            case "difficulty":
                if (map != null && args.length > 2) {
                    try {
                        map.setDifficulty(Integer.parseInt(args[2]));
                        player.sendMessage("§6§lCerkour§e> You set the difficulty to: §6" + map.getDifficulty());
                    } catch (NumberFormatException e) {
                        player.sendMessage("§6§lCerkour§e> §cInvalid difficulty!");
                    }
                }
                break;
            case "adddb":
                if (map != null) {
                    Location loc = player.getLocation();
                    loc.setY(loc.getY() - 1);
                    map.addDeathBlock(loc.getBlock().getType());
                    player.sendMessage("§6§lCerkour§e> You added the block: §6" + loc.getBlock().getType());
                }
                break;
            case "removedb":
                if (map != null) {
                    Location loc = player.getLocation();
                    loc.setY(loc.getY() - 1);
                    map.removeDeathBlocks(loc.getBlock().getType());
                    player.sendMessage("§6§lCerkour§e> You removed the block: §6" + loc.getBlock().getType());
                }
                break;
            case "list":
                CustomFiles.reloadAllCustomFiles();
                List<String> mapNames = new ArrayList<>();
                for (Map m : Cerkour.getInstance().getMapManager().getAllMaps()) {
                    mapNames.add(m.getName());
                }
                player.sendMessage("§6§lCerkour§e> Maps: §6" + String.join("§e, §6", mapNames) + " ");
                break;
            case "setspawn":
                Cerkour.getInstance().getConfig().set("spawn", LocationUtils.locationToString(player.getLocation()));
                Cerkour.getInstance().saveConfig();
                player.sendMessage("§6§lCerkour§e> You set the server spawn!");
                break;
            default:
                player.sendMessage("§6§lCerkour§e> Invalid subcommand!");
                break;
        }
        return true;
    }
}