package me.riveneur.plugin.buyfly;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandCheckFly implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Main.flights.get(player.getName()) != null) {
				player.sendMessage(ChatColor.GOLD + "Around " + ((Main.flights.get(player.getName()) - System.currentTimeMillis()) / 60000) + " minute(s) and " + (int) (((Main.flights.get(player.getName()) - System.currentTimeMillis()) % 60000) / 1000) + " second(s) of flight remaining!");
			}
			else {
				player.sendMessage(ChatColor.RED + "You have not purchased flight!");
			}
		}
		return true;
	}

}
