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
import io.github.domisum.game.status.Countdown;
import io.github.domisum.game.status.Countdown.TimedRunnable;
import io.github.domisum.game.status.GameStatus;
import io.github.domisum.util.LanguageUtil;
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
	
	private Countdown buildingCountdown;
	private Countdown buildingEndCountdown;
	
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
	
	public ScoreManager getScoreManager()
	{
		return scoreManager;
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
		Collection<Player> everybodyElse = new HashSet<Player>(Bukkit.getOnlinePlayers());
		everybodyElse.remove(player);
		
		BuildTheWord.sendMessage(player, "Du hast das Wort erraten!");
		BuildTheWord.broadcastMessage(everybodyElse, "§b" + player.getName() + "§f hat das Wort erraten!");
		
		if(buildingEndCountdown == null) // if this countdown has not started yet, the guess was the first one
		{
			// scores
			scoreManager.score(player, ConfigUtil.getInt("scoreFirstGuess"));
			scoreManager.score(builder, ConfigUtil.getInt("scoreBuildingGuessed"));
			
			// countdowns
			buildingCountdown.cancel();
			buildingCountdown = null;
			
			TimedRunnable stepAction = (timeLeft) ->
			{
				for(Player p : Bukkit.getOnlinePlayers())
					PlayerUtil.sendActionBar(p, "§b" + timeLeft + "§fs verbleibend");
			};
			buildingEndCountdown = new Countdown(ConfigUtil.getInt("buildingEndDuration"), stepAction, () -> endBuild(true));
			buildingEndCountdown.start();
		}
		else
			scoreManager.score(player, ConfigUtil.getInt("scoreFollowingGuesses"));
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
		
		BuildTheWord.broadcastMessage("Eine neue Runde hat begonnen.");
		
		startBuild();
	}
	
	private void startBuild()
	{
		if(builder != null)
		{
			previousBuilderUUIDs.add(builder.getUniqueId());
			makePlayer(builder);
			builder = null;
		}
		
		// determine next builder
		Set<Player> remainingPlayers = new HashSet<Player>(Bukkit.getOnlinePlayers());
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
		
		BuildTheWord.sendMessage(builder, "Du wurdest fürs Bauen gewählt. Dein Wort ist '§b" + currentWord.getName() + "§f'.");
		
		TimedRunnable stepAction = (timeLeft) ->
		{
			for(Player p : Bukkit.getOnlinePlayers())
				PlayerUtil.sendActionBar(p, "§b" + timeLeft + "§fs verbleibend");
		};
		
		buildingCountdown = new Countdown(ConfigUtil.getInt("buildingDuration"), stepAction, () -> endBuild(false));
		buildingCountdown.start();
	}
	
	private void endBuild(boolean guessed)
	{
		if(buildingCountdown != null)
		{
			buildingCountdown.cancel();
			buildingCountdown = null;
		}
		buildingEndCountdown = null;
		
		String message = "Die Runde ist vorbei.";
		if(!guessed)
			message += " Niemand hat das Wort erraten.";
			
		message += " Das Wort war '§b" + currentWord.getName() + "§f'.";
		BuildTheWord.broadcastMessage(message);
		
		startBuild();
	}
	
	
	private void endRound()
	{
		BuildTheWord.broadcastMessage("Die Runde ist vorbei.");
		
		// announce winner
		Set<Player> winners = scoreManager.getHighestScorers();
		if(winners.size() == 0)
			BuildTheWord.broadcastMessage("Niemand hat die Runde gewonnen. Schade!");
		else if(winners.size() == 1)
			BuildTheWord.broadcastMessage("§b" + winners.iterator().next() + "§f hat die Runde gewonnen. Glückwunsch!");
		else
			BuildTheWord.broadcastMessage(LanguageUtil.getPlayerNameList(winners) + " haben die Runde gewonnen. Glückwunsch!");
			
		// start next round
		Bukkit.getScheduler().runTaskLater(BuildTheWord.getInstance(), () -> startRound(), ConfigUtil.getInt("afterRoundDuration") * 20);
	}
	
}
