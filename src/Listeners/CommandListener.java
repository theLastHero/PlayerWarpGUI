package Listeners;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import FileHandlers.PlayerWarpHandler;
import Managers.PlayerWarpManager;
import PlayerWarpGUI.PlayerWarpGUI;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return false;
		}

		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("playerwarps")) {

			// list and show pwarps
			if ((args.length < 1) || (args[0].equalsIgnoreCase("list"))) {
				if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.list")) {
					player.sendMessage(s(PlayerWarpGUI.messagePrefix) + s(" &4You do not have permission to access the &6/pwarps &4command."));
					return true;
				}
				PlayerWarpGUI.chestObject.openGUI(player, 0);

				return true;
			}

			// set a pwarp
			if ((args.length == 1) && (args[0].equalsIgnoreCase("setwarp") || args[0].equalsIgnoreCase("set"))) {

				// check perm
				if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.setWarp")) {
					player.sendMessage(s(PlayerWarpGUI.messagePrefix) + s(" &4You do not have permission to access the &6/pwarps setwarp &4command."));
					return true;
				}

				// check bal/cost
				if ((!(PlayerWarpGUI.setWarpCost == 0))) {
					EconomyResponse r = PlayerWarpGUI.econ.withdrawPlayer(player, PlayerWarpGUI.setWarpCost);
					if (!r.transactionSuccess()) {
						player.sendMessage(s(PlayerWarpGUI.messagePrefix) + s(" &4You do not have enough moeny to perform to: &6/pwarp setWarp."));
						return true;
					}
				}

				// check if already has a pwarp
				if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
					player.sendMessage(s(PlayerWarpGUI.messagePrefix) + s(" &4You already have a /pwarp, you must delete it before etting a new /pwarp"));
					return true;
				}
				;

				// do safeWarp checking
				if (PlayerWarpGUI.useSafeWarp) {

					// check for beathable air blocks
					if (!PlayerWarpManager.isSafeLocation(player.getLocation())) {
						player.sendMessage(s(PlayerWarpGUI.messagePrefix) + s(" &4You cannot set a /pwarp in this unsafe location"));
						return true;
					}
				}

				// all ok, then set a pwarp

				// create file
				if (PlayerWarpHandler.createPlayerWarpFile(player.getUniqueId())) {

					PlayerWarpHandler.createObjectFromWarpFile(PlayerWarpHandler.savePlayerWarpObject(player.getUniqueId(), player.getLocation()));

				}

				return true;
			}

		}

		return false;
	}
	

	public String s(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

}
