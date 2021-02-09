package me.riveneur.plugin.buyfly;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandReFly implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Main.flights.get(player.getName()) != null && Main.flights.get(player.getName()) - System.currentTimeMillis() > 0) {
				if (!player.isFlying()) {
					player.setAllowFlight(true);
					player.setFlying(true);
				}
				player.sendMessage(ChatColor.GOLD + "Flight has been" + ChatColor.GREEN + " re-enabled" + ChatColor.GOLD + "! Use /checkfly to see how much remaining flight time you have.");
			}
			else {
				player.sendMessage(ChatColor.RED + "Flight cannot be re-enabled! You either did not purchase flight recently or your time expired.");
			}
		}
		return true;
	}

}
