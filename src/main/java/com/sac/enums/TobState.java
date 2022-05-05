package com.sac.enums;

import java.util.Arrays;

public enum TobState {
    NoParty(0),
    InParty(1),
    InTob(2);
    private int tobStateValue;
    private static TobState[] states = null;

    TobState(int stateValue){
        tobStateValue = stateValue;
    }

    public int getTobStateValue(){
        return tobStateValue;
    }

    public static TobState fromInteger(int i){

        if(TobState.states == null) {
            TobState.states = TobState.values();
        }

        TobState stateValue = Arrays.stream(TobState.values()).filter(s -> s.getTobStateValue() == i).findFirst().orElse(null);
        if(stateValue == null)
        {
            return NoParty;
        }

        return stateValue;
    }



}
