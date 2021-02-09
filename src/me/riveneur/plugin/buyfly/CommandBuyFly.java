package me.riveneur.plugin.buyfly;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandBuyFly implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;	
			if (args.length == 0) {
				player.sendMessage(ChatColor.GOLD + "Command usage: /buyfly <length of time in minutes>. One minute of flight costs $" + Main.PRICE + ".");
				return true;
			}

			int duration = 0;
			
			try {
				duration = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e) {
				player.sendMessage(ChatColor.RED + "Your length of flight must be a number from " + Main.MIN_LENGTH + " to " + Main.MAX_LENGTH + "!");
				return true;
			}
			
			if (Main.flights.get(player.getName()) != null) {
				player.sendMessage(ChatColor.RED + "You cannot purchase flight because your current flight time is still active!");
				return true;
			}
			if (duration >= Main.MIN_LENGTH && duration <= Main.MAX_LENGTH) {
				double amount = duration * Main.PRICE;
				double balance = Main.econ.getBalance((OfflinePlayer)player);
				if (balance >= amount) {
					player.setAllowFlight(true);
					player.setFlying(true);
					Main.flights.put(player.getName(), System.currentTimeMillis() + (duration * 60000));
					Main.warnings.put(player.getName(), duration / 2);
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "eco take " + player.getName() + " " + Double.toString(amount));
					player.sendMessage(ChatColor.GOLD + "Flight has been " + ChatColor.GREEN + "enabled " + ChatColor.GOLD + "for " + duration + " minutes!");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to purchase flight for " + duration + " minutes!");
				}
				return true;
			}
			else {
				player.sendMessage(ChatColor.RED + "Your length of flight must be an integer from " + Main.MIN_LENGTH + " to " + Main.MAX_LENGTH + "!");
			}
		}
		return true;
	}

}
