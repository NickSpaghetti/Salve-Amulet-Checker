package com.sac.enums;

import java.util.Arrays;

public enum TobState {
    NoParty(0),
    InParty(1),
    InTob(2);
    private final int tobStateValue;
    private static TobState[] states = null;

    TobState(int stateValue){
        tobStateValue = stateValue;
    }

    public static TobState fromInteger(int i){

        if(TobState.states == null) {
            TobState.states = TobState.values();
        }

        if(!Arrays.asList(states).contains(i)){
            return NoParty;
        }

        return states[i];
    }



}
