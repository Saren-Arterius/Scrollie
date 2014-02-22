package net.wtako.Scrollie;

import net.wtako.Scrollie.commands.CommandScrollie;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

	public void onEnable() {
	    this.saveDefaultConfig();
	    this.getConfig().options().copyDefaults(true);
	    this.getLogger().info("Hello bloody java world!");
	    
	    getCommand("scrollie").setExecutor(new CommandScrollie(this));
	}
	
	public void onDisable() {
	    this.getLogger().info("Good-bye bloody chalon!");
	}
    
}
