package com.spell_info;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Spell_InfoPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(Spell_InfoPlugin.class);
		RuneLite.main(args);
	}
}