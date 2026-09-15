package com.infotoggles;

import javax.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;

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

		addButton();
	}

	private void addButton()
	{
		if (config.spellInfo())
		{
			spell.addButton();
		}
	}

	private void onClick(Widget[] spriteWidgets)
	{

	}

	private void removeButton()
	{
		spell.removeButton();
	}

	@Override
	protected void startUp() throws Exception
	{
		clientThread.invoke(() -> {
			addButton();
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientThread.invoke(() -> {
			removeButton();
		});
	}

	@Provides
	InfoTogglesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(InfoTogglesConfig.class);
	}
}