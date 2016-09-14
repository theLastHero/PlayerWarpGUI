package Managers;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import Objects.PlayerWarpObject;
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

		return true;
	}

	// ------------------------------------------------------
	// getController
	// ------------------------------------------------------
	public static PlayerWarpManager getPlayerWarpManager() {
		return playerWarpManager;
	}

	// ------------------------------------------------------
	// getPlayerWarpObject
	// ------------------------------------------------------
	public PlayerWarpObject getPlayerWarpObject(UUID playerUUID) {
		for (PlayerWarpObject n : PlayerWarpObject.playerWarpObjects) {
			if (n.getPlayerUUID() == playerUUID) {
				return n;
			}
		}
		return null;

	}

	// ------------------------------------------------------
	// getPlayerWarpObject
	// ------------------------------------------------------
	public Location getPlayerWarplocation(int warpID) {
		for (PlayerWarpObject n : PlayerWarpObject.playerWarpObjects) {
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
		for (PlayerWarpObject n : PlayerWarpObject.playerWarpObjects) {
			if (n.getPlayerUUID() == playerUUID) {
				return true;
			}
		}
		return false;

	}

	// -----------------------------------------------------
	// createWarpObjects
	// -----------------------------------------------------
	public void createWarpObjects(UUID playerUUID, String warpLocation) {
		new PlayerWarpObject(playerUUID, warpLocation);
	}
	
    public Location parseLoc(String str){
	    String[] arg = str.split(",");
	    double[] parsed = new double[5];
	    for (int a = 0; a < 3; a++) {
	    parsed[a] = Double.parseDouble(arg[a+1]);
	    }
	     
	    Location location = new Location (Bukkit.getServer().getWorld(arg[0]), parsed[0], parsed[1], parsed[2], (float) parsed[3], (float) parsed[4]);
	    return location;
    }
    
    public Location str2loc(String str){
    	 
        String str2loc[]=str.split("\\:");
        Location loc = new Location(Bukkit.getServer().getWorld(str2loc[0]),0,0,0,0,0);
     
        loc.setX(Double.parseDouble(str2loc[1]));
     
        loc.setY(Double.parseDouble(str2loc[2]));
     
        loc.setZ(Double.parseDouble(str2loc[3]));

        loc.setYaw((float) Double.parseDouble(str2loc[4]));
        
        loc.setPitch((float) Double.parseDouble(str2loc[5]));
     
        return loc;
     
    }
    
    public String loc2str(Location loc){
        
        return loc.getWorld().getName()+":"+loc.getBlockX()+":"+loc.getBlockY()+":"+loc.getBlockZ()+":"+(float)loc.getYaw()+":"+(float)loc.getPitch();
     
    }
	    

}
