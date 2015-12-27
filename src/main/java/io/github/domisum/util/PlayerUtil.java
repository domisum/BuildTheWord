package io.github.domisum.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
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
	
	public static void sendActionBar(Player player, String message)
	{
		try
		{
			Class<?> packetPlayOutChatClass = Class.forName(getNMSPath() + "PacketPlayOutChat");
			Class<?> chatComponentTextClass = Class.forName(getNMSPath() + "ChatComponentText");
			Class<?> iChatBaseComponentClass = Class.forName(getNMSPath() + "IChatBaseComponent");
			Object chatComponentText = chatComponentTextClass.getConstructor(new Class<?>[] { String.class }).newInstance(message);
			Object packetPlayOutChat = packetPlayOutChatClass.getConstructor(new Class<?>[] { iChatBaseComponentClass, byte.class })
					.newInstance(chatComponentText, (byte) 2);
					
			// send packet
			Class<?> craftPlayerClass = Class.forName(getCBPath() + "entity.CraftPlayer");
			Object craftPlayer = craftPlayerClass.cast(player);
			Method getHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
			Object entityPlayer = getHandleMethod.invoke(craftPlayer);
			
			Field playerConnectionField = entityPlayer.getClass().getDeclaredField("playerConnection");
			Object playerConnection = playerConnectionField.get(entityPlayer);
			
			Class<?> packetClass = Class.forName(getNMSPath() + "Packet");
			Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
			sendPacketMethod.invoke(playerConnection, packetPlayOutChat);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private static String getNMSPath()
	{
		return "net.minecraft.server." + getVersion() + ".";
	}
	
	private static String getCBPath()
	{
		return "org.bukkit.craftbukkit." + getVersion() + ".";
	}
	
	private static String getVersion()
	{
		// from Skionz (https://bukkit.org/threads/basic-reflection-tutorial.329127/)
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
}
