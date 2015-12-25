package io.github.domisum;

import org.bukkit.plugin.java.JavaPlugin;

public class BuildTheWord extends JavaPlugin
{
	
	@Override
	public void onEnable()
	{
		getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " has been enabled\n");
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " has been disabled\n");
	}
	
}
