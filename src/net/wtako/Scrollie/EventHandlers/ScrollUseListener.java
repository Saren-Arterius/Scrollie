package net.wtako.Scrollie.EventHandlers;

import net.wtako.Scrollie.Methods.ScrollInstance;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class ScrollUseListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Integer scrollID = ScrollInstance.getScrollInstanceID(event.getItem());
        if (scrollID != null) {
            player.sendMessage(scrollID.toString());
        }
    }
}