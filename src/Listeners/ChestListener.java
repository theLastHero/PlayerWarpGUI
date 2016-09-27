package Listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import Managers.PlayerWarpManager;
import Objects.chestObject;
import PlayerWarpGUI.PlayerWarpGUI;
import Utils.A;

public class ChestListener implements Listener {

	public static PlayerWarpGUI plugin;
	final Map<UUID, BukkitTask> tpQueue = new HashMap<UUID, BukkitTask>();

	// -------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------
	public ChestListener(PlayerWarpGUI playerWarpGUI) {
		plugin = playerWarpGUI;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClick(final InventoryClickEvent e) {

		// was it a player
		if (e.getWhoClicked() instanceof Player) {
			
			//check if no idea was clicked
			if(e.getCurrentItem().getItemMeta() == null){
				return;
			}

			// does it match the right inventory name
			if (e.getInventory().getName().contains(chestObject.replaceColorVariables(PlayerWarpGUI.chestText))) {

				// cancel event, prevent player from removing the item
				e.setCancelled(true);

				Player player = (Player) e.getWhoClicked();

				
					// was it a next page icon clicked
					if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Next")) {

						// get next page number
						Scanner in = null;
						int nextPageNum;
						try {
							in = new Scanner(e.getCurrentItem().getItemMeta().getDisplayName()).useDelimiter("[^0-9]+");
							nextPageNum = in.nextInt();
						} finally {
							in.close();
						}

						// close inventory
						e.getWhoClicked().closeInventory();

						// open new inventory from next page
						chestObject.openGUI(player, nextPageNum);

						// exit
						return;
					}

					// get warp ID
					int warpID = chestObject.getWarpID(e.getCurrentItem());
					Location warpLocation = chestObject.getWarpLocation(warpID);

					// do safeWarp checking
					if (PlayerWarpGUI.useSafeWarp) {
						// check for beathable air blocks
						if (!PlayerWarpManager.isSafeLocation(warpLocation)) {
							player.sendMessage(A.b(" &7Teleport cancelled, location was unsafe to teleport to.", player.getDisplayName()));

							return;

						}
					}

					// check disabled worlds
					String world = warpLocation.getWorld().getName().toString();
					for (int i = 0; i < PlayerWarpGUI.disabledWorlds.size(); i++) {
						if (PlayerWarpGUI.disabledWorlds.get(i).equalsIgnoreCase(world)) {
							player.sendMessage(A.b(" &7Teleport cancelled, you cannot teleport to that world.", player.getDisplayName()));
							return;
						}
					}

					tpQueue.put(player.getUniqueId(), new Teleport(PlayerWarpGUI.cooldown, player.getUniqueId(), warpLocation).runTaskTimer(PlayerWarpGUI.getInstance(), 0, 20));

					// close inventory
					e.getWhoClicked().closeInventory();
				}
			}
		
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		if (PlayerWarpGUI.cancelOnMovement) {
			if (tpQueue.containsKey(p.getUniqueId())) {
				tpQueue.get(p.getUniqueId()).cancel();
				tpQueue.remove(p.getUniqueId());
				p.sendMessage(A.c("&7Teleport cancelled.", p.getName()));
			}
		}
	}

	private class Teleport extends BukkitRunnable {
		int count;
		UUID player;
		Location loc;

		Teleport(int count, UUID player, Location loc) {
			this.count = count;
			this.player = player;
			this.loc = loc;
		}

		@Override
		public void run() {
			if (count > 0) {
				Bukkit.getPlayer(player).sendMessage(A.c("&7teleporting in: &6" + count, Bukkit.getPlayer(player).getName()));
				count--;
			} else {
				// Teleport code here
				Bukkit.getPlayer(player).teleport(loc);
				tpQueue.remove(player);
				this.cancel();
				Bukkit.getPlayer(player).sendMessage(A.c("&7teleport Completed.", Bukkit.getPlayer(player).getName()));
			}
		}
	}

}
