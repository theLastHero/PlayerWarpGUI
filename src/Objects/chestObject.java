package Objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
	
	public ItemStack getNextPageItemStack(){
		
		ItemStack nextPageItemstack = parseString(PlayerWarpGUI.nextPageIcon);
		ItemMeta nextPageMeta = nextPageItemstack.getItemMeta();
		nextPageMeta.setDisplayName(ChatColor.GREEN + "Next Page: ");
		nextPageItemstack.setItemMeta(nextPageMeta);
		
		return nextPageItemstack;
	}
	
	public static String replaceColorVariables(String str){
		str = ChatColor.translateAlternateColorCodes('&', str);
		return str;
		
	}

	@SuppressWarnings("deprecation")
	public void openGUI(Player player, int page) {
		
		int pageNumber = page;
		int chestSize = PlayerWarpGUI.chestSize;
		int pageSize = (PlayerWarpGUI.chestSize -1);
		String playerWarpText = PlayerWarpGUI.playerWarpText;
		
		//set next page icon
		ItemStack nextPageItemStack = getNextPageItemStack();
		
		//create inventory
		Inventory inv = Bukkit.createInventory(null, chestSize, ChatColor.DARK_RED + PlayerWarpGUI.chestText);
		
		int counter = 0;
		for (PlayerWarpObject wo : PlayerWarpObject.playerWarpObjects) {

			String playerName =Bukkit.getOfflinePlayer(wo.getPlayerUUID()).getName();
			playerWarpText = playerWarpText.replace("[username]", playerName);
			playerWarpText = replaceColorVariables(playerWarpText);
			
			String playerIcon = PlayerWarpGUI.defaultWarpIcon;
			ItemStack playerWarpItemStack = parseString(playerIcon);

			ItemMeta playerWarpMeta = playerWarpItemStack.getItemMeta();
			playerWarpMeta.setDisplayName(playerWarpText);
			playerWarpItemStack.setItemMeta(playerWarpMeta);
			
			inv.setItem(counter, playerWarpItemStack);
			counter++;

		}
		
		
	    
	    inv.setItem(pageSize,nextPageItemStack);
	    
		
		player.openInventory(inv);
		
	}

}
