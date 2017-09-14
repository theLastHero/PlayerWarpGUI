package Managers;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import FileHandlers.PlayerWarpHandler;
import Objects.PlayerWarpObject;
import Objects.chestObject;
import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerWarpManager {

	static PlayerWarpManager plugin;

	public PlayerWarpManager(PlayerWarpManager playerWarpManager) {
		plugin = playerWarpManager;
	}

	public PlayerWarpManager(PlayerWarpGUI playerWarpGUI) {
		// TODO Auto-generated constructor stub
	}

	private static PlayerWarpManager playerWarpManager = new PlayerWarpManager(plugin);

	/**
	 * Checks if a location is safe (solid ground with 2 breathable blocks)
	 *
	 * @param location
	 *            Location to check
	 * @return True if location is safe
	 */
	public static boolean isSafeLocation(Location location) {
		Block feet = location.getBlock();
		if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
			return false; // not transparent (will suffocate)
		}
		Block head = feet.getRelative(BlockFace.UP);
		if (!head.getType().isTransparent()) {
			return false; // not transparent (will suffocate)
		}
		Block ground = feet.getRelative(BlockFace.DOWN);
		if (!ground.getType().isSolid()) {
			return false; // not solid
		}

		// here check blocks to land on
		for (int i = 0; i < PlayerWarpGUI.unsafeBlocks.size(); i++) {
			Block blockLandsOn = feet;
			if (blockLandsOn.getType().equals(chestObject.parseString(PlayerWarpGUI.unsafeBlocks.get(i)))) {
				return false; // not solid
			}
		}

		// here check blocks to land on
		for (int i = 0; i < PlayerWarpGUI.unsafeBlocks.size(); i++) {

			if (feet.getLocation().add(0, -1, 0).getBlock().getRelative(BlockFace.UP).getType().equals(chestObject.parseString(PlayerWarpGUI.unsafeBlocks.get(i)))) {
				return false; // not solid
			}
		}

		// here check blocks to land on
		for (int i = 0; i < PlayerWarpGUI.unsafeBlocks.size(); i++) {
			Block blockLandsOn = feet.getRelative(BlockFace.DOWN);
			if (blockLandsOn.getType().equals(chestObject.parseString(PlayerWarpGUI.unsafeBlocks.get(i)))) {
				return false; // not solid
			}
		}

		// here check blocks to land on
		for (int i = 0; i < PlayerWarpGUI.unsafeBlocks.size(); i++) {
			Material blockLandsOn = feet.getLocation().add(0, 1, 0).getBlock().getType();
			if (blockLandsOn.equals(chestObject.parseString(PlayerWarpGUI.unsafeBlocks.get(i)))) {
				return false; // not solid
			}
		}

		return true;
	}

	// ------------------------------------------------------
	// getController
	// ------------------------------------------------------
	public static PlayerWarpManager getPlayerWarpManager() {
		return playerWarpManager;
	}

	// ------------------------------------------------------
	// removePlayerObject
	// ------------------------------------------------------
	public static void removePlayerObject(UUID playerUUID) {
		PlayerWarpObject playerWarpObject = getPlayerWarpObject(playerUUID);
		playerWarpObject.removePlayerWarpObject(playerUUID);
		playerWarpObject = null;
	}

	// ------------------------------------------------------
	// updatePlayerObjectIcon
	// ------------------------------------------------------
	public void updatePlayerObjectIcon(UUID playerUUID, String icon) {
		if (getPlayerWarpObject(playerUUID) != null) {
			PlayerWarpObject a = getPlayerWarpObject(playerUUID);
			a.setIcon(icon);
			PlayerWarpHandler.updateIcon(playerUUID, icon);
		}
	}

	// ------------------------------------------------------
	// updatePlayerObjectTitle
	// ------------------------------------------------------
	public void updatePlayerObjectTitle(UUID playerUUID, String title) {
		if (getPlayerWarpObject(playerUUID) != null) {
			PlayerWarpObject a = getPlayerWarpObject(playerUUID);
			a.setTitle(title);
			PlayerWarpHandler.updateTitle(playerUUID, title);
		}
	}
	
	// ------------------------------------------------------
	// updatePlayerObjectLore
	// ------------------------------------------------------
	public void updatePlayerObjectLore(UUID playerUUID, String loreText, int loreLine) {
		if (getPlayerWarpObject(playerUUID) != null) {
			PlayerWarpObject a = getPlayerWarpObject(playerUUID);
			ArrayList<String> loreList = a.getLoreList();
			loreList.set(loreLine, loreText);
			//a.setLoreLine(loreText, loreLine);
			a.setLoreList(loreList);
			PlayerWarpHandler.updatelore(playerUUID, loreList);
		}
	}

	// ------------------------------------------------------
	// getPlayerWarpObject
	// ------------------------------------------------------
	public static PlayerWarpObject getPlayerWarpObject(UUID playerUUID) {
		for (PlayerWarpObject n : PlayerWarpObject.plugin.playerWarpObjects) {
			if (n.getPlayerUUID().equals(playerUUID)) {
				return n;
			}
		}
		return null;

	}

	// ------------------------------------------------------
	// getPlayerWarpObject
	// ------------------------------------------------------
	public Location getPlayerWarplocation(int warpID) {
		for (PlayerWarpObject n : PlayerWarpObject.plugin.playerWarpObjects) {
			if (n.getUid() == warpID) {
				n.getWarpLocation();
			}
		}
		return null;

	}

	// -----------------------------------------------------
	// getPlayerWarpObject
	// -----------------------------------------------------
	public boolean checkPlayerWarpObject(UUID playerUUID) {
		for (PlayerWarpObject n : PlayerWarpObject.plugin.playerWarpObjects) {
			if (n.getPlayerUUID().equals(playerUUID)) {
				return true;
			}
		}
		return false;

	}

	// -----------------------------------------------------
	// createWarpObjects
	// -----------------------------------------------------
	public void createWarpObjects(UUID playerUUID, String warpLocation, String title, String icon, ArrayList<String> loreList) {
		new PlayerWarpObject(playerUUID, warpLocation, title, icon, loreList);
	}

	public Location parseLoc(String str) {
		String[] arg = str.split(",");
		double[] parsed = new double[5];
		for (int a = 0; a < 3; a++) {
			parsed[a] = Double.parseDouble(arg[a + 1]);
		}

		Location location = new Location(Bukkit.getServer().getWorld(arg[0]), parsed[0], parsed[1], parsed[2], (float) parsed[3], (float) parsed[4]);
		return location;
	}

	public Location str2loc(String str) {

		String str2loc[] = str.split("\\:");
		Location loc = new Location(Bukkit.getServer().getWorld(str2loc[0]), 0, 0, 0, 0, 0);

		loc.setX(Double.parseDouble(str2loc[1]));

		loc.setY(Double.parseDouble(str2loc[2]));

		loc.setZ(Double.parseDouble(str2loc[3]));

		loc.setYaw((float) Double.parseDouble(str2loc[4]));

		loc.setPitch((float) Double.parseDouble(str2loc[5]));

		return loc;

	}

	public String loc2str(Location loc) {

		return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() + ":" + (float) loc.getYaw() + ":" + (float) loc.getPitch();

	}

}
