package com.sac.overlays;

import com.google.inject.Inject;
import com.sac.SalveAmuletCheckerConfig;
import com.sac.SalveAmuletCheckerPlugin;
import com.sac.constants.EntityNames;
import lombok.val;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.*;
import net.runelite.client.util.Text;

import java.awt.*;
import java.util.HashMap;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;


public class MysticRoomOverlay extends OverlayPanel {

    private final Client client;
    private final SalveAmuletCheckerPlugin plugin;
    private final SalveAmuletCheckerConfig config;
    private final ItemManager itemManager;
    private final SpriteManager spriteManager;
    private final PanelComponent panelImages = new PanelComponent();

    @Inject
    private MysticRoomOverlay(Client client, SalveAmuletCheckerPlugin plugin, SalveAmuletCheckerConfig config, ItemManager itemManager, SpriteManager spriteManager) {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.itemManager = itemManager;
        this.spriteManager = spriteManager;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Mystic Salve Amulet Checker Overlay"));

    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if(plugin.coxManager.isPlayerInCoxRaid() && plugin.coxManager.playersInRaid != null && client.getSelectedSceneTile() != null && plugin.coxManager.isInMysticRoom(client.getSelectedSceneTile()) ){
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Salve Amulet Checker")
                    .color(Color.white)
                    .build());
            DisplayNames(plugin.coxManager.getPlayersInMysticRoom());
        }


        return super.render(graphics);
    }


    private void DisplayNames(HashMap<Player, Boolean> playersInMysticRoom){
        if(playersInMysticRoom == null){
            return;
        }

        playersInMysticRoom.forEach((player,isInMysticRoom) -> {
            Color salveAmuletEquipColor = isInMysticRoom ? Color.GREEN : Color.red;
            boolean isSalveAmuletEquip = isInMysticRoom;
            if(isInMysticRoom){
                isSalveAmuletEquip =  plugin.isSalveAmuletEquipped(player);
                salveAmuletEquipColor = isSalveAmuletEquip ? Color.green : Color.red;
            }
            panelComponent.getChildren().add(LineComponent.builder()
                    .left(player.getName())
                    .right(isSalveAmuletEquip ? "Yes" : "No")
                    .leftColor(Color.white)
                    .rightColor(salveAmuletEquipColor)
                    .build());
        });

        }

}
