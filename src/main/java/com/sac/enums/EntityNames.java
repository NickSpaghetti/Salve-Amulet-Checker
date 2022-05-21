package com.sac.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EntityNames {
    BLOAT("Pestilent Bloat"),
    MYSTIC("Skeletal Mystic");
    private String entityName;

}