package com.sac.overlays;

import com.google.inject.Inject;
import com.sac.SalveAmuletCheckerConfig;
import com.sac.SalveAmuletCheckerPlugin;
import com.sac.enums.TobState;
import lombok.val;
import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.Text;

import java.awt.*;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class TobLocationOverlay extends OverlayPanel {

    private final Client client;
    private final SalveAmuletCheckerPlugin plugin;
    private final SalveAmuletCheckerConfig config;

    @Inject
    private TobLocationOverlay(Client client, SalveAmuletCheckerPlugin plugin, SalveAmuletCheckerConfig config) {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Mystic Salve Amulet Checker Overlay"));

    }

    @Override
    public Dimension render(Graphics2D graphics) {

        if (plugin.tobManager.getTobState() == TobState.InTob) {
            val currentRoom = plugin.tobManager.GetRoom();
            if (currentRoom != null && currentRoom != "") {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(currentRoom)
                        .color(Color.white)
                        .build());
            }

        }
        return super.render(graphics);
    }
}
