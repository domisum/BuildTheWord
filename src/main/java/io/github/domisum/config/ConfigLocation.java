package io.github.domisum.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLocation extends Location
{
	
	// CONSTANTS
	private static final String WORLD_SUBPATH = ".world";
	private static final String X_SUBPATH = ".x";
	private static final String Y_SUBPATH = ".y";
	private static final String Z_SUBPATH = ".z";
	private static final String YAW_SUBPATH = ".yaw";
	private static final String PITCH_SUBPATH = ".pitch";
	
	// STATUS
	private FileConfiguration config;
	private String path;
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public ConfigLocation(FileConfiguration config, String path)
	{
		super(Bukkit.getWorlds().get(0), 0, 0, 0);
		
		this.config = config;
		this.path = path;
	}
	
	public ConfigLocation(FileConfiguration config, String path, Location model)
	{
		super(model.getWorld(), model.getX(), model.getY(), model.getZ(), model.getYaw(), model.getPitch());
		
		this.config = config;
		this.path = path;
	}
	
	
	// -------
	// LOADING
	// -------
	public ConfigLocation load()
	{
		// # world
		String worldPath = path + WORLD_SUBPATH;
		if(!config.isSet(worldPath))
			throw new IllegalArgumentException("An error occured while loading config location from path '" + path + ". The world value is not set.");
			
		String worldName = config.getString(worldPath);
		World world = Bukkit.getWorld(worldName);
		if(world == null)
			throw new IllegalArgumentException(
					"An error occured while loading config location from path '" + path + ". The world '" + worldName + "' is invalid.");
					
		setWorld(world);
		
		
		// # x
		String xPath = path + X_SUBPATH;
		if(!config.isSet(xPath))
			throw new IllegalArgumentException("An error occured while loading config location from path '" + path + ". The x value is not set.");
			
		setX(config.getDouble(xPath));
		
		// # y
		String yPath = path + Y_SUBPATH;
		if(!config.isSet(yPath))
			throw new IllegalArgumentException("An error occured while loading config location from path '" + path + ". The y value is not set.");
			
		setY(config.getDouble(yPath));
		
		// # z
		String zPath = path + Z_SUBPATH;
		if(!config.isSet(zPath))
			throw new IllegalArgumentException("An error occured while loading config location from path '" + path + ". The z value is not set.");
			
		setZ(config.getDouble(zPath));
		
		
		// # yaw
		String yawPath = path + YAW_SUBPATH;
		if(config.isSet(yawPath))
			setYaw((float) config.getDouble(yawPath));
			
			
		// # pitch
		String pitchPath = path + PITCH_SUBPATH;
		if(config.isSet(pitchPath))
			setPitch((float) config.getDouble(pitchPath));
			
		return this;
	}
	
	
	// -------
	// SAVING
	// -------
	public ConfigLocation save()
	{
		// # world
		String worldPath = path + WORLD_SUBPATH;
		String worldValue = getWorld().getName();
		config.set(worldPath, worldValue);
		
		// # x
		String xPath = path + X_SUBPATH;
		double xValue = getX();
		config.set(xPath, xValue);
		
		// # y
		String yPath = path + Y_SUBPATH;
		double yValue = getY();
		config.set(yPath, yValue);
		
		// # z
		String zPath = path + Z_SUBPATH;
		double zValue = getZ();
		config.set(zPath, zValue);
		
		
		// # yaw
		String yawPath = path + YAW_SUBPATH;
		float yawValue = getYaw();
		config.set(yawPath, yawValue);
		
		// # pitch
		String pitchPath = path + PITCH_SUBPATH;
		float pitchValue = getPitch();
		config.set(pitchPath, pitchValue);
		
		return this;
	}
	
}