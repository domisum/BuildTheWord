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
	
	public Player getBuilder()
	{
		return builder;
	}
	
	public Word getCurrentWord()
	{
		return currentWord;
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
	
	
	public void guessWord(Player player)
	{
		Collection<? extends Player> everybodyElse = Bukkit.getOnlinePlayers();
		everybodyElse.remove(player);
		
		BuildTheWord.sendMessage(player, "Du hast das Wort erraten!");
		BuildTheWord.broadcastMessage(everybodyElse, "§b" + player.getName() + "$f hat das Wort erraten!");
		
		
		// TODO start end countdown
	}
	
	
	// -------
	// PROCESS
	// -------
	public void tryStartGame()
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
		previousBuilderUUIDs.clear();
		builder = null;
		currentWord = null;
		
		BuildTheWord.broadcastMessage("Eine neue Runde wird begonnen.");
		
		startBuild();
	}
	
	private void startBuild()
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
		
		// TODO start round countdown
	}
	
	private void endBuild()
	{
		
		
		startBuild();
	}
	
	
	private void endRound()
	{
	
	}
	
}
