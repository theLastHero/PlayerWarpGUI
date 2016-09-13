package Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import PlayerWarpGUI.PlayerWarpGUI;

public class chestObject {

	public chestObject(PlayerWarpGUI playerWarpGUI) {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	public ItemStack parseString(String itemId) {
		String[] parts = itemId.split(":");
		int matId = Integer.parseInt(parts[0]);
		if (parts.length == 2) {
			short data = Short.parseShort(parts[1]);
			return new ItemStack(Material.getMaterial(matId), 1, data);
		}
		return new ItemStack(Material.getMaterial(matId));
	}

	public ItemStack getNextPageItemStack() {

		ItemStack nextPageItemstack = parseString(PlayerWarpGUI.nextPageIcon);
		ItemMeta nextPageMeta = nextPageItemstack.getItemMeta();
		nextPageMeta.setDisplayName(ChatColor.GREEN + "Next Page: ");
		nextPageItemstack.setItemMeta(nextPageMeta);

		return nextPageItemstack;
	}

	public static String replaceColorVariables(String str) {
		str = ChatColor.translateAlternateColorCodes('&', str);
		return str;

	}

	public static int getWarpID(ItemStack itemStack){
		List<String> loreList = new ArrayList<String>(); 
		loreList = itemStack.getItemMeta().getLore();
		int warpID = Integer.parseInt(ChatColor.stripColor(loreList.get(3).replace("Warp ID: ", "")));
		Bukkit.broadcastMessage("warp id is: "+warpID);
		
		return warpID;
	}
	
	
	public static PlayerWarpObject getPlayerWarpObject(int uid){
		for (PlayerWarpObject n : PlayerWarpObject.playerWarpObjects) {
			if (n.getUid() == uid) {
				return n;
			}
		}
		return null;
		
	}
	
	public static String getWarpLocation(int uid){
		
		PlayerWarpObject playerWarpObject = getPlayerWarpObject(uid);
		return playerWarpObject.getWarpLocation();
	
	}
	
	  public ItemStack setLore(ItemStack item,  ArrayList<String> message) {
		     ItemMeta meta = item.getItemMeta();
		     if(meta.hasLore()) {
		     message.addAll(meta.getLore());
		     }
		     meta.setLore(message);
		     item.setItemMeta(meta);
		     return item;
		   }
	
	@SuppressWarnings({ "unused" })
	public void openGUI(Player player, int page) {

		int pageNumber = page;
		int chestSize = PlayerWarpGUI.chestSize;
		int pageSize = (PlayerWarpGUI.chestSize - 1);

		// set next page icon
		ItemStack nextPageItemStack = getNextPageItemStack();

		// create inventory
		Inventory inv = Bukkit.createInventory(null, chestSize, replaceColorVariables(PlayerWarpGUI.chestText));

		int counter = 0;
		for (PlayerWarpObject wo : PlayerWarpObject.playerWarpObjects) {

			Bukkit.broadcastMessage(wo.getPlayerUUID().toString());

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
			lore.add(ChatColor.DARK_GRAY+"Warp ID: " +wo.uid);
			
			
			
			
			//
			ItemMeta playerWarpMeta = playerWarpItemStack.getItemMeta();
			playerWarpMeta.setDisplayName(playerWarpText);
			
			playerWarpMeta.setLore(lore);
			playerWarpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		   
			
			playerWarpItemStack.setItemMeta(playerWarpMeta);

			//setSecretCode(playerWarpItemStack, wo.getPlayerUUID().toString());

			inv.setItem(counter, playerWarpItemStack);
			counter++;

		}

		inv.setItem(pageSize, nextPageItemStack);

		player.openInventory(inv);

	}

	

}
