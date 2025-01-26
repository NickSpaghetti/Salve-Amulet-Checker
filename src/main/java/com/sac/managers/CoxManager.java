package com.sac.managers;

import lombok.Getter;
import lombok.val;
import net.runelite.api.*;
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
    private HashSet<Player> playersInRaid;
    public final static String RAID_START_MESSAGE = "The raid has begun!";
    public final static String RAID_END_MESSAGE = "As the Great Olm collapses, the crystal blocking your exit has been shattered";

    public boolean isPlayerInCoxParty() {
        boolean isInParty = false;
        if (client.getGameState() == GameState.LOGGED_IN) {
            isInParty = client.getVarbitValue(VarPlayer.IN_RAID_PARTY) != -1;
        }

        return isInParty;
    }

    public boolean isPlayerInCoxRaid() {
        boolean isInRaid = false;
        if (client.getGameState() == GameState.LOGGED_IN) {
            isInRaid = client.getVarbitValue(Varbits.IN_RAID) != 0;
        }

        return isInRaid;
    }

    public boolean isRaidInProgress(){
        boolean isRaidInProgress = false;
        if (client.getGameState() == GameState.LOGGED_IN) {
            isRaidInProgress = client.getVarbitValue(Varbits.RAID_STATE) == 1;
        }
        return isRaidInProgress;
    }

    public boolean isInMysticRoom(Tile currentTile) {
        boolean isInMysticTile = false;
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxRaid()) {
            int chunkData = client.getLocalPlayer().getWorldView().getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
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
            int chunkData = client.getLocalPlayer().getWorldView().getInstanceTemplateChunks()[currentPlane][(x) / 8][y / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            if (template == InstanceTemplates.RAIDS_MYSTICS){
                isInMysticTile = true;
            }
        }
        return isInMysticTile;
    }

    public void setUpRaidParty(Tile currentTile) {
        val coxRaidParty = new HashSet<Player>();
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxParty() && currentTile != null) {
            int chunkData = client.getLocalPlayer().getWorldView().getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);

            if (template == InstanceTemplates.RAIDS_LOBBY || template == InstanceTemplates.RAIDS_START){
                playersInRaid.clear();
                client.getLocalPlayer().getWorldView().players().forEach(player -> coxRaidParty.add(player));
                playersInRaid = coxRaidParty;
            }

        }

    }

    public void setUpRaidParty(int currentPlane, int x, int y) {
        val coxRaidParty = new HashSet<Player>();
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxParty()) {
            int chunkData = client.getLocalPlayer().getWorldView().getInstanceTemplateChunks()[currentPlane][(x) / 8][y / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            if (template == InstanceTemplates.RAIDS_LOBBY || template == InstanceTemplates.RAIDS_START){
                //playersInRaid.clear();
                client.getLocalPlayer().getWorldView().players().forEach(player -> coxRaidParty.add(player));
                playersInRaid = coxRaidParty;
            }

        }

    }

    public InstanceTemplates getCurrentRoom(Tile currentTile){
        if (client.getGameState() == GameState.LOGGED_IN && isPlayerInCoxRaid() && currentTile != null) {
            int chunkData = client.getTopLevelWorldView().getInstanceTemplateChunks()[currentTile.getPlane()][(currentTile.getSceneLocation().getX()) / 8][currentTile.getSceneLocation().getY() / 8];
            InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
            return template;
        }
        return null;
    }


    public HashMap<Player, Boolean> getPlayersInMysticRoom() {
        val playersInMysticRoom = new HashMap<Player, Boolean>();
        if(playersInRaid == null){
           return playersInMysticRoom;
        }
        for (Player player : playersInRaid) {

            boolean isPlayerInMysticRoom = isInMysticRoom(player.getWorldLocation().getPlane(), player.getLocalLocation().getSceneX(), player.getLocalLocation().getSceneY());
            if (playersInMysticRoom.containsKey(player.getName())) {
                playersInMysticRoom.replace(player, isPlayerInMysticRoom);
            } else {
                playersInMysticRoom.put(player, isPlayerInMysticRoom);
            }
        }
        return playersInMysticRoom;
    }

    public Set<Player> getPlayersActiveInMysticRoom() {
        if(playersInRaid == null){
            return Collections.emptySet();
        }

        return playersInRaid.stream()
                .filter((player ->
                        isInMysticRoom(
                            player.getWorldLocation().getPlane()
                            ,player.getLocalLocation().getSceneX()
                            ,player.getLocalLocation().getSceneY()))
                )
                .collect(Collectors.toSet());
    }

    public void removePlayerFromParty(String playerName)
    {
        playersInRaid.removeIf(player -> (Objects.equals(player.getName(), playerName)));
    }

    public ArrayList<Player> GetRaidParty() {
        return new ArrayList<Player>(playersInRaid);
    }

    public void clearRaiders(){
        playersInRaid.clear();
    }

}
