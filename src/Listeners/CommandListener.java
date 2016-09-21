package Listeners;

import java.util.UUID;

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

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class CommandListener implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return false;
		}

		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("playerwarps")) {

			// help menu
			if ((args.length == 1) && (args[0].equalsIgnoreCase("help"))) {

				player.sendMessage(A.c(" &7------------------ &8[&6PlayerWarps&8] &7---------------", player.getDisplayName()));
				player.sendMessage(A.c(" &f/pwarps list  &aview all playerWarps", player.getDisplayName()));
				player.sendMessage(A.c(" &f/pwarps set  &aset your PlayerWarp at your current location", player.getDisplayName()));
				player.sendMessage(A.c(" &f/pwarps delete  &adelete your PlayerWarp.", player.getDisplayName()));

				if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.setTitle")) {
					player.sendMessage(A.c(" &f/pwarps title Your title text  &Change the title of your PlayerWarp.", player.getDisplayName()));
				}

				if (PlayerWarpGUI.perms.has(player, "playerWarpGUI.setWarp.others")) {
					player.sendMessage(A.c(" &f/pwarps set &6{username}  &aset a PlayerWarp for {username} at your location", player.getDisplayName()));
					player.sendMessage(A.c(" &f/pwarps delete &6{username}  &adelete {username}'s PlayerWarp", player.getDisplayName()));
				}

				player.sendMessage(A.c(" &7-----------------------------------------------", player.getDisplayName()));

				return true;
			}

			// list and show pwarps
			if ((args.length < 1) || (args[0].equalsIgnoreCase("list"))) {
				if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.list")) {
					player.sendMessage(A.b(" &aYou do not have permission to access the &6/pwarps &acommand.", player.getDisplayName()));
					return true;
				}
				chestObject.openGUI(player, 0);

				return true;
			}

			// set a pwarp
			if ((args.length == 1) && (args[0].equalsIgnoreCase("setwarp") || args[0].equalsIgnoreCase("set"))) {

				// check perm
				if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.setWarp")) {
					player.sendMessage(A.b(" &aYou do not have permission to access the &6/pwarps set &acommand.", player.getDisplayName()));
					return true;
				}

				// check bal/cost
				if ((!(PlayerWarpGUI.setWarpCost == 0))) {
					EconomyResponse r = PlayerWarpGUI.econ.withdrawPlayer(player, PlayerWarpGUI.setWarpCost);
					if (!r.transactionSuccess()) {
						player.sendMessage(A.b(" &aYou do not have enough moeny to perform to: &6/pwarp set.", player.getDisplayName()));
						return true;
					}
				}

				// check if already has a pwarp
				if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
					player.sendMessage(A.b(" &aYou already have a &6/pwarp&a, you must delete it before setting a new &6/pwarp", player.getDisplayName()));
					return true;
				}

				// do safeWarp checking
				if (PlayerWarpGUI.useSafeWarp) {

					// check for beathable air blocks
					if (!PlayerWarpManager.isSafeLocation(player.getLocation())) {
						player.sendMessage(A.b(" &aYou cannot set a &6/pwarp &ain this unsafe location", player.getDisplayName()));
						return true;
					}
				}

				// check disabled worlds
				String world = player.getWorld().getName().toString();
				for (int i = 0; i < PlayerWarpGUI.disabledWorlds.size(); i++) {
					// Bukkit.broadcastMessage("pworld: " + world + " dworld: " + PlayerWarpGUI.disabledWorlds.get(i));
					if (PlayerWarpGUI.disabledWorlds.get(i).equalsIgnoreCase(world)) {
						player.sendMessage(A.b(" &6/pwarp &acannot be set in this world", player.getDisplayName()));
						return true;
					}
				}

				// GriefPrevetion
				if ((PlayerWarpGUI.enableGriefPrevetion == true) && (PlayerWarpGUI.gp.isEnabled())) {
					me.ryanhamshire.GriefPrevention.Claim isClaim = PlayerWarpGUI.gp.dataStore.getClaimAt(player.getLocation(), false, null);
					if ((isClaim == null) || !(isClaim.getOwnerName().equalsIgnoreCase(player.getName()))) {
						player.sendMessage(A.b("&aYou can only set warps inside your own claim", player.getDisplayName()));
						return true;

					}

				}

				// WorldGuard
				// !!!!!!!!!!!!!! HERE !!!!!!!!!!!!!!!!!!!!!!!
				if ((PlayerWarpGUI.enableWorldGuard == true)) {

					int count = 0;
					boolean owner = false;

					for (ProtectedRegion r : PlayerWarpGUI.wg.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation())) {
						count++;

						if (r.getOwners().contains(player.getUniqueId()) || r.getMembers().contains(player.getUniqueId())) {
							owner = true;
						}

					}

					if ((count == 0) || (owner == false)) {
						player.sendMessage(A.b("&aYou must be a owner or member of the region to set a &6/pwarp &ahere.", player.getDisplayName()));
						return false;
					}
				}

				// all ok, then set a pwarp

				// create file
				if (PlayerWarpHandler.createPlayerWarpFile(player.getUniqueId())) {

					PlayerWarpHandler.createObjectFromWarpFile(PlayerWarpHandler.savePlayerWarpObject(player.getUniqueId(), player.getLocation()));
					player.sendMessage(A.b(" &6/pwarp &ahas been set for: &6[username]", player.getDisplayName()));
				}

				return true;
			}

		}

		// delete a war/pwarpp
		if ((args.length == 1) && (args[0].equalsIgnoreCase("delete"))) {
			if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.setWarp")) {
				player.sendMessage(A.b(" &aYou do not have permission to access the &6/pwarps delete &acommand.", player.getDisplayName()));
				return true;
			}

			if (!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
				player.sendMessage(A.b(" &aYou do not have a &6/pwarp &ato delete.", player.getDisplayName()));
				return true;
			}

			PlayerWarpManager.removePlayerObject(player.getUniqueId());
			PlayerWarpHandler.deletePlayerWarpFile(player.getUniqueId());
			player.sendMessage(A.b(" &aYour &6/pwarp &ahas been deleted.", player.getDisplayName()));

			return true;
		}

		// Set a title
		if ((args.length >= 2) && (args[0].equalsIgnoreCase("title"))) {

			if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.setTitle")) {
				player.sendMessage(A.b(" &aYou do not have permission to access the &6/pwarps title &acommand.", player.getDisplayName()));
				return true;
			}

			if (args.length == 2) {
				player.sendMessage(A.b(" &aUsage: &6/pwarps title &aYour title text.", player.getDisplayName()));
			}

			if (!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
				player.sendMessage(A.b(" &aYou do not have a &6/pwarp &ato set a title for.", player.getDisplayName()));
				return true;
			}

			StringBuilder sb = new StringBuilder(); // Creating a new instance of StringBuilder
			for (int i = 1; i < args.length; i++) { // Basic for loop, going through the arguments starting from 1
				sb.append(args[i]); // Adds the argument into the StringBuilder
				sb.append(" "); // Adding a space into the StringBuilder
			}

			String title = sb.toString();
			Bukkit.broadcastMessage(title);

			PlayerWarpGUI.playerWarpManager.updatePlayerObjectTitle(player.getUniqueId(), title);
			player.sendMessage(A.b(" &aYour title has been set to " + title, player.getDisplayName()));

			return true;
		}

		// ADMIN =============================================================================================
		if (args.length == 2) {

			if (!PlayerWarpGUI.perms.has(player, "playerWarpGUI.setWarp.others")) {
				player.sendMessage(A.b(" &aYou do not have permission to set/delete warps for others.", player.getDisplayName()));
				return true;
			}

			if (args[0].equalsIgnoreCase("delete")) {

				UUID otherUUID;
				// otherUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();

				if (Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore() == true) {
					otherUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
				} else {
					player.sendMessage(A.b(" &aPlayer &6 [username] &anot found.", args[1]));
					return true;
				}

				if (!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(otherUUID)) {
					player.sendMessage(A.b(" &aPlayer &b[username] &adoes not have a &6/pwarp &aset.", Bukkit.getOfflinePlayer(otherUUID).getName()));
					return true;
				}

				PlayerWarpManager.removePlayerObject(otherUUID);
				PlayerWarpHandler.deletePlayerWarpFile(otherUUID);
				player.sendMessage(A.b(" &aPlayer &b[username] &6/pwarp &ahas been deleted.", Bukkit.getOfflinePlayer(otherUUID).getName()));

				return true;

			}

			if (args[0].equalsIgnoreCase("set")) {

				UUID otherUUID;

				if (Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore() == true) {
					otherUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
				} else {
					player.sendMessage(A.b(" &aPlayer &6 [username] &anot found.", args[1]));
					return true;
				}

				// check if already has a pwarp
				if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(otherUUID)) {
					player.sendMessage(A.b(" &aPlayer &b[username] &aalready has a &6/pwarp &aset, you must delete it before etting a new &6/pwarp", Bukkit.getOfflinePlayer(otherUUID).getName()));
					return true;
				}

				// do safeWarp checking
				if (PlayerWarpGUI.useSafeWarp) {

					// check for beathable air blocks
					if (!PlayerWarpManager.isSafeLocation(player.getLocation())) {
						player.sendMessage(A.b(" &aCannot set a &6/pwarp &ain this unsafe location", player.getDisplayName()));
						return true;
					}
				}

				// check disabled worlds
				String world = player.getWorld().getName().toString();
				for (int i = 0; i < PlayerWarpGUI.disabledWorlds.size(); i++) {
					// Bukkit.broadcastMessage("pworld: " + world + " dworld: " + PlayerWarpGUI.disabledWorlds.get(i));
					if (PlayerWarpGUI.disabledWorlds.get(i).equalsIgnoreCase(world)) {
						player.sendMessage(A.b(" &6/pwarp &acannot be set in this world", player.getDisplayName()));
						return true;
					}
				}

				// GriefPrevetion
				if ((PlayerWarpGUI.enableGriefPrevetion == true) && (PlayerWarpGUI.gp.isEnabled())) {
					me.ryanhamshire.GriefPrevention.Claim isClaim = PlayerWarpGUI.gp.dataStore.getClaimAt(player.getLocation(), false, null);
					if ((isClaim == null) || !(isClaim.getOwnerName().equalsIgnoreCase(Bukkit.getOfflinePlayer(otherUUID).getName()))) {
						player.sendMessage(A.b("&aYou can only set warps inside the players own claim", Bukkit.getOfflinePlayer(otherUUID).getName()));
						return true;

					}

				}

				// WorldGuard
				// !!!!!!!!!!!!!! HERE !!!!!!!!!!!!!!!!!!!!!!!
				if ((PlayerWarpGUI.enableWorldGuard == true)) {

					int count = 0;
					boolean owner = false;

					for (ProtectedRegion r : PlayerWarpGUI.wg.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation())) {
						count++;

						if (r.getOwners().contains(otherUUID) || r.getMembers().contains(otherUUID)) {
							owner = true;
						}

					}

					if ((count == 0) || (owner == false)) {
						player.sendMessage(A.b("&aPlayer &b[username]&a must be a owner or member of the region to set a &6/pwarp here.", player.getDisplayName()));
						return false;
					}
				}

				// all ok, then set a pwarp

				// create file
				if (PlayerWarpHandler.createPlayerWarpFile(otherUUID)) {

					PlayerWarpHandler.createObjectFromWarpFile(PlayerWarpHandler.savePlayerWarpObject(otherUUID, player.getLocation()));
					player.sendMessage(A.b(" &6/pwarp &ahas been set for: &6[username]", Bukkit.getOfflinePlayer(otherUUID).getName()));
				}

				return true;

			}

		}

		return false;
	}

}
