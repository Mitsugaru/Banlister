package net.milkycraft;

public class bConfiguration {
	private Banlisting plugin;

	public bConfiguration(Banlisting plugin){
		this.plugin = plugin;
	}

	public void create(){
		plugin.getConfig().loadDefaults(plugin.getResource("config.yml"));
		if(!plugin.getConfig().fileExists() || !plugin.getConfig().checkDefaults()){
			plugin.getConfig().saveDefaults();
		} 
		load();
	}
	public void reload(){         		
		load();
	}

	public void save(){
		plugin.saveConfig();
	}

	public void load(){
		plugin.reloadConfig();
	}
}