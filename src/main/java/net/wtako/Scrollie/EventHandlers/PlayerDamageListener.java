package net.wtako.Scrollie.EventHandlers;

import net.wtako.Scrollie.Schedulers.PlayerPositionChecker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public static void onPlayerMove(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getEntity();
        if (!PlayerPositionChecker.inCheckingPlayerUUIDs.contains(player.getUniqueId())) {
            return;
        }
        if (event.getCause() == DamageCause.SUFFOCATION) {
            event.setCancelled(true);
        }

    }

}
