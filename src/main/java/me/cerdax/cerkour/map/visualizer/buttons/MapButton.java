package me.cerdax.cerkour.map.visualizer.buttons;

import lombok.Getter;
import lombok.Setter;
import me.cerdax.cerkour.Cerkour;
import me.cerdax.cerkour.map.Map;
import me.cerdax.cerkour.map.visualizer.MapVisualizerPage;
import me.cerdax.cerkour.profile.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.lynuxcraft.deadsilenceiv.dutilities.ItemPlaceHolder;
import us.lynuxcraft.deadsilenceiv.dutilities.builders.ItemBuilder;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.InteractiveInventory;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.actions.SlotAction;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.items.ShareableButtonItem;
import us.lynuxcraft.deadsilenceiv.dutilities.inventory.items.ShareableItem;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MapButton extends ShareableGUIButton<MapVisualizerPage> {
    protected final Map map;
    @Getter @Setter protected int slot;
    public MapButton(Map map) {
        super("map#"+map.getMapUUID().toString());
        this.map = map;
        build();
    }

    public void build() {
        ItemStack main = new ItemBuilder(Material.PAPER).setName(getItemName()).setLore(new ArrayList<>(Arrays.asList("&6Difficulty&e: &7%difficulty%"))).build();
        ShareableButtonItem item = new ShareableButtonItem(this,"main",main);
        addItem(item);
        loadPlaceholders();
        setCurrentItem(item);
    }

    @Override
    public void loadActions(MapVisualizerPage inventory) {
        inventory.addAction(new SlotAction(ClickType.LEFT,getSlot(inventory)) {
            @Override
            public void execute(Player player) {
                player.closeInventory();
                Profile profile = Cerkour.getInstance().getProfileManager().getProfile(player.getUniqueId());
                profile.joinMap(map);
            }
        });
    }

    @Override
    protected void loadPlaceholders() {
        for (ShareableItem item : getItems()) {
            item.addPlaceholder(new ItemPlaceHolder("%difficulty%",true,true) {
                @Override
                public String getReplacement() {
                    return ""+map.getDifficulty();
                }
            });
        }
    }

    public abstract String getItemName();

    @Override
    public int getSlot(InteractiveInventory interactiveInventory) {
        return slot;
    }
}
