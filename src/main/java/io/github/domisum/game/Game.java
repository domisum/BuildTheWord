package io.github.domisum.game;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.domisum.BuildTheWord;
import io.github.domisum.config.ConfigLocation;
import io.github.domisum.word.WordProvider;

public class Game
{
	
	// REFERENCES
	private WordProvider wordProvider;
	
	// CONFIG VALUES
	private Location spawnLocation;
	private Location builderSpawnLocation;
	
	// STATUS
	private Player builder;
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public Game()
	{
		loadConfigValues();
		
		wordProvider = new WordProvider();
	}
	
	private void loadConfigValues()
	{
		FileConfiguration config = BuildTheWord.getInstance().getConfig();
		
		spawnLocation = new ConfigLocation(config, "spawnLocation").load();
		builderSpawnLocation = new ConfigLocation(config, "builderSpawnLocation").load();
	}
	
	
	// -------
	// GETTERS
	// -------
	public WordProvider getWordProvider()
	{
		return wordProvider;
	}
	
	
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
	
	public Location getBuilderSpawnLocation()
	{
		return builderSpawnLocation;
	}
	
}
