package com.sac;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("Salve Amulet Checker")
public interface SalveAmuletCheckerConfig extends Config {

    @ConfigItem(
            keyName = "isEnabledInCox",
            name = "Enable Salve Amulet Check in Cox",
            description = "Shows the overlay for Salve Amulet Check in Chambers of Xeric",
            position = 0
    )
    default boolean isEnabledInCox() {
        return true;
    }

    @ConfigItem(
            keyName = "isEnabledInTob",
            name = "Enable Salve Amulet Check in Tob",
            description = "Shows the overlay for Salve Amulet Check in Theatre of Blood",
            position = 0
    )
    default boolean isEnabledInTob() {
        return true;
    }


    @ConfigItem(
            keyName = "isLocationVisibleInCox",
            name = "Show Location in Chambers of Xeric",
            description = "Shows your current location while in Chambers of Xeric",
			position = 0
    )
    default boolean isLocationVisibleInCox() {
        return false;
    }

    @ConfigItem(
            keyName = "isLocationVisibleInTob",
            name = "Show Location in Theatre of Blood",
            description = "Shows your current location while in Theatre of Blood",
            position = 1
    )
    default boolean isLocationVisibleInTob() {
        return false;
    }


	@ConfigItem(
			keyName = "isToxic",
			name = "Call out players",
			description = "Call out players not wearing their Salve Amulet",
			position = 2
	)

	default boolean isToxic() {
		return false;
	}
}
