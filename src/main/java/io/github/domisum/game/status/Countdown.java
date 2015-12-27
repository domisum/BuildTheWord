package io.github.domisum.game.status;

import org.bukkit.scheduler.BukkitRunnable;

import io.github.domisum.BuildTheWord;

public class Countdown extends BukkitRunnable
{
	
	// GAME
	private int secondsLeft;
	private TimedRunnable stepAction;
	private Runnable endAction;
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public Countdown(int seconds, TimedRunnable stepAction, Runnable endAction)
	{
		this.secondsLeft = seconds;
		this.stepAction = stepAction;
		this.endAction = endAction;
	}
	
	public Countdown(int seconds, Runnable endAction)
	{
		this(seconds, null, endAction);
	}
	
	
	// -------
	// RUNNING
	// -------
	public int getSecondsLeft()
	{
		return secondsLeft;
	}
	
	// -------
	// RUNNING
	// -------
	public void start()
	{
		runTaskTimer(BuildTheWord.getInstance(), 20, 20);
	}
	
	public void end()
	{
		cancel();
		
		if(endAction != null)
			endAction.run();
	}
	
	@Override
	public void run()
	{
		secondsLeft--;
		
		if(secondsLeft == 0)
			end();
		else if(stepAction != null)
			stepAction.run(secondsLeft);
	}
	
	
	// -------
	// TIMED RUNNABLE
	// -------
	public interface TimedRunnable
	{
		
		public void run(int value);
		
	}
	
}
