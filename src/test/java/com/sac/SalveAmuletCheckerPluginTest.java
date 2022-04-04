package com.sac;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SalveAmuletCheckerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SalveAmuletCheckerPlugin.class);
		RuneLite.main(args);
	}
}