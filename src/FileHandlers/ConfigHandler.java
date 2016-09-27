package FileHandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;

public class ConfigHandler {

	public static PlayerWarpGUI plugin;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public ConfigHandler(PlayerWarpGUI playerWarpGUI) {
		plugin = playerWarpGUI;
	}

	// +-----------------------------------------------------
	// | loadConfigFile
	// +-----------------------------------------------------
	public void loadConfigFile() {

		// check if config file exsists else make it
		if (!checkConfigFile()) {
			createConfigFile();
		}

		// load the configs
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(plugin.configFile);

			if (config.getString("Metrics.enabled") == null) {

				File saveTo = new File(plugin.configFile);
				FileWriter fw = new FileWriter(saveTo, true);
				PrintWriter pw = new PrintWriter(fw);

				pw.println("Metrics:");
				pw.println("  enabled: true");
				pw.flush();
				pw.close();

			}
			// load useSafeWarp
			PlayerWarpGUI.useSafeWarp = config.getBoolean("SafeWarp.enabled", false);
			// A.d("Setting useSafeWarp to: " + String.valueOf(PlayerWarpGUI.useSafeWarp));

			// load unsafeBlocks
			PlayerWarpGUI.unsafeBlocks = config.getStringList("SafeWarp.unsafeBlocks");
			// A.d("unSafeBlocks: ");

			// print out unsafeBlocks
			int theTotalNumberOfElements = PlayerWarpGUI.unsafeBlocks.size();
			for (int counter = 0; counter < theTotalNumberOfElements; counter++) {
				// A.d("    " + PlayerWarpGUI.unsafeBlocks.get(counter));
			}

			// load teleport cooldown
			PlayerWarpGUI.cooldown = config.getInt("Teleport.cooldown", 3);
			// A.d("Setting teleport cooldown to: " + PlayerWarpGUI.cooldown + " seconds");

			// load cancelOnMovement
			PlayerWarpGUI.cancelOnMovement = config.getBoolean("Teleport.cancelOnMovement", true);
			// A.d("Setting cencelOnMovement to: " + PlayerWarpGUI.cancelOnMovement);

			// load defaultWarpIcon
			PlayerWarpGUI.defaultWarpIcon = config.getString("GUI.DefaultWarpIcon", "35:9");
			// A.d("Setting defaultWarpIcon to: " + PlayerWarpGUI.defaultWarpIcon);

			// load nextPageIcon
			PlayerWarpGUI.nextPageIcon = config.getString("GUI.nextPageIcon", "35:8");
			// A.d("Setting nextPageIcon to: " + PlayerWarpGUI.nextPageIcon);

			// load messagePrefix
			PlayerWarpGUI.messagePrefix = config.getString("Messages.prefix", "[PlayerWarpGUI]");
			// A.d("Setting messagePrefix to: " + PlayerWarpGUI.messagePrefix);

			// load chest size
			PlayerWarpGUI.chestSize = (config.getInt("GUI.rows") * 9);
			// A.d("Setting chestSize: " + PlayerWarpGUI.chestSize);

			// load chest size
			PlayerWarpGUI.chestText = config.getString("GUI.chestText", "PlayerWarpGUI");
			// A.d("Setting chestText to: " + PlayerWarpGUI.chestText);

			// load player warp text
			PlayerWarpGUI.playerWarpText = config.getString("GUI.playerWarpText", "&6[username]");
			// A.d("Setting playerWarpText to: " + PlayerWarpGUI.playerWarpText);

			// load setWarpCost
			PlayerWarpGUI.setWarpCost = config.getInt("Settings.setWarpCost", 0);
			// A.d("Setting setWarpCost to: " + Integer.toString(PlayerWarpGUI.setWarpCost));

			// load disabledWorlds
			PlayerWarpGUI.disabledWorlds = config.getStringList("Settings.disabledWorlds");
			// A.d("Setting setWarpCost to: " + Integer.toString(PlayerWarpGUI.setWarpCost));

			// load GriefPrevetion
			PlayerWarpGUI.enableGriefPrevetion = config.getBoolean("GriefPrevetion.enabled", false);
			// A.d("Setting enableGriefPrevetion to: " + PlayerWarpGUI.enableGriefPrevetion);

			// load enableWorldGuard
			PlayerWarpGUI.enableWorldGuard = config.getBoolean("WorldGuard.enabled", false);
			// A.d("Setting enableWorldGuardto: " + PlayerWarpGUI.enableWorldGuard);

			// load enableWorldGuard
			PlayerWarpGUI.useOwners = config.getBoolean("WorldGuard.enabled", false);
			// A.d("Setting owners to: " + PlayerWarpGUI.useOwners);

			// load enableWorldGuard
			PlayerWarpGUI.useMembers = config.getBoolean("WorldGuard.enabled", false);
			// A.d("Setting members to: " + PlayerWarpGUI.useMembers);

			// load debug_mode
			PlayerWarpGUI.DEBUG_MODE = config.getBoolean("Settings.debug_mode", false);

			PlayerWarpGUI.useMetrics = config.getBoolean("Metrics.enabled", false);
			// A.d("Setting debug_mode to: " + PlayerWarpGUI.DEBUG_MODE);

			// load maxTitleSize
			PlayerWarpGUI.maxTitleSize = config.getInt("Settings.maxTitleSize", 25);
			// A.d("Setting maxTitleSize to: " + PlayerWarpGUI.maxTitleSize);

			// PlayerWarpGUI.getInstance().saveConfig();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// -----------------------------------------------------
	// createConfigFile
	// -----------------------------------------------------
	public void createConfigFile() {
		File configFile = new File(plugin.configFile);

		configFile.getParentFile().mkdirs();
		copy(plugin.getResource(plugin.defaultConfigFile), configFile);

	}

	// -----------------------------------------------------
	// checkConfigFile
	// -----------------------------------------------------
	public boolean checkConfigFile() {
		File playerWarpDataFile = new File(plugin.configFile);

		// Check it exsists
		if (!playerWarpDataFile.exists()) {
			return false;
		}

		return true;
	}

	// -------------------------------------------------------------------------------------
	// copy
	// -------------------------------------------------------------------------------------
	public static void copy(InputStream in, File file) {

		try {

			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {

				out.write(buf, 0, len);

			}
			out.close();
			in.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
