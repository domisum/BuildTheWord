package io.github.domisum.util;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PlayerUtil
{
	
	public static void clear(Player player)
	{
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		player.setLevel(0);
		player.setExp(0);
		
		player.setHealth(((Damageable) player).getMaxHealth());
		player.setSaturation(5);
		player.setFoodLevel(20);
		
		for(PotionEffect pe : player.getActivePotionEffects())
			player.removePotionEffect(pe.getType());
	}
	
}
