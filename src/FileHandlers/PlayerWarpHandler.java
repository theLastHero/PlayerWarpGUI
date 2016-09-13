package FileHandlers;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import Managers.PlayerWarpManager;
import Objects.PlayerWarpObject;
import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerWarpHandler {

	public static PlayerWarpGUI plugin;

	// -------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------
	public PlayerWarpHandler(PlayerWarpGUI playerWarpGUI) {
		plugin = playerWarpGUI;
	}
	
	// -----------------------------------------------------
	// getPlayer
	// -----------------------------------------------------
	public static  ArrayList<PlayerWarpObject> getPlayerWarpObjects() {
		return PlayerWarpObject.playerWarpObjects;
	}

	// -----------------------------------------------------
	// createObjectFromWarpFile
	// -----------------------------------------------------
	public boolean createObjectFromWarpFile(File file) {

		//String filename = file.getName().replace(".yml", "");
		UUID playerUUID = UUID.fromString(file.getName().replace(".yml", ""));
		String playerName = null;
		String warpLocation = null;

		// check if object already exsits
		if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(playerUUID)) {
			return false;
		}

		// load the configs
		FileConfiguration config = new YamlConfiguration();
		try {

			config.load(file);
			plugin.getLogger().info("LOADED PLAYERWARP FILE: " + playerUUID.toString());

			// set object userName
			playerName = Bukkit.getServer().getOfflinePlayer(playerUUID).getName();
			plugin.getLogger().info("   playerName: " + playerName);

			// set warpLocation
			warpLocation = config.getString("warpDetails.Location");
			plugin.getLogger().info("   warpLocation" + warpLocation);

		} catch (Exception e) {
			e.printStackTrace();
		}

		//cretae actual object
		PlayerWarpManager.getPlayerWarpManager().createWarpObjects(playerUUID, warpLocation);
		
		return true;
	}

	// -----------------------------------------------------
	// loadAllWarpObjects
	// -----------------------------------------------------
	public void loadAllWarpObjects() {
		// check if for warp folder, else create it
		if (!PlayerWarpGUI.playerWarpHandler.checkWarpFolder()) {
			PlayerWarpGUI.playerWarpHandler.createWarpFolder();
		}

		File warpsFolder = new File(plugin.warpsFolder);
		String filename = null;

		if (!(warpsFolder.listFiles() == null)) {
			for (File file : warpsFolder.listFiles()) {

				plugin.getLogger().info(filename);
				createObjectFromWarpFile(file);

			}
		}
	}



	// -----------------------------------------------------
	// createConfigFile
	// -----------------------------------------------------
	public void createWarpFolder() {
		File playerWarpFolder = new File(plugin.warpsFolder);

		playerWarpFolder.mkdirs();

	}

	// -----------------------------------------------------
	// checkConfigFile
	// -----------------------------------------------------
	public boolean checkWarpFolder() {
		File playerWarpFolder = new File(plugin.warpsFolder);

		// Check it exsists
		if (!playerWarpFolder.exists()) {
			return false;
		}

		return true;
	}




}
