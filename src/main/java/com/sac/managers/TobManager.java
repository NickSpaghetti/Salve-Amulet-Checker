package com.sac.managers;

import com.sac.enums.TobState;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.Widget;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.HashSet;
import static net.runelite.api.widgets.WidgetID.TOB_GROUP_ID;

public class TobManager {

    private HashSet<String> tobRaiderNames;
    private TobState tobState;
    private String currentRoom;

    public static int MAX_RAIDERS = 5;
    public static final int THEATRE_RAIDERS_VARC = 330;
    public static final int TOB_BOSS_INTERFACE_ID = 1;
    public static final int TOB_BOSS_INTERFACE_TEXT_ID = 2;


    @Inject
    private Client client;

    public HashSet<String> getRaiderNames() {
        return tobRaiderNames;
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
        Widget widget = client.getWidget(TOB_GROUP_ID, TOB_BOSS_INTERFACE_ID);
        if (widget != null && widget.getChild(TOB_BOSS_INTERFACE_TEXT_ID) != null) {
            Widget childWidget = widget.getChild(TOB_BOSS_INTERFACE_TEXT_ID);
            if(!childWidget.getText().equals("")){
                currentRoom = childWidget.getText();
            }
        }
        return currentRoom;
    }


    public TobState getTobState() {
        if (client.getGameState() != GameState.LOGGED_IN) return TobState.NoParty;

        TobState newRaidState =  TobState.fromInteger(client.getVarbitValue(Varbits.THEATRE_OF_BLOOD));
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
    }


}
