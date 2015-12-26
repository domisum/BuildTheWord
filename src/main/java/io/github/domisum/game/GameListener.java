package io.github.domisum.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.domisum.BuildTheWord;

public class GameListener implements Listener
{
	
	// -------
	// CONSTRUCTOR
	// -------
	public GameListener()
	{
		registerListener();
	}
	
	private void registerListener()
	{
		BuildTheWord pluginInstance = BuildTheWord.getInstance();
		pluginInstance.getServer().getPluginManager().registerEvents(this, pluginInstance);
	}
	
	
	// -------
	// EVENTS
	// -------
	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		player.teleport(BuildTheWord.getGame().getSpawnLocation());
	}
	
}
