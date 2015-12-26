package io.github.domisum.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import io.github.domisum.BuildTheWord;
import io.github.domisum.config.ConfigUtil;

public class MySQLUtil
{
	
	// REFERENCES
	private static Connection connection;
	
	
	// -------
	// CONNECTION
	// -------
	public static boolean establishConnection()
	{
		if(connection != null)
			return true;
			
		BuildTheWord.getInstance().getLogger().info("Establishing MySQL database connection...");
		
		try
		{
			// create database if it does not exist, create connection without database specification first
			String connectionUrl = "jdbc:mysql://" + ConfigUtil.getString("mysqlHost") + ":" + ConfigUtil.getInt("mysqlPort") + "?autoReconnect=true";
			connection = DriverManager.getConnection(connectionUrl, ConfigUtil.getString("mysqlUsername"), ConfigUtil.getString("mysqlPassword"));
			
			BuildTheWord.getInstance().getLogger().info("Connection established successfully");
			
			// create database
			String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + ConfigUtil.getString("mysqlDatabaseName");
			PreparedStatement createDatabaseStatement = prepareStatement(createDatabaseSQL);
			createDatabaseStatement.executeUpdate();
			
			BuildTheWord.getInstance().getLogger().info(
					"The database '" + ConfigUtil.getString("mysqlDatabaseName") + "' has been created sucessfully (if it didn't exist already)");
					
			// use database
			String selectDatabaseSQL = "USE " + ConfigUtil.getString("mysqlDatabaseName");
			PreparedStatement selectDatabaseStatement = prepareStatement(selectDatabaseSQL);
			selectDatabaseStatement.executeUpdate();
			
			BuildTheWord.getInstance().getLogger()
					.info("The database '" + ConfigUtil.getString("mysqlDatabaseName") + "' has been put in use successfully");
		}
		catch(SQLException e)
		{
			BuildTheWord.getInstance().getLogger().log(Level.SEVERE, "An error occured while trying to establish the database connection", e);
		}
		
		return true;
	}
	
	public static void close()
	{
		try
		{
			if(connection != null)
				connection.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	// -------
	// CONNECTION
	// -------
	public static PreparedStatement prepareStatement(String sql) throws SQLException
	{
		return connection.prepareStatement(sql);
	}
	
}
