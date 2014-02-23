package net.wtako.Scrollie.EventHandlers;

import net.wtako.Scrollie.Main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class PlayerListener implements Listener {
    
    private Main plugin;

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin.getLogger().info("tresting");
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        this.plugin.getLogger().info("aaa");
        this.plugin.getLogger().info(event.getPlayer().getName() + event.getMessage());
    }
}