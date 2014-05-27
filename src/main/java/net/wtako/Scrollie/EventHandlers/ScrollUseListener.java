package net.wtako.Scrollie.EventHandlers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.wtako.Scrollie.Methods.ScrollInstance;
import net.wtako.Scrollie.Schedulers.TeleportationTask;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class ScrollUseListener implements Listener {

    private static final Map<String, Integer>           movedCount = new HashMap<String, Integer>();
    private static final Map<String, TeleportationTask> TPTask     = new HashMap<String, TeleportationTask>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final TeleportationTask currentTPTask = ScrollUseListener.TPTask.get(player.getName());
        if (currentTPTask != null) {

            if (currentTPTask.getWarmUpTime() != currentTPTask.getWarmUpTimeLeft()) {
                TeleportationTask.interrupt(player);
            }
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        try {
            if (!ScrollInstance.isValid(event.getItem())) {
                return;
            }
            final ScrollInstance scroll = new ScrollInstance(event.getItem());
            if (scroll.doPreActions(player)) {
                try {
                    ScrollUseListener.TPTask.put(player.getName(), new TeleportationTask(player, scroll));
                    event.setCancelled(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(Lang.CANT_START_TP.toString());
            }
        } catch (final SQLException e) {
            player.sendMessage(Lang.DB_EXCEPTION.toString());
            e.printStackTrace();
        }
    }

    public static Map<String, TeleportationTask> getTPTask() {
        return ScrollUseListener.TPTask;
    }

    public static Map<String, Integer> getMovedCount() {
        return ScrollUseListener.movedCount;
    }

}