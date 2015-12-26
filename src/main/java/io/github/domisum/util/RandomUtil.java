package io.github.domisum.util;

import java.util.Random;

public class RandomUtil
{
	
	// REFERENCES
	private static Random random;
	
	
	// -------
	// GETTERS
	// -------
	public static Random getRandom()
	{
		if(random == null)
			random = new Random();
			
		return random;
	}
	
	public static int nextInt(int bound)
	{
		return getRandom().nextInt(bound);
	}
	
}