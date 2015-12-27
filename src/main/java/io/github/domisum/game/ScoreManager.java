package io.github.domisum.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
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
	// GETTERS
	// -------
	public int getScore(Player player)
	{
		return scores.get(player);
	}
	
	public Set<Player> getHighestScorers()
	{
		int highestScore = 0;
		Set<Player> highestScorers = new HashSet<Player>();
		
		for(Entry<Player, Integer> entry : scores.entrySet())
		{
			if(entry.getValue() > highestScore)
			{
				highestScore = entry.getValue();
				highestScorers.clear();
				highestScorers.add(entry.getKey());
			}
			else if(entry.getValue() == highestScore)
				highestScorers.add(entry.getKey());
		}
		
		return highestScorers;
	}
	
	
	// -------
	// CHANGERS
	// -------
	public void score(Player player, int score)
	{
		scores.put(player, getScore(player) + score);
		
		BuildTheWord.sendMessage(player, "Du erhälst §b" + score + "§f Punkte.");
	}
	
	public void reset()
	{
		for(Player key : scores.keySet())
			scores.put(key, 0);
	}
	
	public void reload()
	{
		for(Player p : Bukkit.getOnlinePlayers())
			scores.put(p, 0);
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
