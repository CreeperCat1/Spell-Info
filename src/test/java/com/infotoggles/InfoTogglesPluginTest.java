package com.infotoggles;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class InfoTogglesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(InfoTogglesPlugin.class);
		RuneLite.main(args);
	}
}