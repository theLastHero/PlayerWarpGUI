package PlayerWarpGUI;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import FileHandlers.ConfigHandler;
import FileHandlers.MessageHandler;
import FileHandlers.PlayerWarpHandler;
import Listeners.ChestListener;
import Listeners.CommandListener;
import Managers.Metrics;
import Managers.PlayerWarpManager;
import Objects.chestObject;
import Utils.A;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class PlayerWarpGUI extends JavaPlugin {

	public static PlayerWarpGUI instance;
	private PlayerWarpGUI plugin;

	public static Economy econ = null;
	public static Permission perms = null;

	public String configFile = this.getDataFolder() + File.separator + "config.yml"; // main config file location
	public String messageFile = this.getDataFolder() + File.separator + "messages.yml"; // messages file location
	public String warpsFolder = this.getDataFolder() + File.separator + "playerWarps"; // folder that holds the playerwarp folders
	public String defaultConfigFile = "defaultConfig.yml";
	public String defaultWarpConfigFile = "defaultWarpConfig.yml";
	public String defaultMessagesFile = "defaultMessagesFile.yml";

	public static boolean DEBUG_MODE = true; // prints all sorts of inf to console

	public static boolean useSafeWarp; // wether to check for safe warps when setting and teleporting to
	public static List<String> unsafeBlocks; // list of unsafe blocks to land on
	public static int cooldown; // cooldown for teleport in seconds
	public static boolean cancelOnMovement; // cancel teleport if player moves
	public static int chestSize; // size o fthe chest to open
	public static String chestText; // text on the chest that opens
	public static int maxTitleSize = 25;

	public static String defaultWarpIcon = "35:9"; // defaul icon that will show in the GUI for player warps
	public static String nextPageIcon = "35:8"; // defaul icon that will show in the GUI for player warps
	public static String playerWarpText = "&6[username]"; // text that displays on icon
	public static String messagePrefix = "[PlayerWarpGUI]"; // prefic in front of all messages sent from this plugin

	public static boolean enableGriefPrevetion = false; // use grief preveyion
	public static boolean enableWorldGuard = false; // use worldguard
	public static boolean useOwners = true; // owners can set warps
	public static boolean useMembers = true; // members can set warps
	public static boolean useMetrics = true; // members can set warps

	public static int setWarpCost = 0; // cost of setting a warp
	public static List<String> disabledWorlds; // list of unsafe blocks to land on

	public ConfigHandler configHandler;
	public MessageHandler messageHandler;
	public static PlayerWarpHandler playerWarpHandler;
	public static PlayerWarpManager playerWarpManager;
	public static chestObject chestObject;

	public static GriefPrevention gp;
	public static WorldGuardPlugin wg;

	public static String noPermission;
	
	public static String withdrawn;
	public static String alreadyHaveWarpSet;
	public static String notEnoughMoney;
	public static String setInUnsafeLocation;
	public static String setInDisabledWorld;
	public static String GPpermission;
	public static String WGpermission;
	public static String warpSet;
	public static String deleteWarp;
	public static String titleUsage;
	public static String noWarpSet;
	public static String titleSizeError;
	public static String titleSet;
	public static String noIconItem;
	public static String iconSet;

	public static A a;

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
		this.messageHandler = new MessageHandler(this);
		PlayerWarpGUI.playerWarpHandler = new PlayerWarpHandler(this);
		PlayerWarpGUI.playerWarpManager = new PlayerWarpManager(this);
		PlayerWarpGUI.chestObject = new chestObject(this);
		PlayerWarpGUI.a = new A(plugin);

		// listeners
		this.getCommand("playerwarps").setExecutor(new CommandListener());
		Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(plugin), this);

		
		

		// load config file data
		configHandler.loadConfigFile();
		
		/*
		getConfig().addDefault("metrics.enabled", true);
		getConfig().options().copyDefaults(true);
		saveConfig();
		*/
		
		// load messages file data
		messageHandler.loadMessageFile();
		// load all warp objects
		playerWarpHandler.loadAllWarpObjects();

		setupEconomy();
		setupPermissions();

		//
		if (enableGriefPrevetion == true) {
			gp = GriefPrevention.instance;
		}

		if (enableWorldGuard == true) {
			wg = WGBukkit.getPlugin();
		}

		//metrics
		if (useMetrics == true) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				// Failed to submit the stats :-(
			}
		}
		
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
