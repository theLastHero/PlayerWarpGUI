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
	@SuppressWarnings("unused")
	public boolean createObjectFromWarpFile(File file) {

		UUID playerUUID = null;
		String playerName = null;
		String warpLocation = null;
		String uuid = null;

		//check if file name is a validate UUID
		try{
			playerUUID = UUID.fromString(file.getName().replace(".yml", ""));
		} catch (IllegalArgumentException exception){
			return true;
		}
		
		// check if object already exsits
		if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(playerUUID)) {
			return true;
		}

		// load the configs
		FileConfiguration config = new YamlConfiguration();
		try {

			config.load(file);
			//plugin.getLogger().info("LOADED PLAYERWARP FILE: " + playerUUID.toString());

			// set object userName
			playerName = Bukkit.getServer().getOfflinePlayer(playerUUID).getName();
			//plugin.getLogger().info("   playerName: " + playerName);

			// set warpLocation
			warpLocation = config.getString("warpDetails.Location");
			//plugin.getLogger().info("   warpLocation" + warpLocation);

			// set warpLocation
			uuid = config.getString("playerData.UUID");
			//plugin.getLogger().info("   uuid: " + uuid);

			
		} catch (Exception e) {
			e.printStackTrace();
			return true;
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

		if (!(warpsFolder.listFiles() == null)) {
			for (File file : warpsFolder.listFiles()) {

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
