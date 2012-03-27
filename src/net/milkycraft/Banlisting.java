package net.milkycraft;

import org.bukkit.plugin.java.JavaPlugin;

public class Banlisting extends JavaPlugin
{
	/**
	 * Class variables
	 */
	// Config object
	private Config config;
	// Tag for logger
	public static final String prefix = "[BanListing]";

	/**
	 * Enable plugin
	 */
	@Override
	public void onEnable()
	{
		// Grab config
		config = new Config(this);
		// Check if plugin needs to be updated. Config will update for us.
		config.checkUpdate();
		// Create the Commander class to handle commands
		Commander commander = new Commander(this);
		// Set Commander to the specific command
		getCommand("bp").setExecutor(commander);
		//TODO check if MCBans is enabled?
		//TODO have listener for bans? Update list in Commander on event
		// Log message that we are now enabled. Give version as well
		this.getLogger().info(
				"BanListing v" + this.getDescription().getVersion()
						+ " enabled");
	}

	/**
	 * Disable plugin
	 */
	@Override
	public void onDisable()
	{
		// TODO when we do database stuff, we'd finish any queued queries and
		// close our connection here
		// Log message that we're disabled
		this.getLogger().info("Banlisting disabled");
	}

	/**
	 * Use this method to get the Config class. This is so that the default
	 * JavaPlugin method getConfig() still points to the actual
	 * file/YamlConfiguration, which Config needs access to. Abstracts the file
	 * system and allows for Bukkit/CraftBukkit to handle the file organization
	 * rather than the Config class itself.
	 * 
	 * You want to get the Config object if you need to know anything from the
	 * config class
	 * 
	 * @return Config object
	 */
	public Config getPluginConfig()
	{
		return config;
	}
}
