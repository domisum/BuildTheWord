package io.github.domisum.util;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class LanguageUtil
{
	
	public static String getPlayerNameList(Set<Player> players)
	{
		Set<Player> remainingPlayers = new HashSet<Player>(players);
		String nameList = "";
		
		while(remainingPlayers.size() > 0)
		{
			Player player = remainingPlayers.iterator().next();
			remainingPlayers.remove(player);
			
			nameList += getPlayerNameWithColors(player);
			
			if(remainingPlayers.size() == 1)
				nameList += " und ";
			else if(remainingPlayers.size() >= 2)
				nameList += ", ";
		}
		
		return nameList;
	}
	
	public static String getPlayerNameWithColors(Player player)
	{
		return "§b" + player.getName() + "§f";
	}
	
}
