package io.github.domisum.word;

import java.util.ArrayList;
import java.util.List;

public class Word
{
	
	// STATUS
	private String name;
	
	private List<String> synonyms = new ArrayList<String>();
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public Word(String name)
	{
		this.name = name;
	}
	
	
	// -------
	// GETTERS
	// -------
	public String getName()
	{
		return name;
	}
	
	public boolean isContainedIn(String message)
	{
		message = message.toLowerCase();
		message = message.replaceAll("\\s+", ""); // remove all spaces
		
		if(message.contains(name))
			return true;
			
		for(String synonym : synonyms)
			if(message.contains(synonym))
				return true;
				
		return false;
	}
	
	
	// -------
	// CHANGERS
	// -------
	public void addSynonym(String synonym)
	{
		synonyms.add(synonym);
	}
	
}
