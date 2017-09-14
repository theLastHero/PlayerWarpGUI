package FileHandlers;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;

public class MessageHandler {

	public static PlayerWarpGUI plugin;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public MessageHandler(PlayerWarpGUI playerWarpGUI) {
		plugin = playerWarpGUI;
	}

	public void loadMessageFile() {

		// check if file exsists
		if (!checkMessageFile()) {
			createMessageFile();
		}

		// load the configs
		FileConfiguration message = new YamlConfiguration();
		try {
			message.load(plugin.messageFile);

			// load useSafeWarp
			PlayerWarpGUI.noPermission = message.getString("messages.permissions.noPermission", " &7You do not have permission to do that command");

			PlayerWarpGUI.notEnoughMoney = message.getString("messages.errors.notEnoughMoney", " &7You do not have enough money to perform to: &6/pwarp set.");
			PlayerWarpGUI.alreadyHaveWarpSet = message.getString("messages.errors.alreadyHaveWarpSet", " &7You already have a &6/pwarp&7, you must delete it before setting a new &6/pwarp");
			PlayerWarpGUI.setInUnsafeLocation = message.getString("messages.errors.setInUnsafeLocation", " &7You cannot set a &6/pwarp &7in this unsafe location");
			PlayerWarpGUI.setInDisabledWorld = message.getString("messages.errors.setInDisabledWorld", " &6/pwarp &7cannot be set in this world");
			PlayerWarpGUI.GPpermission = message.getString("messages.errors.GPpermission", "&7You can only set warps inside your own claim");
			PlayerWarpGUI.WGpermission = message.getString("messages.errors.WGpermission", "&7You must be a owner or member of the region to set a &6/pwarp &7here.");
			
			PlayerWarpGUI.withdrawn = message.getString("messages.others.withdrawn", "&6[cost] &7has been withdrawn from your account");
			PlayerWarpGUI.warpSet = message.getString("messages.others.warpset", " &6/pwarp &7has been set for: &6[username]");
			PlayerWarpGUI.deleteWarp = message.getString("messages.others.deleteWarp", " &7Your &6/pwarp &7has been deleted.");
			PlayerWarpGUI.titleUsage = message.getString("messages.titles.titleUsage", " &7Usage: &6/pwarps title &7Your title text.");
			PlayerWarpGUI.noWarpSet = message.getString("messages.others.noWarpSet", " &7You do not have a &6/pwarp.");
			PlayerWarpGUI.titleSizeError = message.getString("messages.titles.titleSizeError", " &7You do not have a &6/pwarp.");
			PlayerWarpGUI.titleSet = message.getString("messages.titles.titleSet", " &7Your title has been set.");
			PlayerWarpGUI.titleUsage = message.getString("messages.titles.titleUsage", " &7Usage: &6/pwarps title &7Your title text.");
			PlayerWarpGUI.iconSet = message.getString("messages.icons.iconSet", " &7Your icon has been changed.");
			PlayerWarpGUI.noIconItem = message.getString("messages.icons.noIconItem", " &7You must be holding the item in your hand to use the  &6/pwarps icon &7command.");
			PlayerWarpGUI.loreSizeError = message.getString("messages.lore.loreSizeError", " &7Your lore text is too long");
			PlayerWarpGUI.loreSet = message.getString("messages.lore.loreSet", " &7Your lore text has been set");
			PlayerWarpGUI.loreUsage = message.getString("messages.lore.loreUsage", " &7Usage: &6/pwarps lore1,lore2 or lore3 &7Your lore text.");
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------
	// checkMessageFile
	// -----------------------------------------------------
	public boolean checkMessageFile() {
		File messageFile = new File(plugin.messageFile);

		// Check it exsists
		if (!messageFile.exists()) {
			return false;
		}

		return true;
	}

	// -----------------------------------------------------
	// createConfigFile
	// -----------------------------------------------------
	public void createMessageFile() {
		File messageFile = new File(plugin.messageFile);

		messageFile.getParentFile().mkdirs();
		A.copy(plugin.getResource(plugin.defaultMessagesFile), messageFile);

	}

}
