package Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import FileHandlers.PlayerWarpHandler;
import Managers.PlayerWarpManager;
import Objects.chestObject;
import PlayerWarpGUI.PlayerWarpGUI;

public class ChestListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClick(final InventoryClickEvent e) {

		// was it a player
		if (e.getWhoClicked() instanceof Player) {

			// does it match the right inventory name
			if (e.getInventory().getName().contains(chestObject.replaceColorVariables(PlayerWarpGUI.chestText))) {

				// cancel event, prevent player from removing the item
				e.setCancelled(true);

				// get warp ID
				int warpID = chestObject.getWarpID(e.getCurrentItem());
				
				e.getWhoClicked().teleport(chestObject.getWarpLocation(warpID));

				//close inventory
				e.getWhoClicked().closeInventory();
			}

		}
	}

}
