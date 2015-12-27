package io.github.domisum.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import io.github.domisum.BuildTheWord;

public class ScoreboardManager
{
	
	// -------
	// SCOREBOARD
	// -------
	public static void updateScoreboard()
	{
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		
		Objective objective = scoreboard.getObjective("btwScores");
		if(objective != null)
			objective.unregister();
			
		objective = scoreboard.registerNewObjective("btwScores", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("§bBuildTheWord");
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			Score sc = objective.getScore(p.getName());
			sc.setScore(BuildTheWord.getGame().getScoreManager().getScore(p));
			
			p.setScoreboard(scoreboard);
		}
	}
	
}
