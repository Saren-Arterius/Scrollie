package net.wtako.Scrollie.EventHandlers;

import net.wtako.Scrollie.Utils.Messaging;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class PlayerListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().contains("exit")) {
            Messaging messager = new Messaging(event.getPlayer().getName() + "said exit.");
            messager.logInfo();
            event.setCancelled(true);
        }
    }
}