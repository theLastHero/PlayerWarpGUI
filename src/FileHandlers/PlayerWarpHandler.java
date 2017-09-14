package FileHandlers;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import Managers.PlayerWarpManager;
import Objects.PlayerWarpObject;
import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;

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
	public static ArrayList<PlayerWarpObject> getPlayerWarpObjects() {
		return PlayerWarpObject.playerWarpObjects;
	}

	public static boolean createPlayerWarpFile(UUID uuid) {

		File playerDataFile = new File(PlayerWarpGUI.instance.warpsFolder + File.separator + uuid.toString() + ".yml");

		playerDataFile.getParentFile().mkdirs();
		ConfigHandler.copy(plugin.getResource("defaultWarpConfig.yml"), playerDataFile);

		return true;
	}

	public static boolean deletePlayerWarpFile(UUID uuid) {

		File playerDataFile = new File(PlayerWarpGUI.instance.warpsFolder + File.separator + uuid.toString() + ".yml");
		playerDataFile.delete();
		// playerDataFile.getParentFile().mkdirs();
		// ConfigHandler.copy(plugin.getResource("defaultWarpConfig.yml"),
		// playerDataFile);

		return true;
	}

	// ------------------------------------------------------
	// savePlayerWarpObject
	// ------------------------------------------------------
	public static File savePlayerWarpObject(UUID uuid, Location location) {

		File playerDataFile = new File(PlayerWarpGUI.instance.warpsFolder + File.separator + uuid.toString() + ".yml");

		// save to file
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(playerDataFile);

			// set warpLocation
			config.set("warpDetails.location", PlayerWarpManager.getPlayerWarpManager().loc2str(location));

			// set UUID
			config.set("playerData.UUID", uuid.toString());

			config.set("warpDetails.title", "");

			config.save(playerDataFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return playerDataFile;
	}

	// ------------------------------------------------------
	// updateIcon
	// ------------------------------------------------------
	public static File updateIcon(UUID uuid, String icon) {

		File playerDataFile = new File(PlayerWarpGUI.instance.warpsFolder + File.separator + uuid.toString() + ".yml");

		// save to file
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(playerDataFile);

			config.set("warpDetails.icon", icon);

			config.save(playerDataFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return playerDataFile;
	}

	// ------------------------------------------------------
	// updateTitle
	// ------------------------------------------------------
	public static File updateTitle(UUID uuid, String title) {

		File playerDataFile = new File(PlayerWarpGUI.instance.warpsFolder + File.separator + uuid.toString() + ".yml");

		// save to file
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(playerDataFile);

			config.set("warpDetails.title", title);

			config.save(playerDataFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return playerDataFile;
	}
	
	// ------------------------------------------------------
	// updateTitle
	// ------------------------------------------------------
	public static File updatelore(UUID uuid, ArrayList<String> lore) {

		File playerDataFile = new File(PlayerWarpGUI.instance.warpsFolder + File.separator + uuid.toString() + ".yml");

		// save to file
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(playerDataFile);

			config.set("warpDetails.lore", lore);

			config.save(playerDataFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return playerDataFile;
	}

	// -----------------------------------------------------
	// createObjectFromWarpFile
	// -----------------------------------------------------
	@SuppressWarnings({ "unused", "unchecked" })
	public static boolean createObjectFromWarpFile(File file) {

		UUID playerUUID = null;
		String playerName = null;
		String warpLocation = null;
		String uuid = null;
		String icon = null;
		String title = null;
		ArrayList<String> loreList;
		boolean enbaled = true;

		// check if file name is a validate UUID
		try {
			playerUUID = UUID.fromString(file.getName().replace(".yml", ""));
		} catch (IllegalArgumentException exception) {
			return true;
		}

		if (!Bukkit.getOfflinePlayer(playerUUID).hasPlayedBefore()) {
			A.d("Cannot load PlayerWarp file: Player not found");
			return true;
		}

		// check if object already exsits
		if (PlayerWarpManager.getPlayerWarpManager().checkPlayerWarpObject(playerUUID)) {
			A.d("Cannot load PlayerWarp file: Warp object already exsists");
			return true;
		}

		// load the configs
		FileConfiguration config = new YamlConfiguration();
		try {

			config.load(file);
			A.d("LOADED PLAYERWARP FILE: " + playerUUID.toString());

			// set object userName
			playerName = Bukkit.getServer().getOfflinePlayer(playerUUID).getName();
			A.d("   playerName: " + playerName);

			// set warpLocation
			warpLocation = config.getString("warpDetails.location");
			A.d("   warpLocation: " + warpLocation);

			// set icon
			icon = config.getString("warpDetails.icon");
			A.d("   icon: " + icon);

			// set warpLocation
			title = config.getString("warpDetails.title");
			A.d("   title: " + title);

			// set warpLocation
			uuid = config.getString("playerData.UUID");
			A.d("   uuid: " + uuid);

			// set warpLocation
			loreList = (ArrayList<String>) config.getList("warpDetails.lore");
			
			int l=0;
			for(String x : loreList) {
			  A.d("   lore: " + x);
			  l = l+1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

		// cretae actual object
		PlayerWarpManager.getPlayerWarpManager().createWarpObjects(playerUUID, warpLocation, title, icon, loreList);

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
