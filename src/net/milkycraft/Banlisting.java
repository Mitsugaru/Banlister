package net.milkycraft;

import java.util.logging.Logger;

public class Banlisting extends BanWrapper {
	private bConfiguration configi;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static final String prefix = "[BanListing]";
	@Override
	public void onEnable() {
		Commander myGoldExecutor ;		
		 myGoldExecutor = new Commander(this, config);
		 getCommand("bannedplayers").setExecutor(myGoldExecutor);
		saveConfig();
		configi = new bConfiguration(this);
		configi.create();
		configi.reload();
		this.getServer().getPluginManager();
		this.getDescription();		
	}
			@Override
			public void onDisable() {
				Logger log = Logger.getLogger("Minecraft");
				{
					log.info("Banlisting disabled");
				}
			}
				public bConfiguration config(){
					return configi;
				}	
}
