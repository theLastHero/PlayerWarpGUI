package CommandActions;

import org.bukkit.entity.Player;

import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;

public class Help {
	
	public void printHelpMenu(Player player) {
		
		player.sendMessage(
				A.c(" &7------------------ &8[&6PlayerWarps&8] &7---------------", player.getDisplayName()));

		player.sendMessage(A.c(" &f/pwarps list  &aview all playerWarps", player.getDisplayName()));

		player.sendMessage(
				A.c(" &f/pwarps set  &aset your PlayerWarp at your current location", player.getDisplayName()));
		player.sendMessage(A.c(" &f/pwarps delete  &adelete your PlayerWarp.", player.getDisplayName()));
		player.sendMessage(
				A.c(" &f/pwarps icon  &aChange your warp icon to the item in hand.", player.getDisplayName()));
		player.sendMessage(A.c(
				" &f/pwarps title Your title text  &aChange the title of your warp text. Supports colorcodes.",
				player.getDisplayName()));
		player.sendMessage(A.c(
				" &f/pwarps lore1 Your Lore text  &aChange text for the lore of your warp. Also lore2, lore3. Supports colorcodes.",
				player.getDisplayName()));

		if (PlayerWarpGUI.perms.has(player, "playerwarpgui.setwarp.others")) {
			player.sendMessage(
					A.c(" &f/pwarps set &6{username}  &aset a PlayerWarp for {username} at your location",
							player.getDisplayName()));
			player.sendMessage(A.c(" &f/pwarps delete &6{username}  &adelete {username}'s PlayerWarp",
					player.getDisplayName()));
		}

		player.sendMessage(A.c(" &7-----------------------------------------------", player.getDisplayName()));

		
	}

}
