package Listeners;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import FileHandlers.PlayerWarpHandler;
import Managers.PlayerWarpManager;
import Objects.chestObject;
import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;

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
					player.sendMessage(A.b(" &4You do not have permission to access the &6/pwarps &4command.", player.getDisplayName()));
					return true;
				}
				chestObject.openGUI(player, 0);

				return true;
			}

			// set a pwarp
			if ((args.length == 1) && (args[0].equalsIgnoreCase("setwarp") || args[0].equalsIgnoreCase("set"))) {

				// check perm
				if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.setWarp")) {
					player.sendMessage(A.b(" &4You do not have permission to access the &6/pwarps setwarp &4command.", player.getDisplayName()));
					return true;
				}

				// check bal/cost
				if ((!(PlayerWarpGUI.setWarpCost == 0))) {
					EconomyResponse r = PlayerWarpGUI.econ.withdrawPlayer(player, PlayerWarpGUI.setWarpCost);
					if (!r.transactionSuccess()) {
						player.sendMessage(A.b(" &4You do not have enough moeny to perform to: &6/pwarp setWarp.", player.getDisplayName()));
						return true;
					}
				}

				// check if already has a pwarp
				if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
					player.sendMessage(A.b(" &4You already have a /pwarp, you must delete it before etting a new /pwarp", player.getDisplayName()));
					return true;
				}

				// do safeWarp checking
				if (PlayerWarpGUI.useSafeWarp) {

					// check for beathable air blocks
					if (!PlayerWarpManager.isSafeLocation(player.getLocation())) {
						player.sendMessage(A.b(" &4You cannot set a /pwarp in this unsafe location", player.getDisplayName()));
						return true;
					}
				}
				
				//check disabled worlds
				String world = player.getWorld().getName().toString();
				for(int i = 0; i < PlayerWarpGUI.disabledWorlds.size(); i++){
					Bukkit.broadcastMessage("pworld: " + world + " dworld: "+ PlayerWarpGUI.disabledWorlds.get(i));
					  if (PlayerWarpGUI.disabledWorlds.get(i).equalsIgnoreCase(world)){
							player.sendMessage(A.b(" &4/pwarp cannot be set in this world", player.getDisplayName()));
						  return true;
					  }
					}

				// all ok, then set a pwarp

				// create file
				if (PlayerWarpHandler.createPlayerWarpFile(player.getUniqueId())) {

					PlayerWarpHandler.createObjectFromWarpFile(PlayerWarpHandler.savePlayerWarpObject(player.getUniqueId(), player.getLocation()));
					player.sendMessage(A.b(" &4/pwarp has been set for: &6[username]", player.getDisplayName()));
				}

				return true;
			}

		}

		// delete a war/pwarpp
		if ((args.length == 1) && (args[0].equalsIgnoreCase("delete"))) {
			if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.setWarp")) {
				player.sendMessage(A.b(" &4You do not have permission to access the &6/pwarps delete &4command.", player.getDisplayName()));
				return true;
			}
			
			if(!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())){
				player.sendMessage(A.b(" &4You do not have a /pwarp set.", player.getDisplayName()));
			}
			
			PlayerWarpManager.removePlayerObject(player.getUniqueId());
			PlayerWarpHandler.deletePlayerWarpFile(player.getUniqueId());
			player.sendMessage(A.b(" &4Your /pwarp has been deleted.", player.getDisplayName()));
			
			return true;
		}
		
		
		

		return false;
	}
}
