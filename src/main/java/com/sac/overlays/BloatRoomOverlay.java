package com.sac.overlays;

import com.google.inject.Inject;
import com.sac.SalveAmuletCheckerConfig;
import com.sac.SalveAmuletCheckerPlugin;
import com.sac.enums.EntityNames;
import com.sac.enums.TobState;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;
import java.util.HashSet;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class BloatRoomOverlay extends OverlayPanel {

    private final Client client;
    private final SalveAmuletCheckerPlugin plugin;
    private final SalveAmuletCheckerConfig config;

    @Inject
    private BloatRoomOverlay(Client client, SalveAmuletCheckerPlugin plugin, SalveAmuletCheckerConfig config) {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Bloat Salve Amulet Checker Overlay"));

    }

    @Override
    public Dimension render(Graphics2D graphics) {

        if(plugin.tobManager.getTobState() == TobState.InTob && plugin.tobManager.GetRoom().equals(EntityNames.BLOAT.getEntityName())){
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Salve Amulet Checker")
                    .color(Color.white)
                    .build());
            DisplayNames(plugin.tobManager.getRaiderNames());
        }


        return super.render(graphics);
    }


    private void DisplayNames(HashSet<String> playersNames){
        if(playersNames == null){
            return;
        }

        playersNames.forEach((playerName) -> {
            Player foundPlayer =  client.getPlayers().stream().filter((player) -> player.getName().equals(playerName)).findFirst().orElseGet(() -> null);
            if(foundPlayer != null){
                boolean isSalveAmuletEquip = plugin.isSalveAmuletEquipped(foundPlayer);
                Color salveAmuletEquipColor = isSalveAmuletEquip ? Color.green : Color.red;
                panelComponent.getChildren().add(LineComponent.builder()
                        .left(foundPlayer.getName())
                        .right(isSalveAmuletEquip ? "Yes" : "No")
                        .leftColor(Color.white)
                        .rightColor(salveAmuletEquipColor)
                        .build());
            }
        });

    }

}
