package com.sac.managers;

import lombok.val;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.raids.Raid;
import net.runelite.client.plugins.raids.RaidRoom;
import net.runelite.client.plugins.raids.solver.Room;
import net.runelite.client.ws.PartyMember;
import net.runelite.client.ws.PartyService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;

@PluginDescriptor(
        name = "Salve Amulet Checker",
        tags = {"combat", "overlay", "chambers", "xeric", "raids", "tob", "theatre of blood"}
)

public class CoxManager {

    @Inject
    private Client client;
    private HashSet<Player> playersInRaid;

    public boolean isPlayerInCoxParty() {
        boolean isInParty = false;
        if (client.getGameState() == GameState.LOGGED_IN) {
            isInParty = client.getVar(VarPlayer.IN_RAID_PARTY) != -1;
        }

        return isInParty;
    }

    public boolean isPlayerInCoxRaid() {
        boolean isInRaid = false;
        if (client.getGameState() == GameState.LOGGED_IN) {
            isInRaid = client.getVar(Varbits.IN_RAID) != -1;
        }

        return isInRaid;
    }

    public boolean isInMysticRoom(Tile currentTile) {
        boolean isInMysticTile = false;
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxRaid()) {
            int chunkData = client.getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            if (template == InstanceTemplates.RAIDS_MYSTICS){
                isInMysticTile = true;
            }
        }
        return isInMysticTile;
    }

    public boolean isInMysticRoom(int currentPlane, int x, int y) {
        boolean isInMysticTile = false;
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxRaid()) {
            int chunkData = client.getInstanceTemplateChunks()[currentPlane][(x) / 8][y / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            if (template == InstanceTemplates.RAIDS_MYSTICS){
                isInMysticTile = true;
            }
        }
        return isInMysticTile;
    }

    public void setUpRaidParty(Tile currentTile) {
        val coxRaidParty = new HashSet<Player>();
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxParty()) {
            int chunkData = client.getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);

            if (template == InstanceTemplates.RAIDS_LOBBY){
                coxRaidParty.clear();
                coxRaidParty.addAll(client.getPlayers());
            }
            playersInRaid = coxRaidParty;
        }

    }

    public InstanceTemplates getCurrentRoom(Tile currentTile){
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxRaid()) {
            int chunkData = client.getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            return template;
        }
        return null;
    }


    public HashMap<Player, Boolean> getPlayersInMysticRoom() {
        val playersInMysticRoom = new HashMap<Player, Boolean>();
        for (Player player : playersInRaid) {

            boolean isPlayerInMysticRoom = isInMysticRoom(player.getWorldLocation().getPlane(), player.getWorldLocation().getX()/8, player.getWorldLocation().getY()/8);
            if (playersInMysticRoom.containsKey(player.getName())) {
                playersInMysticRoom.replace(player, isPlayerInMysticRoom);
            } else {
                playersInMysticRoom.put(player, isPlayerInMysticRoom);
            }
        }
        return playersInMysticRoom;
    }

}
