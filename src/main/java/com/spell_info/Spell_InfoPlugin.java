package com.spell_info;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetType;
import net.runelite.api.widgets.JavaScriptCallback;

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

	private boolean infoActive;

	private int filterButtonOX;

	private int filterButtonOY;

	private boolean positionCached;

	private static final int buttonDistance = 40;

	private static final int FONT_COLOUR_INACTIVE = 0xff981f;

	private static final int FONT_COLOUR_ACTIVE = 0xffffff;

	private static final int[] SPRITE_IDS_INACTIVE = {
			1141, 1142, 1143,
			1144, 1145, 1146,
			1147, 1148, 1149
	};

	private static final int[] SPRITE_IDS_ACTIVE = {
			1150, 1151, 1152,
			1153, 1154, 1155,
			1156, 1157, 1158
	};

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
		infoActive = false;
		client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP).setHidden(true);

		Widget filterButton = client.getWidget(InterfaceID.MagicSpellbook.FILTERBUTTON);

		if (!positionCached)
		{
			filterButtonOX = filterButton.getRelativeX();
			filterButtonOY = filterButton.getRelativeY();
			positionCached = true;
		}

		filterButton.setForcedPosition(
				filterButtonOX + buttonDistance,
				filterButtonOY
		);

		Widget filterButtonParent = filterButton.getParent();
		Widget[] refComponents = filterButton.getChildren();

		final Widget[] spriteWidgets = new Widget[9];

		for (int i = 0; i < 9; i++)
		{
			Widget c = spriteWidgets[i] = filterButtonParent.createChild(-1, WidgetType.GRAPHIC)
					.setSpriteId(SPRITE_IDS_INACTIVE[i])
					.setSpriteTiling(refComponents[i].getSpriteTiling())
					.setSize(refComponents[i].getWidth(), refComponents[i].getHeight());
			c.setForcedPosition(
					filterButtonOX + refComponents[i].getRelativeX() - buttonDistance,
					filterButtonOY + refComponents[i].getRelativeY()
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
				filterButtonOX + refComponents[9].getRelativeX() - buttonDistance,
				filterButtonOY + refComponents[9].getRelativeY()
		);
		text.revalidate();

		text.setHasListener(true);
		text.setOnMouseOverListener((JavaScriptCallback) ev ->
		{
			text.setTextColor(FONT_COLOUR_ACTIVE);
		});
		text.setOnMouseLeaveListener((JavaScriptCallback) ev ->
		{
			text.setTextColor(FONT_COLOUR_INACTIVE);
		});
		text.setOnClickListener((JavaScriptCallback) ev ->
		{
			onClick(spriteWidgets);
		});
	}

	private void onClick(Widget[] spriteWidgets)
	{
		infoActive = !infoActive;
		int[] spriteIds = infoActive ? SPRITE_IDS_ACTIVE : SPRITE_IDS_INACTIVE;

		for (int i = 0; i <= 8; i++)
		{
			spriteWidgets[i].setSpriteId(spriteIds[i]);
		}

		client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP).setHidden(!infoActive);
	}

	private void removeButton()
	{
		client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP).setHidden(false);

		Widget filterButton = client.getWidget(InterfaceID.MagicSpellbook.FILTERBUTTON);
		Widget filterButtonParent = filterButton.getParent();

		filterButton.setForcedPosition(
				filterButtonOX,
				filterButtonOY
		);

		filterButtonParent.deleteAllChildren();
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
}