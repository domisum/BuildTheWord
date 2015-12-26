package io.github.domisum.word;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import io.github.domisum.BuildTheWord;
import io.github.domisum.db.MySQLUtil;

public class WordProvider
{
	
	// CONSTANTS
	private static final String TABLE_NAME = "words";
	private static final String SYNONYM_DELIMITER = "//";
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public WordProvider()
	{
		createTable();
	}
	
	
	// -------
	// DATABASE
	// -------
	private void createTable()
	{
		try
		{
			// @formatter:off
			String createTableSQL = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` " + 
									"(`name` varchar(64) NOT NULL, " + 
									"`synonyms` varchar(1024) NOT NULL DEFAULT ''," + 
									"PRIMARY KEY (`name`)) " + 
									"ENGINE=InnoDB DEFAULT CHARSET=latin1";
			// @formatter:on
			PreparedStatement createTableStatement = MySQLUtil.prepareStatement(createTableSQL);
			createTableStatement.executeUpdate();
		}
		catch(SQLException e)
		{
			BuildTheWord.getInstance().getLogger().log(Level.SEVERE, "An error occured while creating the table '" + TABLE_NAME + "'", e);
		}
		
		BuildTheWord.getInstance().getLogger().info("The table '" + TABLE_NAME + "' has been created sucessfully (if it didn't exist already)");
	}
	
	private Word queryRandomWord() throws SQLException
	{
		String randomWordSQL = "SELECT * from " + TABLE_NAME + " ORDER BY RAND() LIMIT 1";
		PreparedStatement randomWordStatement = MySQLUtil.prepareStatement(randomWordSQL);
		ResultSet randomWordResultSet = randomWordStatement.executeQuery();
		
		if(randomWordResultSet.next() == false)
			BuildTheWord.getInstance().getLogger().log(Level.SEVERE, "The table is empty - There are no words to choose from!",
					new IllegalStateException());
					
		Word word = new Word(randomWordResultSet.getString("name"));
		
		String synonymsString = randomWordResultSet.getString("synonyms");
		String[] synonyms = synonymsString.split(SYNONYM_DELIMITER);
		for(String synonym : synonyms)
			word.addSynonym(synonym);
			
		return word;
	}
	
	
	// -------
	// GETTERS
	// -------
	public Word getRandomWord()
	{
		// TODO add protection against duplicates
		
		try
		{
			return queryRandomWord();
		}
		catch(SQLException e)
		{
			BuildTheWord.getInstance().getLogger().log(Level.SEVERE, "An error occured while fetching a random word", e);
		}
		
		return null;
	}
	
}
