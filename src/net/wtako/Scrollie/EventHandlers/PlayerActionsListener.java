package net.wtako.Scrollie.EventHandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.RegisteredListener;

public class PlayerActionsListener implements Listener {

    private static final Map<String, PlayerActionsListener> playerListenersMap = new HashMap<String, PlayerActionsListener>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (ScrollUseListener.getTPTask().get(player.getName()) == null) {
            return;
        }
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getY() == event.getTo().getY()
                && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }
        Integer oldVal = ScrollUseListener.getMovedCount().get(player.getName());
        if (oldVal == null) {
            oldVal = 0;
        }
        Integer newVal = oldVal + 1;
        ScrollUseListener.getMovedCount().put(player.getName(), newVal);
        if (newVal >= 10
                && ScrollUseListener.getTPTask().get(player.getName()).getWarmUpTime() != ScrollUseListener.getTPTask()
                        .get(player.getName()).getWarmUpTimeLeft()) {
            player.sendMessage(Lang.WARM_UP_FAIL.toString());
            ScrollUseListener.getTPTask().get(player.getName()).cancel();
            ScrollUseListener.getTPTask().remove(player.getName());
            ScrollUseListener.getMovedCount().remove(player.getName());
            PlayerActionsListener.unregisterEvents(player);
        }
    }

    public static void unregisterEvents(Player player) {
        boolean needToUnregisterEvent = true;
        final PlayerActionsListener playerListeners = playerListenersMap.remove(player.getName());
        for (final Entry<String, PlayerActionsListener> entry: playerListenersMap.entrySet()) {
            if (entry.getValue().toString().split("@")[0].equals(playerListeners.toString().split("@")[0])) {
                needToUnregisterEvent = false;
                break;
            }
        }
        if (needToUnregisterEvent) {
            PlayerMoveEvent.getHandlerList().unregister(Main.getInstance());
        }
    }

    public static void registerEvents(Player player) {
        PlayerActionsListener playerActionsListener = new PlayerActionsListener();
        boolean needToRegisterEvent = true;
        final String playerMoveListenerClassName = playerActionsListener.toString().split("@")[0];
        for (final RegisteredListener listener: HandlerList.getRegisteredListeners(Main.getInstance())) {
            if (listener.getListener().toString().contains(playerMoveListenerClassName)) {
                needToRegisterEvent = false;
                break;
            }
        }
        if (needToRegisterEvent) {
            player.getServer().getPluginManager().registerEvents(playerActionsListener, Main.getInstance());
        }
        playerListenersMap.put(player.getName(), playerActionsListener);
    }

}
