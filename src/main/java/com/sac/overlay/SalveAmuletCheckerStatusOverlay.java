package com.sac.overlay;

import java.awt.*;

import com.sac.SalveAmuletCheckerPlugin;
import com.sac.constants.EntityNames;
import com.sac.state.PanelState;
import net.runelite.api.Client;
import net.runelite.api.InstanceTemplates;
import net.runelite.api.MenuAction;
import net.runelite.api.Tile;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPriority;
import javax.inject.Inject;

public class SalveAmuletCheckerStatusOverlay extends OverlayPanel {

    public static final Color TITLED_CONTENT_COLOR = new Color(190, 190, 190);
    private Client client;
    private SalveAmuletCheckerPlugin plugin;

    @Inject
    private SalveAmuletCheckerStatusOverlay(Client client, SalveAmuletCheckerPlugin plugin){
        super(plugin);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPriority(OverlayPriority.LOW);
        this.client = client;
        this.plugin = plugin;
        getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY,"","Current Room"));
    }


    @Override
    public Dimension render(Graphics2D graphics)
    {
        if(PanelState.CurrentMonsterSelected != "" || PanelState.CurrentMonsterSelected != null){
            return null;
        }
        String currentLocation = "";
        if(plugin.activeMonster == EntityNames.BLOAT && plugin.tobManager.GetRoom() != null){
            currentLocation = plugin.tobManager.GetRoom();
        }
        else if(plugin.activeMonster == EntityNames.MYSTIC && plugin.coxManager.isPlayerInCoxRaid()){
            Tile currentTile = client.getSelectedSceneTile();
            int chunkData = client.getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            currentLocation = template.name();
        }
        else {
            return null;
        }


        return super.render(graphics);
    }

}
