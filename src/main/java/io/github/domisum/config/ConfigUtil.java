package io.github.domisum.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import io.github.domisum.BuildTheWord;

public class ConfigUtil
{
	
	// VALUES
	private static Map<String, String> valuesString = new HashMap<String, String>();
	private static Map<String, Integer> valuesInt = new HashMap<String, Integer>();
	
	private static Map<String, String> valuesStringCC = new HashMap<String, String>();
	
	
	// -------
	// GETTERS
	// -------
	public static String getString(String path)
	{
		if(valuesString.containsKey(path))
			return valuesString.get(path);
			
		if(!getConfig().isSet(path))
			throw new IllegalArgumentException("The config does not contain a String at the path '" + path + "'.");
			
		String value = getConfig().getString(path);
		valuesString.put(path, value);
		
		return value;
	}
	
	public static int getInt(String path)
	{
		if(valuesInt.containsKey(path))
			return valuesInt.get(path);
			
		if(!getConfig().isSet(path))
			throw new IllegalArgumentException("The config does not contain an Integer at the path '" + path + "'.");
			
		int value = getConfig().getInt(path);
		valuesInt.put(path, value);
		
		return value;
	}
	
	
	public static String getStringCC(String path)
	{
		if(valuesStringCC.containsKey(path))
			return valuesStringCC.get(path);
			
		String value = getString(path);
		value = ChatColor.translateAlternateColorCodes('&', value) + ChatColor.RESET;
		
		valuesStringCC.put(path, value);
		return value;
	}
	
	
	// -------
	// UTIL
	// -------
	private static FileConfiguration getConfig()
	{
		return BuildTheWord.getInstance().getConfig();
	}
	
}