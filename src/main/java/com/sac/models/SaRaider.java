package com.sac.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

public class SaRaider {
    @Getter
    @NonNull
    private final Player player;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private WorldPoint previousWorldLocation;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private WorldPoint previousWorldLocationForOverlay;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private boolean isWearingSalveAmulet;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private boolean isSalveAmuletRequired;

    SaRaider(@NonNull Player player) {
        this.player = player;
    }

    public String getName() {
        return player.getName();
    }

    public WorldPoint getCurrentWorldLocation() {
        return player.getWorldLocation();
    }
}
