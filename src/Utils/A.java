package Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.ChatColor;

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
		//replace [cost]
		str = str.replace("[cost]", (Integer.toString(PlayerWarpGUI.setWarpCost)));
		
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
	
	// -------------------------------------------------------------------------------------
	// copy
	// -------------------------------------------------------------------------------------
	public static void copy(InputStream in, File file) {

		try {

			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {

				out.write(buf, 0, len);

			}
			out.close();
			in.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
	
}
