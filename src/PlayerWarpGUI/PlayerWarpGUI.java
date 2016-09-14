package PlayerWarpGUI;

import java.io.File;
import java.util.List;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import FileHandlers.ConfigHandler;
import FileHandlers.PlayerWarpHandler;
import Listeners.ChestListener;
import Listeners.CommandListener;
import Managers.PlayerWarpManager;
import Objects.chestObject;
import Utils.NameFetcher;

public class PlayerWarpGUI extends JavaPlugin {

	public static PlayerWarpGUI instance;
	private PlayerWarpGUI plugin;

	public static Economy econ = null;
	public static Permission perms = null;

	public String configFile = this.getDataFolder() + File.separator + "config.yml"; // main config file location
	public String warpsFolder = this.getDataFolder() + File.separator + "playerWarps"; // folder that holds the playerwarp folders
	public String defaultConfigFile = "defaultConfig.yml";
	public String defaultWarpConfigFile = "defaultWarpConfig.yml";

	//
	public static boolean useSafeWarp; // wether to check for safe warps when setting and teleporting to
	public static List<String> unsafeBlocks; // list of unsafe blocks to land on
	public static int cooldown; // cooldown for teleport in seconds
	public static boolean cancelOnMovement; // cancel teleport if player moves
	public static int chestSize; // size o fthe chest to open
	public static String chestText; // text on the chest that opens

	public static String defaultWarpIcon = "35:9"; // defaul icon that will show in the GUI for player warps
	public static String nextPageIcon = "35:8"; // defaul icon that will show in the GUI for player warps
	public static String playerWarpText = "&6[username]"; // text that displays on icon
	public static String messagePrefix = "[PlayerWarpGUI]"; // prefic in front of all messages sent from this plugin
	
	public static int setWarpCost = 0;	//cost of setting a warp

	public ConfigHandler configHandler;
	public static PlayerWarpHandler playerWarpHandler;
	public static PlayerWarpManager playerWarpManager;
	public static chestObject chestObject;
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
		PlayerWarpGUI.playerWarpHandler = new PlayerWarpHandler(this);
		PlayerWarpGUI.playerWarpManager = new PlayerWarpManager(this);
		PlayerWarpGUI.chestObject = new chestObject(this);

		// listeners
		this.getCommand("playerwarps").setExecutor(new CommandListener());
		Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(), this);

		// load config file data
		configHandler.loadConfigFile();
		// load all warp objects
		playerWarpHandler.loadAllWarpObjects();

		setupEconomy();
		setupPermissions();

	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
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
