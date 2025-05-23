package com.sac.overlays;

import com.google.inject.Inject;
import com.sac.SalveAmuletCheckerConfig;
import com.sac.SalveAmuletCheckerPlugin;
import lombok.val;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;
import java.util.Set;

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
        setPriority(MysticRoomOverlay.PRIORITY_LOW);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.itemManager = itemManager;
        this.spriteManager = spriteManager;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Mystic Salve Amulet Checker Overlay"));

    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Player player = client.getLocalPlayer();
        if (player != null && plugin.coxManager.isPlayerInCoxRaid() && plugin.coxManager.isInMysticRoom(player.getWorldLocation().getPlane(), player.getLocalLocation().getSceneX(), player.getLocalLocation().getSceneY())) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Salve Amulet Checker")
                    .color(Color.white)
                    .build());
            DisplayNames(plugin.coxManager.getPlayersActiveInMysticRoom());
        }

        return super.render(graphics);
    }


    private void DisplayNames(Set<Player> playersInMysticRoom) {
        if (playersInMysticRoom == null) {
            return;
        }

        playersInMysticRoom.forEach((player) -> {
            val isSalveAmuletEquip = plugin.isSalveAmuletEquipped(player);
            val salveAmuletEquipColor = isSalveAmuletEquip ? Color.green : Color.red;
            panelComponent.getChildren().add(LineComponent.builder()
                    .left(player.getName())
                    .right(isSalveAmuletEquip ? "Yes" : "No")
                    .leftColor(Color.white)
                    .rightColor(salveAmuletEquipColor)
                    .build());
        });
    }

}
