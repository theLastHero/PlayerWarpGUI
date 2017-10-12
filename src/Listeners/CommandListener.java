package Listeners;

import java.util.UUID;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import FileHandlers.PlayerWarpHandler;
import Managers.PlayerWarpManager;
import Objects.chestObject;
import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class CommandListener implements CommandExecutor {
	public static PlayerWarpGUI plugin;

	// -------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------
	public CommandListener(PlayerWarpGUI playerWarpGUI) {
		plugin = playerWarpGUI;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return false;
		}

		final Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("playerwarps")) {

			// help menu
			if ((args.length == 1) && (args[0].equalsIgnoreCase("help"))) {

				PlayerWarpGUI.help.printHelpMenu(player);
				return true;
			}

			// list and show pwarps
			if ((args.length < 1) || (args[0].equalsIgnoreCase("list"))) {
				if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.list")) {
					player.sendMessage(A.b(PlayerWarpGUI.noPermission, player.getDisplayName()));
					return true;
				}
				chestObject.openGUI(player, 0);

				return true;
			}

			// set a pwarp
			if ((args.length == 1) && (args[0].equalsIgnoreCase("setwarp") || args[0].equalsIgnoreCase("set"))) {

				// check perm
				if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.setwarp")) {
					player.sendMessage(A.b(PlayerWarpGUI.noPermission, player.getDisplayName()));
					return true;
				}

				// check bal/cost
				if ((!(PlayerWarpGUI.setWarpCost == 0))) {
					EconomyResponse r = PlayerWarpGUI.econ.withdrawPlayer(player, PlayerWarpGUI.setWarpCost);
					if (!r.transactionSuccess()) {
						player.sendMessage(A.b(PlayerWarpGUI.notEnoughMoney, player.getDisplayName()));
						return true;
					} else {
						player.sendMessage(A.b(PlayerWarpGUI.withdrawn, player.getDisplayName()));
					}
				}

				// check if already has a pwarp
				if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
					player.sendMessage(A.b(PlayerWarpGUI.alreadyHaveWarpSet, player.getDisplayName()));
					return true;
				}

				// do safeWarp checking
				if (PlayerWarpGUI.useSafeWarp) {

					// check for beathable air blocks
					if (!PlayerWarpManager.isSafeLocation(player.getLocation())) {
						player.sendMessage(A.b(PlayerWarpGUI.setInUnsafeLocation, player.getDisplayName()));
						return true;
					}
				}

				// check disabled worlds
				String world = player.getWorld().getName().toString();
				for (int i = 0; i < PlayerWarpGUI.disabledWorlds.size(); i++) {
					// Bukkit.broadcastMessage("pworld: " + world + " dworld: " +
					// PlayerWarpGUI.disabledWorlds.get(i));
					if (PlayerWarpGUI.disabledWorlds.get(i).equalsIgnoreCase(world)) {
						player.sendMessage(A.b(PlayerWarpGUI.setInDisabledWorld, player.getDisplayName()));
						return true;
					}
				}

				// RedProtect
				if ((PlayerWarpGUI.enableRedProtect == true) && (PlayerWarpGUI.rp.isEnabled())) {

					boolean skipRest = false;
					Region r = RedProtectAPI.getRegion(player.getLocation());

					if (r == null) {
						player.sendMessage(A.b(PlayerWarpGUI.RPpermission, player.getDisplayName()));
						return true;
					}
					
					if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.bypassRP")) {
						skipRest = true;
					}
					// member check
					if (PlayerWarpGUI.useRPMembers  && !skipRest) {
						if (!r.isMember(player) && !r.isAdmin(player) && !r.isLeader(player)) {
							player.sendMessage(A.b(PlayerWarpGUI.RPpermission, player.getDisplayName()));
							return true;
						} else { 
							skipRest = true;
									}
					}

					// member check
					if (PlayerWarpGUI.useRPAdmins  && !skipRest) {
						if (!r.isAdmin(player) && !r.isLeader(player)) {
							player.sendMessage(A.b(PlayerWarpGUI.RPpermission, player.getDisplayName()));
							return true;
						} else { 
							skipRest = true;
						}
					}

					// member check
					if (PlayerWarpGUI.useRPLeaders  && !skipRest) {
						if (!r.isLeader(player)) {
							player.sendMessage(A.b(PlayerWarpGUI.RPpermission, player.getDisplayName()));
							return true;
						}
					}

				}

				// GriefPrevetion
				if ((PlayerWarpGUI.enableGriefPrevetion == true) && (PlayerWarpGUI.gp.isEnabled())) {
					me.ryanhamshire.GriefPrevention.Claim isClaim = PlayerWarpGUI.gp.dataStore
							.getClaimAt(player.getLocation(), false, null);

					if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.bypassGP")) {
						if ((isClaim == null) || !(isClaim.getOwnerName().equalsIgnoreCase(player.getName()))) {
							player.sendMessage(A.b(PlayerWarpGUI.GPpermission, player.getDisplayName()));
							return true;
						}
					}

				}

				// WorldGuard
				// !!!!!!!!!!!!!! HERE !!!!!!!!!!!!!!!!!!!!!!!
				if ((PlayerWarpGUI.enableWorldGuard == true)) {

					int count = 0;
					boolean owner = false;

					for (ProtectedRegion r : PlayerWarpGUI.wg.getRegionManager(player.getWorld())
							.getApplicableRegions(player.getLocation())) {
						count++;

						if (r.getOwners().contains(player.getUniqueId())
								|| r.getMembers().contains(player.getUniqueId())) {
							owner = true;
						}

					}
					if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.bypassWG")) {
						if ((count == 0) || (owner == false)) {
							player.sendMessage(A.b(PlayerWarpGUI.WGpermission, player.getDisplayName()));
							return false;
						}
					}
				}

				// all ok, then set a pwarp

				// create file
				if (PlayerWarpHandler.createPlayerWarpFile(player.getUniqueId())) {

					PlayerWarpHandler.createObjectFromWarpFile(
							PlayerWarpHandler.savePlayerWarpObject(player.getUniqueId(), player.getLocation()));
					player.sendMessage(A.b(PlayerWarpGUI.warpSet, player.getDisplayName()));
				}

				return true;
			}

		}

		// delete a war/pwarpp
		if ((args.length == 1) && (args[0].equalsIgnoreCase("delete"))) {
			if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.setwarp")) {
				player.sendMessage(A.b(PlayerWarpGUI.noPermission, player.getDisplayName()));
				return true;
			}

			if (!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
				player.sendMessage(A.b(PlayerWarpGUI.noWarpSet, player.getDisplayName()));
				return true;
			}

			PlayerWarpManager.removePlayerObject(player.getUniqueId());
			PlayerWarpHandler.deletePlayerWarpFile(player.getUniqueId());
			player.sendMessage(A.b(PlayerWarpGUI.deleteWarp, player.getDisplayName()));

			return true;
		}

		// Set a title
		// ------------
		if ((args.length >= 2) && (args[0].equalsIgnoreCase("title"))) {

			if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.title")) {
				player.sendMessage(A.b(PlayerWarpGUI.noPermission, player.getDisplayName()));
				return true;
			}

			if (args.length == 1) {
				player.sendMessage(A.b(PlayerWarpGUI.titleUsage, player.getDisplayName()));
				return true;
			}

			if (!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
				player.sendMessage(A.b(PlayerWarpGUI.noWarpSet, player.getDisplayName()));
				return true;
			}

			StringBuilder sb = new StringBuilder(); // Creating a new instance of StringBuilder
			for (int i = 1; i < args.length; i++) { // Basic for loop, going through the arguments starting from 1
				sb.append(args[i]); // Adds the argument into the StringBuilder
				sb.append(" "); // Adding a space into the StringBuilder
			}

			String title = sb.toString();
			// Bukkit.broadcastMessage(title);

			if ((title.length() > PlayerWarpGUI.maxTitleSize) || (title.length() == 0)) {
				player.sendMessage(A.b(PlayerWarpGUI.titleSizeError, player.getDisplayName()));
				return true;
			}

			PlayerWarpGUI.playerWarpManager.updatePlayerObjectTitle(player.getUniqueId(), title);
			player.sendMessage(A.b(PlayerWarpGUI.titleSet, player.getDisplayName()));

			return true;
		}

		// -------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------
		// Set lore1
		// ------------
		if ((args.length >= 2) && ((args[0].equalsIgnoreCase("lore1")
				|| (args[0].equalsIgnoreCase("lore2") || (args[0].equalsIgnoreCase("lore3")))))) {
			int loreNum = 0;
			if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.lore")) {
				player.sendMessage(A.b(PlayerWarpGUI.noPermission, player.getDisplayName()));
				return true;
			}

			if (args.length == 1) {
				player.sendMessage(A.b(PlayerWarpGUI.titleUsage, player.getDisplayName()));
				return true;
			}

			if (!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
				player.sendMessage(A.b(PlayerWarpGUI.noWarpSet, player.getDisplayName()));
				return true;
			}

			if (args[0].equalsIgnoreCase("lore1")) {
				loreNum = 0;
			}

			if (args[0].equalsIgnoreCase("lore2")) {
				loreNum = 1;
			}

			if (args[0].equalsIgnoreCase("lore3")) {
				loreNum = 2;
			}

			StringBuilder sb = new StringBuilder(); // Creating a new instance of StringBuilder
			for (int i = 1; i < args.length; i++) { // Basic for loop, going through the arguments starting from 1
				sb.append(args[i]); // Adds the argument into the StringBuilder
				sb.append(" "); // Adding a space into the StringBuilder
			}

			String lore1 = sb.toString();
			// Bukkit.broadcastMessage(title);

			if ((lore1.length() > PlayerWarpGUI.maxTitleSize) || (lore1.length() == 0)) {
				player.sendMessage(A.b(PlayerWarpGUI.loreSizeError, player.getDisplayName()));
				return true;
			}

			PlayerWarpGUI.playerWarpManager.updatePlayerObjectLore(player.getUniqueId(), lore1, loreNum);
			player.sendMessage(A.b(PlayerWarpGUI.loreSet, player.getDisplayName()));

			return true;
		}

		// ----------------------------------------------------------------------------------------------
		// ----------------------------------------------------------------------------------------------

		// Set a icon
		// ------------

		if ((args.length >= 1) && (args[0].equalsIgnoreCase("icon"))) {

			ItemStack newIcon = null;

			if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.icon")) {
				player.sendMessage(A.b(PlayerWarpGUI.noPermission, player.getDisplayName()));
				return true;
			}

			if (!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(player.getUniqueId())) {
				player.sendMessage(A.b(PlayerWarpGUI.noWarpSet, player.getDisplayName()));
				return true;
			}

			if (player.getItemInHand().getItemMeta() == null) {
				player.sendMessage(A.b(PlayerWarpGUI.noIconItem, player.getDisplayName()));
				return true;
			}

			newIcon = player.getItemInHand();
			int iconID = newIcon.getTypeId();
			int iconData = newIcon.getData().getData();
			String newIconString = "" + iconID + ":" + iconData;

			PlayerWarpGUI.playerWarpManager.updatePlayerObjectIcon(player.getUniqueId(), newIconString);
			player.sendMessage(A.b(PlayerWarpGUI.iconSet, player.getDisplayName()));

			return true;
		}

		// ADMIN
		// =============================================================================================
		if (args.length == 2) {

			if (!PlayerWarpGUI.perms.has(player, "playerwarpgui.setwarp.others")) {
				player.sendMessage(A.b(PlayerWarpGUI.noPermission, player.getDisplayName()));
				return true;
			}

			if (args[0].equalsIgnoreCase("delete")) {

				UUID otherUUID;
				// otherUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();

				if (Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore() == true) {
					otherUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
				} else {
					player.sendMessage(A.b(" &7Player &6 [username] &7not found.", args[1]));
					return true;
				}

				if (!PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(otherUUID)) {
					player.sendMessage(A.b(" &7Player &b[username] &7does not have a &6/pwarp &7set.",
							Bukkit.getOfflinePlayer(otherUUID).getName()));
					return true;
				}

				PlayerWarpManager.removePlayerObject(otherUUID);
				PlayerWarpHandler.deletePlayerWarpFile(otherUUID);
				player.sendMessage(A.b(" &7Player &b[username] &6/pwarp &7has been deleted.",
						Bukkit.getOfflinePlayer(otherUUID).getName()));

				return true;

			}

			if (args[0].equalsIgnoreCase("set")) {

				UUID otherUUID;

				if (Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore() == true) {
					otherUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
				} else {
					player.sendMessage(A.b(" &7Player &6 [username] &7not found.", args[1]));
					return true;
				}

				// check if already has a pwarp
				if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(otherUUID)) {
					player.sendMessage(A.b(
							" &7Player &b[username] &7already has a &6/pwarp &7set, you must delete it before etting a new &6/pwarp",
							Bukkit.getOfflinePlayer(otherUUID).getName()));
					return true;
				}

				// do safeWarp checking
				if (PlayerWarpGUI.useSafeWarp) {

					// check for beathable air blocks
					if (!PlayerWarpManager.isSafeLocation(player.getLocation())) {
						player.sendMessage(
								A.b(" &7Cannot set a &6/pwarp &7in this unsafe location", player.getDisplayName()));
						return true;
					}
				}

				// check disabled worlds
				String world = player.getWorld().getName().toString();
				for (int i = 0; i < PlayerWarpGUI.disabledWorlds.size(); i++) {
					// Bukkit.broadcastMessage("pworld: " + world + " dworld: " +
					// PlayerWarpGUI.disabledWorlds.get(i));
					if (PlayerWarpGUI.disabledWorlds.get(i).equalsIgnoreCase(world)) {
						player.sendMessage(A.b(" &6/pwarp &7cannot be set in this world", player.getDisplayName()));
						return true;
					}
				}

				// GriefPrevetion
				if ((PlayerWarpGUI.enableGriefPrevetion == true) && (PlayerWarpGUI.gp.isEnabled())) {
					me.ryanhamshire.GriefPrevention.Claim isClaim = PlayerWarpGUI.gp.dataStore
							.getClaimAt(player.getLocation(), false, null);
					if ((isClaim == null) || !(isClaim.getOwnerName()
							.equalsIgnoreCase(Bukkit.getOfflinePlayer(otherUUID).getName()))) {
						player.sendMessage(A.b("&7You can only set warps inside the players own claim",
								Bukkit.getOfflinePlayer(otherUUID).getName()));
						return true;

					}

				}

				// WorldGuard
				// !!!!!!!!!!!!!! HERE !!!!!!!!!!!!!!!!!!!!!!!
				if ((PlayerWarpGUI.enableWorldGuard == true)) {

					int count = 0;
					boolean owner = false;

					for (ProtectedRegion r : PlayerWarpGUI.wg.getRegionManager(player.getWorld())
							.getApplicableRegions(player.getLocation())) {
						count++;

						if (r.getOwners().contains(otherUUID) || r.getMembers().contains(otherUUID)) {
							owner = true;
						}

					}

					if ((count == 0) || (owner == false)) {
						player.sendMessage(A.b(
								"&7Player &b[username]&7 must be a owner or member of the region to set a &6/pwarp here.",
								player.getDisplayName()));
						return false;
					}
				}

				// all ok, then set a pwarp

				// create file
				if (PlayerWarpHandler.createPlayerWarpFile(otherUUID)) {

					PlayerWarpHandler.createObjectFromWarpFile(
							PlayerWarpHandler.savePlayerWarpObject(otherUUID, player.getLocation()));
					player.sendMessage(A.b(" &6/pwarp &7has been set for: &6[username]",
							Bukkit.getOfflinePlayer(otherUUID).getName()));
				}

				return true;

			}

		}

		player.sendMessage(A.b(PlayerWarpGUI.unknownCommand, player.getDisplayName()));
		return false;
	}

}
