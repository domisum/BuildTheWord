package io.github.domisum.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.domisum.BuildTheWord;

public class ScoreManager implements Listener
{
	
	// STATUS
	private Map<Player, Integer> scores = new HashMap<Player, Integer>();
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public ScoreManager()
	{
		registerListener();
	}
	
	private void registerListener()
	{
		BuildTheWord pluginInstance = BuildTheWord.getInstance();
		pluginInstance.getServer().getPluginManager().registerEvents(this, pluginInstance);
	}
	
	
	// -------
	// CHANGERS
	// -------
	public void reset()
	{
		for(Player key : scores.keySet())
			scores.put(key, 0);
	}
	
	
	// -------
	// EVENTS
	// -------
	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		scores.put(event.getPlayer(), 0);
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		scores.remove(event.getPlayer());
	}
	
	@EventHandler
	public void playerKick(PlayerKickEvent event)
	{
		scores.remove(event.getPlayer());
	}
	
}
