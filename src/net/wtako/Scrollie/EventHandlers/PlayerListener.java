package net.wtako.Scrollie.EventHandlers;

import net.wtako.Scrollie.Utils.Logging;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class PlayerListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().contains("exit")) {
            Logging logger = new Logging();
            logger.logInfo(event.getPlayer().getName() + " said exit.");
            event.setCancelled(true);
        }
    }
}