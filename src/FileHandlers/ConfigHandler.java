package FileHandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;

public class ConfigHandler {

	public static PlayerWarpGUI plugin;

	// -------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------
	public ConfigHandler(PlayerWarpGUI playerWarpGUI) {
		plugin = playerWarpGUI;
	}

	// -----------------------------------------------------
	// loadConfigFile
	// -----------------------------------------------------
	public void loadConfigFile() {

		// check if config file exsists else make it
		if (!checkConfigFile()) {
			createConfigFile();
		}

		// load the configs
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(plugin.configFile);

			// load useSafeWarp
			PlayerWarpGUI.useSafeWarp = config.getBoolean("SafeWarp.enabled", false);
			plugin.getLogger().info("Setting useSafeWarp to: " + String.valueOf(PlayerWarpGUI.useSafeWarp));

			// load unsafeBlocks
			PlayerWarpGUI.unsafeBlocks = config.getStringList("SafeWarp.unsafeBlocks");
			plugin.getLogger().info("unSafeBlocks: ");

			// print out unsafeBlocks
			int theTotalNumberOfElements = PlayerWarpGUI.unsafeBlocks.size();
			for (int counter = 0; counter < theTotalNumberOfElements; counter++) {
				plugin.getLogger().info("    " + PlayerWarpGUI.unsafeBlocks.get(counter));
			}

			// load teleport cooldown
			PlayerWarpGUI.cooldown = config.getInt("teleport.cooldown", 3);
			plugin.getLogger().info("Setting teleport cooldown to: " + PlayerWarpGUI.cooldown + " seconds");
			
			// load cancelOnMovement
			PlayerWarpGUI.cancelOnMovement = config.getBoolean("teleport.cancelOnMovement", true);
			plugin.getLogger().info("Setting cencelOnMovement to: " + PlayerWarpGUI.cancelOnMovement);
			
			// load defaultWarpIcon
			PlayerWarpGUI.defaultWarpIcon = config.getString("DefaultWarpIcon", "35:9");
			plugin.getLogger().info("Setting defaultWarpIcon to: " + PlayerWarpGUI.defaultWarpIcon);

			// load messagePrefix
			PlayerWarpGUI.messagePrefix = config.getString("Messages.prefix", "[PlayerWarpGUI]");
			plugin.getLogger().info("Setting messagePrefix to: " + PlayerWarpGUI.messagePrefix);

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
