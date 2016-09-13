package Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import PlayerWarpGUI.PlayerWarpGUI;

public class ChestListener implements Listener {

	
	

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onInventoryClick(final InventoryClickEvent e) {

		// was it a player
		if (e.getWhoClicked() instanceof Player) {

			// does it match the right inventory name
			if (e.getInventory().getName().contains(PlayerWarpGUI.chestText)) {
				
				// cancel event, prevent player from removing the item
				e.setCancelled(true);
				
				
				
			}
			
		}
	}
	
	
	
}
