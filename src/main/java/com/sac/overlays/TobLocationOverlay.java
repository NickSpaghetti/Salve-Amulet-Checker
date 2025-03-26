package com.sac.overlays;

import com.google.inject.Inject;
import com.sac.SalveAmuletCheckerConfig;
import com.sac.SalveAmuletCheckerPlugin;
import com.sac.enums.TobState;
import lombok.val;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.TitleComponent;

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
        setPriority(Overlay.PRIORITY_HIGH);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Mystic Salve Amulet Checker Overlay"));

    }

    @Override
    public Dimension render(Graphics2D graphics) {

        if (config.isLocationVisibleInTob() && plugin.tobManager.getTobState() == TobState.InTob) {
            val currentRoom = plugin.tobManager.GetRoom();
            if (currentRoom != null && !currentRoom.isEmpty()) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(currentRoom)
                        .color(Color.white)
                        .build());
            }

        }
        return super.render(graphics);
    }
}
