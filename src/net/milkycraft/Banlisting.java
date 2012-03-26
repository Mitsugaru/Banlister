package net.milkycraft;

import java.util.logging.Logger;

public class Banlisting extends BanWrapper
{
	private bConfiguration configi;
	public static final String prefix = "[BanListing]";

	@Override
	public void onEnable()
	{
		Commander myGoldExecutor = new Commander(this);
		getCommand("bp").setExecutor(myGoldExecutor);
		saveConfig();
		configi = new bConfiguration(this);
		configi.create();
		configi.reload();
		//this.getServer().getPluginManager();
		this.getLogger().info("BanListing v" + this.getDescription().getVersion() + " enabled");
	}

	@Override
	public void onDisable()
	{
		this.getLogger().info("Banlisting disabled");
	}

	public bConfiguration config()
	{
		return configi;
	}
}
