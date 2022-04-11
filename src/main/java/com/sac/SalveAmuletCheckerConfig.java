package com.sac;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Salve Amulet Checker")
public interface SalveAmuletCheckerConfig extends Config {

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
			name = "Call out players ",
			description = "Call out players not wearing their Salve Amulet",
			position = 2
	)

	default boolean isToxic() {
		return false;
	}
}
