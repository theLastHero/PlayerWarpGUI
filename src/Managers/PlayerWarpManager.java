package Managers;

import java.util.UUID;

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
	
}
