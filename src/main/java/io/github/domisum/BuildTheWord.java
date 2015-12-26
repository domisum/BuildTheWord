package io.github.domisum;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.domisum.db.MySQLUtil;
import io.github.domisum.game.Game;

public class BuildTheWord extends JavaPlugin
{
	
	// REFERENCES
	private static BuildTheWord instance;
	
	private static Game game; 
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public BuildTheWord()
	{
		instance = this;
	}
	
	
	@Override
	public void onEnable()
	{
		initConfig();
		
		// initiate managers and helpers
		MySQLUtil.establishConnection();
		
		game = new Game();
		
		// register listeners and command executors
		
		
		getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " has been enabled\n");
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " has been disabled\n");
	}
	
	
	private void initConfig()
	{
		// TODO remove this
		File configFile = new File(getDataFolder(), "config.yml");
		
		if(configFile.exists())
			configFile.delete();
			
		saveDefaultConfig();
	}
	
	
	// -------
	// GETTERS
	// -------
	public static BuildTheWord getInstance()
	{
		return instance;
	}
	
	public static Game getGame()
	{
		return game;
	}
	
}
