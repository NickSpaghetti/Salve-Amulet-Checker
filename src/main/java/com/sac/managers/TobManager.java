package com.sac.managers;

import com.sac.enums.TobState;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class TobManager {

    private HashSet<String> tobRaiderNames;
    private TobState tobState;
    private String currentRoom;

    public static int MAX_RAIDERS = 5;
    public static final int THEATRE_RAIDERS_VARC = 330;
    public static final int TOB_BOSS_INTERFACE_TEXT_ID = 2;
    private static final int MAX_ROOM_NAME_WORDS = 5;

    private static final Set<Integer> BLOAT_NPC_IDS = Set.of(
            NpcID.TOB_BLOAT,
            NpcID.TOB_BLOAT_STORY,
            NpcID.TOB_BLOAT_HARD,
            NpcID.TOBQUEST_BLOAT,
            NpcID.DEADMAN_BREACH_BLOAT
    );


    @Inject
    private Client client;

    public HashSet<String> getRaiderNames() {
        return tobRaiderNames;
    }




    public boolean isBloatActive() {
        return client.getLocalPlayer().getWorldView().npcs().stream()
                .anyMatch(npc -> BLOAT_NPC_IDS.contains(npc.getId()));
    }

    public void LoadRaiders(){
        for (int i = 0; i < MAX_RAIDERS; i++) {
            String playerName = client.getVarcStrValue(THEATRE_RAIDERS_VARC + i);
            if (playerName != null && !playerName.isEmpty()) {
                 tobRaiderNames.add(Text.sanitize(playerName));
            }
        }
    }

    public String GetRoom(){
        Widget widget = client.getWidget(InterfaceID.TobHud.ATMOSPHERIC);
        if (widget != null && widget.getChild(TOB_BOSS_INTERFACE_TEXT_ID) != null) {
            Widget childWidget = widget.getChild(TOB_BOSS_INTERFACE_TEXT_ID);
            if(childWidget != null && !childWidget.getText().isEmpty()){
                String text = childWidget.getText();
                if (looksLikeRoomName(text)) {
                    currentRoom = text;
                }
            }
        }
        return currentRoom;
    }

    private boolean looksLikeRoomName(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '.' || c == ',' || c == '!' || c == '?') {
                return false;
            }
        }
        return trimmed.split("\\s+").length <= MAX_ROOM_NAME_WORDS;
    }


    public TobState getTobState() {
        if (client.getGameState() != GameState.LOGGED_IN) return TobState.NoParty;

        TobState newRaidState =  TobState.fromInteger(client.getVarbitValue(VarbitID.TOB_CLIENT_PARTYSTATUS));
            if (newRaidState == TobState.NoParty || newRaidState == TobState.InParty) {
                // We're not in a raid
                resetTobState();
            } else
            {
                tobState = newRaidState;
            }

        return tobState;
    }

    private void resetTobState(){
        tobState = TobState.NoParty;
        tobRaiderNames = new HashSet<>();
        currentRoom = null;
    }


}
