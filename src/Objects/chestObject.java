package Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Managers.PlayerWarpManager;
import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;

public class chestObject {

	public chestObject(PlayerWarpGUI playerWarpGUI) {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	public static ItemStack parseString(String itemId) {
		String[] parts = itemId.split(":");
		int matId = Integer.parseInt(parts[0]);
		if (parts.length == 2) {
			short data = Short.parseShort(parts[1]);
			return new ItemStack(Material.getMaterial(matId), 1, data);
		}
		return new ItemStack(Material.getMaterial(matId));
	}

	public static ItemStack getNextPageItemStack(int pageNum) {

		ItemStack nextPageItemstack = parseString(PlayerWarpGUI.nextPageIcon);
		ItemMeta nextPageMeta = nextPageItemstack.getItemMeta();
		nextPageMeta.setDisplayName(ChatColor.GREEN + "Next Page: " + pageNum);
		nextPageItemstack.setItemMeta(nextPageMeta);

		return nextPageItemstack;
	}

	public static String replaceColorVariables(String str) {
		str = ChatColor.translateAlternateColorCodes('&', str);
		return str;

	}

	public static int getWarpID(ItemStack itemStack) {
		List<String> loreList = new ArrayList<String>();
		loreList = itemStack.getItemMeta().getLore();
		int warpID = Integer.parseInt(ChatColor.stripColor(loreList.get(3).replace("Warp ID: ", "")));

		return warpID;
	}

	public static PlayerWarpObject getPlayerWarpObject(int uid) {
		for (PlayerWarpObject n : PlayerWarpObject.playerWarpObjects) {
			if (n.getUid() == uid) {
				return n;
			}
		}
		return null;

	}

	public static Location getWarpLocation(int uid) {

		PlayerWarpObject playerWarpObject = getPlayerWarpObject(uid);
		return PlayerWarpManager.getPlayerWarpManager().str2loc(playerWarpObject.getWarpLocation());

	}

	public ItemStack setLore(ItemStack item, ArrayList<String> message) {
		ItemMeta meta = item.getItemMeta();
		if (meta.hasLore()) {
			message.addAll(meta.getLore());
		}
		meta.setLore(message);
		item.setItemMeta(meta);
		return item;
	}

	@SuppressWarnings({ "unused" })
	public static void openGUI(Player player, int page) {

		int pageNumber = page;
		int chestSize = PlayerWarpGUI.chestSize;
		int pageSize = (PlayerWarpGUI.chestSize - 1);
		int startNum = 0; // decalre variable
		int pageNum = page; // what page to start from
		boolean showNext = true; // to show next page icon or not

		// set next page icon

		// create inventory
		Inventory inv = Bukkit.createInventory(null, chestSize, replaceColorVariables(PlayerWarpGUI.chestText));

		
		// create array list of warps by name from hashmap
		ArrayList<PlayerWarpObject> playerWarpObjects = PlayerWarpObject.playerWarpObjects;
		
		// set start
		startNum = pageNum * pageSize;
		Bukkit.broadcastMessage("startNum: " + startNum);

		// calculate loop size
		int loopSize = startNum + pageSize;
		Bukkit.broadcastMessage("loopSize: " + loopSize);
		
		// check if page size is smaller then max pageSize
		// if not then set to actual size, and set showNext to false
		if (loopSize > (playerWarpObjects.size())) {
			loopSize = ((playerWarpObjects.size()) - startNum);
			showNext = false;
		}
		Bukkit.broadcastMessage("loopSize2: " + loopSize);

		// loop through all for the page
		//!!!!!!!!!!!!!!! HERE !!!!!!!!!!!!!!!!!!!
		//not showing nextpage icons, something to do with loop size bigger the startnum????
		for (int i = 0; i < loopSize; i++) {
			int objNum = startNum + i;
			Bukkit.broadcastMessage("aaa: " + i);
			//
			String playerWarpText = PlayerWarpGUI.playerWarpText;
			
			PlayerWarpObject a = playerWarpObjects.get(objNum);
			int b = a.getUid();
			
			// fix display name here
			playerWarpText = A.c(playerWarpText, Bukkit.getOfflinePlayer(a.getPlayerUUID()).getName());
			//playerWarpText = A.c(playerWarpText, PlayerWarpGUI.nameFetcher.call().);
			
			
			//
			String playerIcon = PlayerWarpGUI.defaultWarpIcon;
			ItemStack playerWarpItemStack = parseString(playerIcon);

			//
			ArrayList<String> lore = new ArrayList<String>();

			lore.add("");
			lore.add("");
			lore.add("");
			lore.add(ChatColor.DARK_GRAY + "Warp ID: " + b);

			//
			ItemMeta playerWarpMeta = playerWarpItemStack.getItemMeta();
			playerWarpMeta.setDisplayName(playerWarpText);

			playerWarpMeta.setLore(lore);
			playerWarpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

			playerWarpItemStack.setItemMeta(playerWarpMeta);

			// setSecretCode(playerWarpItemStack, wo.getPlayerUUID().toString());

			inv.setItem(i, playerWarpItemStack);
			//i++;
			
			
		}
	
		/*
		int counter = 0;
		for (PlayerWarpObject wo : PlayerWarpObject.playerWarpObjects) {

			//
			String playerWarpText = PlayerWarpGUI.playerWarpText;
			playerWarpText = playerWarpText.replace("[username]", Bukkit.getOfflinePlayer(wo.getPlayerUUID()).getName());
			playerWarpText = replaceColorVariables(playerWarpText);

			//
			String playerIcon = PlayerWarpGUI.defaultWarpIcon;
			ItemStack playerWarpItemStack = parseString(playerIcon);

			//
			ArrayList<String> lore = new ArrayList<String>();

			lore.add("");
			lore.add("");
			lore.add("");
			lore.add(ChatColor.DARK_GRAY + "Warp ID: " + wo.uid);

			//
			ItemMeta playerWarpMeta = playerWarpItemStack.getItemMeta();
			playerWarpMeta.setDisplayName(playerWarpText);

			playerWarpMeta.setLore(lore);
			playerWarpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

			playerWarpItemStack.setItemMeta(playerWarpMeta);

			// setSecretCode(playerWarpItemStack, wo.getPlayerUUID().toString());

			inv.setItem(counter, playerWarpItemStack);
			counter++;

		}
		*/

		// if going to show nextPage icon or not
		if (showNext) {

			ItemStack nextPageItemStack = getNextPageItemStack(page+1);
			inv.setItem(pageSize, nextPageItemStack);
		}
		

		player.openInventory(inv);

	}

}
