package com.sac.managers;

import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@PluginDescriptor(
        name = "Salve Amulet Checker",
        tags = {"combat", "overlay", "chambers", "xeric", "raids", "tob", "theatre of blood"}
)

public class CoxManager {

    @Inject
    private Client client;

    @Getter
    private final HashSet<String> raiderNames = new HashSet<>();
    public final static String RAID_START_MESSAGE = "The raid has begun!";
    public final static String RAID_END_MESSAGE = "As the Great Olm collapses, the crystal blocking your exit has been shattered";

    public boolean isPlayerInCoxParty() {
        boolean isInParty = false;
        if (client.getGameState() == GameState.LOGGED_IN) {
            isInParty = client.getVarbitValue(VarPlayerID.RAIDS_PARTY_GROUPHOLDER) != -1;
        }

        return isInParty;
    }

    public boolean isPlayerInCoxRaid() {
        boolean isInRaid = false;
        if (client.getGameState() == GameState.LOGGED_IN) {
            isInRaid = client.getVarbitValue(VarbitID.RAIDS_CLIENT_INDUNGEON) != 0;
        }

        return isInRaid;
    }

    public boolean isRaidInProgress() {
        boolean isRaidInProgress = false;
        if (client.getGameState() == GameState.LOGGED_IN) {
            isRaidInProgress = client.getVarbitValue(VarbitID.RAIDS_CLIENT_PROGRESS) == 1;
        }
        return isRaidInProgress;
    }

    public boolean isInMysticRoom(Tile currentTile) {
        boolean isInMysticTile = false;
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxRaid()) {
            int chunkData = client.getLocalPlayer().getWorldView().getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            if (template == InstanceTemplates.RAIDS_MYSTICS) {
                isInMysticTile = true;
            }
        }
        return isInMysticTile;
    }

    public boolean isInMysticRoom(int currentPlane, int x, int y) {
        boolean isInMysticTile = false;
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxRaid()) {
            int chunkData = client.getLocalPlayer().getWorldView().getInstanceTemplateChunks()[currentPlane][(x) / 8][y / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            if (template == InstanceTemplates.RAIDS_MYSTICS) {
                isInMysticTile = true;
            }
        }
        return isInMysticTile;
    }

    public void LoadRaiders() {
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxParty()) {
            client.getLocalPlayer().getWorldView().players().forEach(player -> raiderNames.add(player.getName()));
        }
    }

    public InstanceTemplates getCurrentRoom(Tile currentTile) {
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxRaid() && currentTile != null) {
            int chunkData = client.getTopLevelWorldView().getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            return template;
        }
        return null;
    }


    public Set<Player> getPlayersActiveInMysticRoom() {
        return client.getLocalPlayer().getWorldView().players().stream()
                .filter(player -> raiderNames.contains(player.getName()))
                .filter((player ->
                        isInMysticRoom(
                                player.getWorldLocation().getPlane()
                                , player.getLocalLocation().getSceneX()
                                , player.getLocalLocation().getSceneY()))
                )
                .collect(Collectors.toSet());
    }

    public void clearRaiders() {
        raiderNames.clear();
    }

}
