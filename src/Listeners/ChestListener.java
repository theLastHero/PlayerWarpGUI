package Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import Managers.PlayerWarpManager;
import Objects.chestObject;
import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;

public class ChestListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClick(final InventoryClickEvent e) {

		// was it a player
		if (e.getWhoClicked() instanceof Player) {

			// does it match the right inventory name
			if (e.getInventory().getName().contains(chestObject.replaceColorVariables(PlayerWarpGUI.chestText))) {

				// cancel event, prevent player from removing the item
				e.setCancelled(true);
				Player player = (Player) e.getWhoClicked(); 

				// get warp ID
				int warpID = chestObject.getWarpID(e.getCurrentItem());
				
				
				// do safeWarp checking
				if (PlayerWarpGUI.useSafeWarp) {
					// check for beathable air blocks
					if (!PlayerWarpManager.isSafeLocation(chestObject.getWarpLocation(warpID))) {
						player.sendMessage(A.b(" &4Teleport cancelled, location was unsafe to teleport to.", player.getDisplayName()));

						return;
						
					}
				}
				
				//check disabled worlds
				String world = chestObject.getWarpLocation(warpID).getWorld().getName().toString();
				for(int i = 0; i < PlayerWarpGUI.disabledWorlds.size(); i++){
					  if (PlayerWarpGUI.disabledWorlds.get(i).equalsIgnoreCase(world)){
							player.sendMessage(A.b(" &4Teleport cancelled, you cannot teleport to that world.", player.getDisplayName()));
						  return;
					  }
					}
				
				e.getWhoClicked().teleport(chestObject.getWarpLocation(warpID));

				//close inventory
				e.getWhoClicked().closeInventory();
			}

		}
	}

}
