package com.spell_info;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.JavaScriptCallback;
import net.runelite.api.gameval.SpriteID;

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
		filterButton.setForcedPosition(
				filterButton.getRelativeX() + 50,
				filterButton.getRelativeY()
		);
		//1141-1149
		//1150-1158

		final int[] SPRITE_IDS_INACTIVE = {
				1141,
				1142,
				1143,
				1144,
				1145,
				1146,
				1147,
				1148,
				1149
		};

		final int[] SPRITE_IDS_ACTIVE = {
				1150,
				1151,
				1152,
				1153,
				1154,
				1155,
				1156,
				1157,
				1158
		};


		final int FONT_COLOUR_INACTIVE = 0xff981f;
		final int FONT_COLOUR_ACTIVE = 0xffffff;

		Widget filterButtonParent = filterButton.getParent();
		Widget[] refComponents = filterButton.getChildren();

		final Widget[] spriteWidgets = new Widget[9];

		for (int i = 0; i < 9; i++)
		{
			Widget c = spriteWidgets[i] = filterButtonParent.createChild(-1, WidgetType.GRAPHIC)
					.setSpriteId(refComponents[i].getSpriteId())
					.setSpriteTiling(refComponents[i].getSpriteTiling())
					.setSize(refComponents[i].getWidth(), refComponents[i].getHeight());
			c.setForcedPosition(
					filterButton.getRelativeX() + refComponents[i].getRelativeX() - 100,
					filterButton.getRelativeY() + refComponents[i].getRelativeY()
			);
			c.revalidate();
		}

		final Widget text = filterButtonParent.createChild(-1, WidgetType.TEXT)
				.setText("Info")
				.setTextColor(FONT_COLOUR_INACTIVE)
				.setFontId(refComponents[9].getFontId())
				.setTextShadowed(refComponents[9].getTextShadowed())
				.setXTextAlignment(refComponents[9].getXTextAlignment())
				.setYTextAlignment(refComponents[9].getYTextAlignment())
				.setSize(refComponents[9].getWidth(), refComponents[9].getHeight());
		text.setForcedPosition(
				filterButton.getRelativeX() + refComponents[9].getRelativeX() - 100,
				filterButton.getRelativeY() + refComponents[9].getRelativeY()
		);
		text.revalidate();

		text.setHasListener(true);
		text.setOnMouseOverListener((JavaScriptCallback) ev ->
		{
			for (int i = 0; i <= 8; i++)
			{
				spriteWidgets[i].setSpriteId(SPRITE_IDS_ACTIVE[i]); //need active sprite ids
			}
			text.setTextColor(FONT_COLOUR_ACTIVE);
		});
		text.setOnMouseLeaveListener((JavaScriptCallback) ev ->
		{
			for (int i = 0; i <= 8; i++)
			{
				spriteWidgets[i].setSpriteId(SPRITE_IDS_INACTIVE[i]); //need inactive sprite ids
			}
			text.setTextColor(FONT_COLOUR_INACTIVE);
		});

		text.setAction(0, "Info");
		text.setOnOpListener((JavaScriptCallback) ev -> log.debug("works")); //onClick.run());
		filterButtonParent.revalidate();
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