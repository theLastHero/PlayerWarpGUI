package Utils;

import org.bukkit.ChatColor;

import com.sun.istack.internal.logging.Logger;

import sun.security.action.GetLongAction;
import PlayerWarpGUI.PlayerWarpGUI;

public class A {

	public A(PlayerWarpGUI plugin) {
		// TODO Auto-generated constructor stub
	}

	public static String b(String str, String playerName) {

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

	public static String c(String str, String playerName) {

		// replace color codes
		str = ChatColor.translateAlternateColorCodes('&', str);
		// replace [username] variable
		if (!playerName.equals(null)) {
			str = str.replace("[username]", playerName);
		}
		return str;
	}

	public static void d (String s){
		if (PlayerWarpGUI.DEBUG_MODE){
		PlayerWarpGUI.instance.getLogger().info(s);
		}
	}
}
