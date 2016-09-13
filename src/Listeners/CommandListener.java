package Listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.PlayerWarpGUI;



public class CommandListener implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("playerwarps")) {
			
			if ((args.length < 1) || (args[0].equalsIgnoreCase("list"))) {
				// do chest open gui here
				PlayerWarpGUI.chestObject.openGUI(player, 0);
				return true;
			}
			
		}

		
		return false;
	}

}
