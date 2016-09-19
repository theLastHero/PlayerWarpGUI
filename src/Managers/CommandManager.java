package Managers;

import org.bukkit.entity.Player;

import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;

public class CommandManager {

	
	public static boolean setWarpCheck(Player player, String perm){
		if (!PlayerWarpGUI.perms.has(player, perm)) {
			player.sendMessage(A.b(" &4You do not have permission to access the &6/pwarps setwarp &4command.", player.getDisplayName()));
			return true;
		}
		return false;
	}
	
	
}
