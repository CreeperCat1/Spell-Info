package com.infotoggles;

import javax.inject.Inject;
import javax.swing.*; //remove
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction; //remove
import net.runelite.api.MenuEntry; //remove
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.gameval.VarClientID; //remove

@Slf4j
@PluginDescriptor(
		name = "Info Toggles"
)

public class InfoTogglesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private InfoTogglesConfig config;

	@Inject
	private InfoTogglesSpell spell; //maybe switch this to a button manager class and have 2 instances for spell/prayer

	@Inject
	private InfoTogglesPrayer prayer;

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() != 218)
		{
			return;
		}

		addAllButtons();
	}

	@Subscribe //remove entire onGameTick
	public void onGameTick(GameTick gameTick)
	{
		MenuEntry[] menuEntries = client.getMenuEntries();
		int last = menuEntries.length - 1;

		if (last < 0)
		{
			return;
		}

		MenuEntry menuEntry = menuEntries[last];
		String target = menuEntry.getTarget();
		String option = menuEntry.getOption();
		MenuAction type = menuEntry.getType();
		log.debug("Target: " + target.toString() + " Option: " + option.toString() + " Type: " + type.toString());

		int tooltipTimeout = client.getVarcIntValue(VarClientID.TOOLTIP_TIME);
		/*if (tooltipTimeout > client.getGameCycle())
		{
			log.debug("timeout is bigger");
			return;
		}*/

		// If this varc is set, a tooltip is already being displayed
		int tooltipDisplayed = client.getVarcIntValue(VarClientID.TOOLTIP_BUILT);
		if (tooltipDisplayed == 1)
		{
			log.debug("alrday displayer");
			return;
		}
		log.debug("completed and showing tooltip");
		//adds tooltip
	}

	private void addAllButtons()
	{
		if (config.spellInfo())
		{
			spell.addButton();
		}
		if (config.prayerInfo())
		{
			prayer.addButton();
		}
	}

	private void onClick(Widget[] spriteWidgets)
	{

	}

	private void removeAllButtons()
	{
		spell.removeButton();
		prayer.removeButton();
	}

	@Override
	protected void startUp() throws Exception
	{
		clientThread.invoke(() -> {
			addAllButtons();
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientThread.invoke(() -> {
			removeAllButtons();
		});
	}

	@Provides
	InfoTogglesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(InfoTogglesConfig.class);
	}
}