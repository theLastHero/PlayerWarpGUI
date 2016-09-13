package Objects;

import java.util.ArrayList;
import java.util.UUID;


public class PlayerWarpObject {

	public static ArrayList<PlayerWarpObject> playerWarpObjects = new ArrayList<PlayerWarpObject>();
	
	private UUID playerUUID;
	private String warpLocation;
	
	
	public PlayerWarpObject() {
	}

	
	public PlayerWarpObject(UUID playerUUID, String warpLocation) {

		this.setPlayerUUID(playerUUID);
		this.setWarpLocation(warpLocation);
		
		playerWarpObjects.add(this);
		

	}


	/**
	 * @return the playerUUID
	 */
	public UUID getPlayerUUID() {
		return playerUUID;
	}


	/**
	 * @param playerUUID the playerUUID to set
	 */
	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}


	/**
	 * @return the warpLocation
	 */
	public String getWarpLocation() {
		return warpLocation;
	}


	/**
	 * @param warpLocation the warpLocation to set
	 */
	public void setWarpLocation(String warpLocation) {
		this.warpLocation = warpLocation;
	}


}
