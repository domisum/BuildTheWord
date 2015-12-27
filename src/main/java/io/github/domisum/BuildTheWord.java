package io.github.domisum;

import java.io.File;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.domisum.config.ConfigUtil;
import io.github.domisum.db.MySQLUtil;
import io.github.domisum.game.Game;
import io.github.domisum.game.GameListener;
import io.github.domisum.game.ScoreboardManager;

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
		new GameListener();
		
		getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " has been enabled\n");
		
		
		// reload interaction - this makes the plugin compatible with the /reload command
		for(Player p : Bukkit.getOnlinePlayers())
			Game.makePlayer(p);
			
		game.getScoreManager().reload();
		ScoreboardManager.updateScoreboard();
		game.tryStartGame();
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
	
	
	// -------
	// MESSAGING
	// -------
	public static void broadcastMessage(String message)
	{
		broadcastMessage(Bukkit.getOnlinePlayers(), message);
	}
	
	public static void broadcastMessage(Collection<? extends Player> recipients, String message)
	{
		for(Player p : recipients)
			p.sendMessage(ConfigUtil.getStringCC("pluginPrefix") + message);
			
		getInstance().getLogger().info("BROADCAST <- '" + ChatColor.stripColor(message) + "'");
	}
	
	public static void sendMessage(Player player, String message)
	{
		player.sendMessage(ConfigUtil.getStringCC("pluginPrefix") + message);
		
		getInstance().getLogger().info(player.getName() + " <- '" + ChatColor.stripColor(message) + "'");
	}
	
}
