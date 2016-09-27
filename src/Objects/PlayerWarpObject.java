package Objects;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerWarpObject {

	public static ArrayList<PlayerWarpObject> playerWarpObjects = new ArrayList<PlayerWarpObject>();

	static int UNIQUE_ID = 0;
	int uid = ++UNIQUE_ID;

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	
    public int hashCode() {
        return uid;
    }

	public void removePlayerWarpObject(UUID playerUUID) {
		playerWarpObjects.remove(this);
	}

	/**
	 * @param uid
	 *            the uid to set
	 */

	private UUID playerUUID;
	private String warpLocation;
	private String title;
	private String icon;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public int PlayerWarpObject() {
		 return uid;
	}

	public PlayerWarpObject(UUID playerUUID, String warpLocation, String title, String icon) {

		this.setPlayerUUID(playerUUID);
		this.setWarpLocation(warpLocation);
		this.setTitle(title);
		this.setIcon(icon);
		
		playerWarpObjects.add(this);

	}

	/**
	 * @return the playerUUID
	 */
	public UUID getPlayerUUID() {
		return playerUUID;
	}

	/**
	 * @param playerUUID
	 *            the playerUUID to set
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
	 * @param warpLocation
	 *            the warpLocation to set
	 */
	public void setWarpLocation(String warpLocation) {
		this.warpLocation = warpLocation;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

}
