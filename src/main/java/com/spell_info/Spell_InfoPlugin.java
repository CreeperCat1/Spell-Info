package com.spell_info;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.widgets.Widget;

@Slf4j
@PluginDescriptor(
	name = "Spell Info"
)

public class Spell_InfoPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() != 218 || client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP) == null)
		{
			return;
		}
		clientThread.invoke(() -> client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP).setHidden(true));
		addButton();
	}

	void addButton()
	{
		Widget filterButton =  client.getWidget(InterfaceID.MagicSpellbook.FILTERBUTTON);

		filterButton.setForcedPosition(filterButton.getRelativeX() + 50, filterButton.getRelativeY());
	}

	/*@Subscribe
	public void onScriptPreFired(ScriptPreFired event) // works on pre or post fired
	{
		if (event.getScriptId() == 914) //914 is script called when changing tabs and 2610 is on magic tab (but only sometimes)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "spellbook going to open", null);
			Widget filterButton = client.getWidget(InterfaceID.MagicSpellbook.FILTERBUTTON);
			filterButton.setOriginalX(50).revalidate();
		}
	}*/

	@Override
	protected void startUp() throws Exception
	{
		if (client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP) == null)
		{
			return;
		}
		clientThread.invoke(() -> client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP).setHidden(true));
	}

	@Override
	protected void shutDown() throws Exception
	{
		if (client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP) == null)
		{
			return;
		}
		clientThread.invoke(() -> client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP).setHidden(false));
	}
}