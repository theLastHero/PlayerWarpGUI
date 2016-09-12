package PlayerWarpGUI;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import FileHandlers.ConfigHandler;
import FileHandlers.PlayerWarpHandler;
import Utils.NameFetcher;

public class PlayerWarpGUI extends JavaPlugin {

	public static PlayerWarpGUI instance;
	private PlayerWarpGUI plugin;
	public String configFile = this.getDataFolder() + File.separator + "config.yml";
	public String warpsFolder = this.getDataFolder() + File.separator + "playerWarps";
	public String defaultConfigFile = "defaultConfig.yml";
	public String defaultWarpConfigFile = "defaultWarpConfig.yml";

	//
	public static boolean useSafeWarp; // wether to check for safe warps when setting and teleporting to
	public static List<String> unsafeBlocks; // list of unsafe blocks to land on
	public static int cooldown; // cooldown for teleport in seconds
	public static boolean cancelOnMovement;

	public static String defaultWarpIcon = "35:9"; // defaul icon that will show in the GUI for player warps
	public static String messagePrefix = "[PlayerWarpGUI]"; // prefic in front of all messages sent from this plugin

	public ConfigHandler configHandler;
	public static PlayerWarpHandler playerWarpHandler;
	public static NameFetcher nameFetcher;

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {

		instance = this;
		this.configHandler = new ConfigHandler(this);
		this.playerWarpHandler = new PlayerWarpHandler(this);

		configHandler.loadConfigFile();
		playerWarpHandler.loadAllWarpObjects();
		
		// load warp files into objects
		/*
		 * + check if folder exsists, if not make it
		 * grab all files in folder that match a uuid name and .yml
		 * load each file as config and create object for that file
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */

		

	

	}

	// -------------------------------------------------------------------------------------
	// getInstance
	// -------------------------------------------------------------------------------------
	public static Plugin getInstance() {
		return instance;
	}

	/**
	 * @return the plugin
	 */
	public PlayerWarpGUI getPlugin() {
		return plugin;
	}

	/**
	 * @param plugin
	 *            the plugin to set
	 */
	public void setPlugin(PlayerWarpGUI plugin) {
		this.plugin = plugin;
	}

}
