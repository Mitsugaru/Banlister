package net.milkycraft;

public class BanWrapper extends org.bukkit.plugin.java.JavaPlugin {
    protected BanConfiguration config;
    //What is going on here?
    public BanConfiguration getConfig() {
        if(config == null) {
            config = new BanConfiguration(this);
        }
        return config;
    }
    public void reloadConfig() {
        getConfig().load();
    }
    public void saveConfig() {
        getConfig().save();
    }
    public void saveDefaultConfig() {
        getConfig().saveDefaults();
    }
}