package net.wtako.Scrollie.EventHandlers;

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class PlayerListener implements Listener {

    public static Logger log = Logger.getLogger("Scrollie");
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().contains("exit")) {
            log.info(event.getPlayer().getName() + " said exit.");
            event.setCancelled(true);
        }
    }
}