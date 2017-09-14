package Objects;

import java.util.ArrayList;
import java.util.UUID;

import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerWarpObject {

	public static PlayerWarpGUI plugin;

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
		plugin.playerWarpObjects.remove(this);
	}

	/**
	 * @param uid
	 *            the uid to set
	 */

	private UUID playerUUID;
	private String warpLocation;
	private String title;
	private String icon;
	private ArrayList<String> loreList;

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
	
	public void setLoreLine(String loreText, int loreLine) {
		this.loreList.set(loreLine, loreText);
	}

	public int PlayerWarpObject() {
		 return uid;
	}

	public PlayerWarpObject(UUID playerUUID, String warpLocation, String title, String icon, ArrayList<String> loreList) {

		this.setPlayerUUID(playerUUID);
		this.setWarpLocation(warpLocation);
		this.setTitle(title);
		this.setIcon(icon);
		this.setLoreList(loreList);
		
		plugin.playerWarpObjects.add(this);

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

	public ArrayList<String> getLoreList() {
		return loreList;
	}

	public void setLoreList(ArrayList<String> loreList) {
		this.loreList = loreList;
	}

}
