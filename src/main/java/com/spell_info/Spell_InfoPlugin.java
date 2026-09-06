package com.spell_info;

import javax.annotation.Nonnull;
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

	private static final int buttonDistance = 40;

	private boolean infoActive;

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
		Widget tooltip = client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP);

		if (widgetLoaded.getGroupId() != 218 || tooltip == null)
		{
			return;
		}

		tooltip.setHidden(true);
		addButton();
	}

	private void addButton()
	{
		Widget filterButton =  client.getWidget(InterfaceID.MagicSpellbook.FILTERBUTTON);

		filterButton.setForcedPosition(
				filterButton.getRelativeX() + buttonDistance,
				filterButton.getRelativeY()
		);

		Widget filterButtonParent = filterButton.getParent();
		Widget[] refComponents = filterButton.getChildren(); //maybe final for both and move to top of addbutton? something about c ssame for text

		final Widget[] spriteWidgets = new Widget[9];

		for (int i = 0; i < 9; i++)
		{
			Widget c = spriteWidgets[i] = filterButtonParent.createChild(-1, WidgetType.GRAPHIC)
					.setSpriteId(SPRITE_IDS_INACTIVE[i])
					.setSpriteTiling(refComponents[i].getSpriteTiling())
					.setSize(refComponents[i].getWidth(), refComponents[i].getHeight());
			c.setForcedPosition(
					filterButton.getRelativeX() + refComponents[i].getRelativeX() - (buttonDistance * 2),
					refComponents[i].getRelativeY()
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
				filterButton.getRelativeX() + refComponents[9].getRelativeX() - (buttonDistance * 2),
				refComponents[9].getRelativeY()
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
		int[] spriteIds = infoActive ? SPRITE_IDS_INACTIVE : SPRITE_IDS_ACTIVE;

		for (int i = 0; i <= 8; i++)
		{
			spriteWidgets[i].setSpriteId(spriteIds[i]);
		}

		client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP).setHidden(infoActive);
		infoActive = !infoActive;//move infoactive to top maybe
	}

	private void removeButton()
	{
		Widget filterButton = client.getWidget(InterfaceID.MagicSpellbook.FILTERBUTTON);

		filterButton.setForcedPosition(
				filterButton.getRelativeX() - buttonDistance,
				filterButton.getRelativeY()
		);

		Widget filterParent = filterButton.getParent();
		filterParent.deleteAllChildren();
	}

	@Override
	protected void startUp() throws Exception
	{
		Widget tooltip = client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP);

		if (tooltip == null)
		{
			return;
		}

		clientThread.invoke(() -> {
			tooltip.setHidden(true);
			addButton();
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		infoActive = false;
		clientThread.invoke(() -> {
			client.getWidget(InterfaceID.MagicSpellbook.TOOLTIP).setHidden(false);
			removeButton();
		});
	}
}