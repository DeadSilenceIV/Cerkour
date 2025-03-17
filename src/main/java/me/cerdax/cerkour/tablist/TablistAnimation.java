package me.cerdax.cerkour.tablist;

import me.cerdax.cerkour.Cerkour;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TablistAnimation implements Runnable{
    private static final TablistAnimation instance = new TablistAnimation();
    private final Map<UUID, Integer> headerPositions = new HashMap<>();
    private final Map<UUID, Integer> footerPositions = new HashMap<>();
    private List<String> headerLines = new ArrayList<>();
    private List<String> footerLines = new ArrayList<>();



    @Override
    public void run() {
        headerLines.add("<yellow><b>C<yellow><gold>erkour<gold>");
        headerLines.add("<yellow><b>Ce<yellow><gold>rkour<gold>");
        headerLines.add("<yellow><b>Cer<yellow><gold>kour<gold>");
        headerLines.add("<yellow><b>Cerk<yellow><gold>our<gold>");
        headerLines.add("<yellow><b>Cerko<yellow><gold>ur<gold>");
        headerLines.add("<yellow><b>Cerkou<yellow><gold>r<gold>");
        headerLines.add("<yellow><b>Cerkour<yellow>");
        footerLines.add("<gold>play.cerkour.net<gold>");

        MiniMessage miniMessage = MiniMessage.miniMessage();

        for (Player player : Bukkit.getOnlinePlayers()) {
            int headerPosition = headerPositions.getOrDefault(player.getUniqueId(), 0);
            int footerPosition = footerPositions.getOrDefault(player.getUniqueId(), 0);

            if (headerPosition >= headerLines.size()) {
                headerPosition = 0;
            }
            if (footerPosition >= footerLines.size()) {
                footerPosition= 0;
            }
            Audience audience = Cerkour.getInstance().getAdventure().player(player);
            audience.sendPlayerListHeaderAndFooter(miniMessage.deserialize(headerLines.get(headerPosition)), miniMessage.deserialize(footerLines.get(footerPosition)));

            headerPositions.put(player.getUniqueId(), headerPosition + 1);
            footerPositions.put(player.getUniqueId(), footerPosition + 1);
        }

    }
    public static TablistAnimation getInstance() {
        return instance;
    }
}
