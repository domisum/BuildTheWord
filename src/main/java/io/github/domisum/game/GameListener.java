package io.github.domisum.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.domisum.BuildTheWord;
import io.github.domisum.game.status.GameStatus;
import io.github.domisum.word.Word;

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
		
		Game.makePlayer(player);
		
		BuildTheWord.broadcastMessage(event.getJoinMessage());
		event.setJoinMessage(null);
		
		if(BuildTheWord.getGame().getGameStatus() == GameStatus.WAITING)
			BuildTheWord.getGame().tryStartRound();
	}
	
	
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event)
	{
		// make chat look nice
		event.setFormat("§b%1$s§f: %2$s");
		
		Player player = event.getPlayer();
		
		Word currentWord = BuildTheWord.getGame().getCurrentWord();
		if(currentWord == null)
			return;
			
		if(!currentWord.isContainedIn(event.getMessage()))
			return;
			
		// only the writing player should be able to see the guess
		event.getRecipients().clear();
		event.getRecipients().add(player);
		
		// the builder can't guess his own word!
		if(player == BuildTheWord.getGame().getBuilder())
		{
			event.setCancelled(true);
			Bukkit.getScheduler().runTaskLater(BuildTheWord.getInstance(), () -> BuildTheWord.sendMessage(player, "Verrate nicht das Wort!"), 0);
			return;
		}
		
		Bukkit.getScheduler().runTaskLater(BuildTheWord.getInstance(), () -> BuildTheWord.getGame().guessWord(player), 0);
	}
	
}
