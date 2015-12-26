package io.github.domisum.game;

import io.github.domisum.word.WordProvider;

public class Game
{
	
	// REFERENCES
	private WordProvider wordProvider;
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public Game()
	{
		wordProvider = new WordProvider();
	}
	
	
	// -------
	// GETTERS
	// -------
	public WordProvider getWordProvider()
	{
		return wordProvider;
	}
	
}
