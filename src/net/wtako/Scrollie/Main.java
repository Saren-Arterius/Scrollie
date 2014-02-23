package net.wtako.Scrollie;

import net.wtako.Scrollie.Commands.CommandScrollie;
import net.wtako.Scrollie.EventHandlers.PlayerListener;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    
    private static Main instance;
    
	public void onEnable() {
	    this.saveDefaultConfig();
	    this.getConfig().options().copyDefaults(true);
	    this.getLogger().info("Hello bloody java world!");
	    this.getCommand("scrollie").setExecutor(new CommandScrollie(this));
	    this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}
	
	public void onDisable() {
	    this.getLogger().info("Good-bye bloody chalon!");
	}
	
	public static Main getInstance() {
        return instance;
	}
    
}
