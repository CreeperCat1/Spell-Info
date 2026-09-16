package com.infotoggles;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("InfoTogglesConfig")
public interface InfoTogglesConfig extends Config
{
	@ConfigItem(
		keyName = "spellInfo",
		name = "Show Spell Info",
		description = "Enable to show the spell Info button",
		position = 1
	)
	default boolean spellInfo()
	{
		return true;
	}

	@ConfigItem(
			keyName = "prayerInfo",
			name = "Show Prayer Info",
			description = "Enable to show the prayer Info button.<br>"
				+ "(WARNING: Must enable \"Show Prayer Tooltips\" in settings to work)",
			position = 2
	)
	default boolean prayerInfo()
	{
		return false;
	}
}