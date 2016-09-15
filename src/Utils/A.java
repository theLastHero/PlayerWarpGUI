package Utils;

import org.bukkit.ChatColor;

import PlayerWarpGUI.PlayerWarpGUI;

public class A {

	public A(PlayerWarpGUI plugin) {
		// TODO Auto-generated constructor stub
	}

	
	public static String b(String str, String playerName){

		// add prefix
		str = PlayerWarpGUI.messagePrefix + str;
		// replace color codes
		str = ChatColor.translateAlternateColorCodes('&', str);
		// replace [username] variable
		if (!playerName.equals(null)) {
			str = str.replace("[username]", playerName);
		}
		return str;
	}
	
	public static String c(String str, String playerName){

		// replace color codes
		str = ChatColor.translateAlternateColorCodes('&', str);
		// replace [username] variable
		if (!playerName.equals(null)) {
			str = str.replace("[username]", playerName);
		}
		return str;
	}
	
}
