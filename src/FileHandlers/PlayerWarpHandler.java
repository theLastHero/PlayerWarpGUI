package FileHandlers;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;
import Utils.NameFetcher;

public class PlayerWarpHandler {

	public static PlayerWarpGUI plugin;

	// -------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------
	public PlayerWarpHandler(PlayerWarpGUI playerWarpGUI) {
		plugin = playerWarpGUI;
	}

	// -----------------------------------------------------
	// loadWarpYMLFile
	// -----------------------------------------------------
	public void loadWarpYMLFile(File file) {

		String filename = file.getName().replace(".yml", "");
		UUID playerUUID = UUID.fromString(filename);
		String playerName = null;
		String warpLocation = null;
		
		// load the configs
		FileConfiguration config = new YamlConfiguration();
		try {

			config.load(file);
			plugin.getLogger().info("LOADED PLAYERWARP FILE: " + filename);

			// set object userName
			playerName = Bukkit.getServer().getOfflinePlayer(playerUUID).getName();
			plugin.getLogger().info("   playerName: " + playerName);

			// set warpLocation
			warpLocation = config.getString("warpDetials.Location");
			plugin.getLogger().info("   warpLocation" + warpLocation);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadAllWarpObjects() {
		// check if for warp folder, else create it
		if (!PlayerWarpGUI.playerWarpHandler.checkWarpFolder()) {
			PlayerWarpGUI.playerWarpHandler.createWarpFolder();
		}

		File warpsFolder = new File(plugin.warpsFolder);
		// ArrayList<UUID> returnArray = new ArrayList<UUID>();
		@SuppressWarnings("unused")
		String filename = null;

		if (!(warpsFolder.listFiles() == null)) {
			for (File file : warpsFolder.listFiles()) {

				plugin.getLogger().info(filename);
				loadWarpYMLFile(file);

			}
		}
	}

	public void createWarpObjects() {

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
