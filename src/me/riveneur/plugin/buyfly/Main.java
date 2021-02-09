package me.riveneur.plugin.buyfly;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener {
	FileConfiguration config = getConfig();
	public static Economy econ = null;
	public static Map<String, Long> flights = new HashMap<>();
	public static Map<String, Integer> warnings = new HashMap<>();
	public static final int BUILD_LIMIT = 256;
	
	public static double PRICE;
	public static int MIN_LENGTH;
	public static int MAX_LENGTH;

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
	    }
	    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	    if (rsp == null) {
	    	return false;
	    }
	    econ = rsp.getProvider();
	    return econ != null;
	}

	@Override
	public void onEnable() {
		if(!(setupEconomy())) {
            getLogger().severe("Vault not found - Disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);
        }
		else {
			config.addDefault("price", 2.75);
			config.addDefault("minLength", 5);
			config.addDefault("maxLength", 60);
	
			PRICE = config.getDouble("price");
			MIN_LENGTH = config.getInt("minLength");
			MAX_LENGTH = config.getInt("maxLength");
			config.options().copyDefaults(true);
			saveConfig();
			
			getServer().getPluginManager().registerEvents(this, this);
			this.getCommand("buyfly").setExecutor(new CommandBuyFly());
			this.getCommand("checkfly").setExecutor(new CommandCheckFly());
			this.getCommand("refly").setExecutor(new CommandReFly());
		}
	}
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (flights.containsKey(event.getPlayer().getName())) {
			Player player = event.getPlayer();
			long timeLeft = flights.get(player.getName()) - System.currentTimeMillis();
			if (warnings.get(player.getName()) != null && warnings.get(player.getName()) > 0 && timeLeft <= warnings.get(player.getName()) * 60000) {
				player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Warning" + ChatColor.WHITE + "] " + ChatColor.GOLD + "Less than " + ((timeLeft / 60000) + 1) + " minutes of flight remaining!");
				warnings.put(player.getName(), 0);
			}
			if (warnings.get(player.getName()) != null && warnings.get(player.getName()) == 0 && timeLeft <= 60000) {
				player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Warning" + ChatColor.WHITE + "] " + ChatColor.GOLD + "Less than 1 minute of flight remaining!");
				warnings.put(player.getName(), -1);
			} 
			if (warnings.get(player.getName()) != null && warnings.get(player.getName()) == -1 && timeLeft <= 10000) {
				player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Warning" + ChatColor.WHITE + "] " + ChatColor.GOLD + "Less than 10 seconds of flight remaining! When time runs out, you will be teleported to the ground.");
				warnings.put(player.getName(), -2);
			} 
			if (timeLeft <= 0) {
				flights.remove(player.getName());
				warnings.remove(player.getName());
				player.setFlying(false);
				player.setAllowFlight(false);
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tp " + player.getName() + " " + player.getLocation().getX() + " " + BUILD_LIMIT + " " + player.getLocation().getZ());
				player.sendMessage(ChatColor.GOLD + "Flight has been " + ChatColor.RED + "disabled" + ChatColor.GOLD + "!");
			}
		}
	}
}
