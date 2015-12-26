package io.github.domisum.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.domisum.BuildTheWord;
import io.github.domisum.config.ConfigLocation;
import io.github.domisum.config.ConfigUtil;
import io.github.domisum.util.PlayerUtil;
import io.github.domisum.util.RandomUtil;
import io.github.domisum.word.Word;
import io.github.domisum.word.WordProvider;

public class Game
{
	
	// REFERENCES
	private WordProvider wordProvider;
	private ScoreManager scoreManager;
	
	// CONFIG VALUES
	private Location spawnLocation;
	private Location builderSpawnLocation;
	
	// STATUS
	private GameStatus gameStatus = GameStatus.WAITING;
	
	private Set<UUID> previousBuilderUUIDs = new HashSet<UUID>();
	private Player builder;
	private Word currentWord;
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public Game()
	{
		loadConfigValues();
		
		wordProvider = new WordProvider();
		scoreManager = new ScoreManager();
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
	
	
	public GameStatus getGameStatus()
	{
		return gameStatus;
	}
	
	
	// -------
	// PLAYER
	// -------
	public static void makePlayer(Player player)
	{
		player.teleport(BuildTheWord.getGame().spawnLocation);
		PlayerUtil.clear(player);
		player.setGameMode(GameMode.ADVENTURE);
	}
	
	public static void makeBuilder(Player player)
	{
		player.teleport(BuildTheWord.getGame().builderSpawnLocation);
		PlayerUtil.clear(player);
		player.setGameMode(GameMode.CREATIVE);
		player.setFlying(true);
	}
	
	
	// -------
	// PROCESS
	// -------
	public void tryStart()
	{
		if(Bukkit.getOnlinePlayers().size() >= ConfigUtil.getInt("minimumPlayers"))
			startGame();
		else
			BuildTheWord.broadcastMessage("Zum Starten des Spiels sind nicht genug Spieler online. (§b" + Bukkit.getOnlinePlayers().size() + "§f/§b"
					+ ConfigUtil.getInt("minimumPlayers") + "§f)");
	}
	
	private void startGame()
	{
		BuildTheWord.broadcastMessage("Das Spiel wird gestartet.");
		
		startRound();
	}
	
	private void startRound()
	{
		scoreManager.reset();
		BuildTheWord.broadcastMessage("Eine neue Runde wird begonnen.");
		
		nextBuild();
	}
	
	private void nextBuild()
	{
		if(builder != null)
		{
			makePlayer(builder);
			builder = null;
		}
		
		// determine next builder
		Collection<? extends Player> remainingPlayers = Bukkit.getOnlinePlayers();
		for(UUID uuid : previousBuilderUUIDs)
			remainingPlayers.removeIf((o) ->
			{
				return ((Player) o).getUniqueId() == uuid;
			});
			
		// everybody has already built
		if(remainingPlayers.size() == 0)
		{
			endRound();
			return;
		}
		
		currentWord = wordProvider.getRandomWord();
		
		// randomly select next builder
		builder = remainingPlayers.toArray(new Player[remainingPlayers.size()])[RandomUtil.nextInt(remainingPlayers.size())];
		makeBuilder(builder);
		
		BuildTheWord.sendMessage(builder, "Du wurdest zum Bauenden gewählt. Dein Wort ist '§b" + currentWord.getName() + "§f'.");
	}
	
	
	private void endRound()
	{
	
	}
	
}
