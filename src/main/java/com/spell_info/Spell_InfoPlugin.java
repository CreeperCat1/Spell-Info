package com.spell_info;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.api.widgets.WidgetPositionMode;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.widgets.Widget;
import java.util.Arrays;

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

		final int FONT_COLOUR_INACTIVE = 0xff981f;
		final int FONT_COLOUR_ACTIVE = 0xffffff;

		int padding = 8;
		final int w = filterButton.getOriginalWidth();
		final int h = filterButton.getOriginalHeight();
		final int x = filterButton.getRelativeX() + (w / 2) + (padding / 2);
		final int y = filterButton.getRelativeY();

		Widget filterButtonParent = filterButton.getParent();
		Widget[] refComponents = filterButton.getChildren();

		final Widget[] spriteWidgets = new Widget[9];

		int bgWidth = w - refComponents[0].getOriginalWidth();
		int bgHeight = h - refComponents[0].getOriginalHeight();
		int bgX = (x + refComponents[0].getOriginalX()) + (w - bgWidth) / 2;
		int bgY = (y + refComponents[0].getOriginalY()) + (h - bgHeight) / 2;
		spriteWidgets[0] = filterButtonParent.createChild(-1, WidgetType.GRAPHIC)
				.setSpriteId(refComponents[0].getSpriteId())
				.setPos(bgX, bgY)
				.setSize(bgWidth, bgHeight)
				.setYPositionMode(filterButton.getYPositionMode());
		spriteWidgets[0].revalidate();

		for (int i = 0; i < 8; i++)
		{
			Widget c = spriteWidgets[i] = filterButtonParent.createChild(-1, WidgetType.GRAPHIC)
					.setSpriteId(refComponents[i].getSpriteId())
					.setSize(refComponents[i].getOriginalWidth(), refComponents[i].getOriginalHeight());
			c.setForcedPosition(refComponents[i].getRelativeX() - 25, refComponents[i].getRelativeY());

			spriteWidgets[i].revalidate();
		}

		final Widget text = filterButtonParent.createChild(-1, WidgetType.TEXT)
				.setText("Info")
				.setTextColor(FONT_COLOUR_INACTIVE)
				.setFontId(refComponents[9].getFontId())
				.setTextShadowed(refComponents[9].getTextShadowed())
				.setXTextAlignment(refComponents[9].getXTextAlignment())
				.setYTextAlignment(refComponents[9].getYTextAlignment())
				.setPos(x, y)
				.setSize(w, h)
				.setYPositionMode(filterButton.getYPositionMode());
		text.revalidate();
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