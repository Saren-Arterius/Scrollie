package net.wtako.Scrollie.Schedulers;

import java.sql.SQLException;
import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.EventHandlers.PlayerActionsListener;
import net.wtako.Scrollie.EventHandlers.ScrollUseListener;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Methods.ScrollInstance;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportationTask extends BukkitRunnable {

    private Player         player;
    private Integer        warmUpTimeLeft;
    private Integer        warmUpTime;
    private Location       location;
    private ScrollInstance scroll;

    public TeleportationTask(Player player, ScrollInstance scroll) {
        this.player = player;
        this.scroll = scroll;
        this.location = scroll.getLocation();
        if (player.hasPermission("Scrollie.overrideWUCD")) {
            warmUpTime = 0;
            warmUpTimeLeft = 0;
        } else {
            this.warmUpTime = scroll.getWarmUpTime();
            this.warmUpTimeLeft = scroll.getWarmUpTime();
        }
        if (warmUpTime > 0) {
            player.sendMessage(MessageFormat.format(Lang.WARMING_UP.toString(), warmUpTimeLeft));
        }
        PlayerActionsListener.registerEvents(player);
        this.runTaskTimer(Main.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if (warmUpTimeLeft > 0) {
            warmUpTimeLeft--;
            if (warmUpTimeLeft % 5 == 0 && warmUpTimeLeft != 0) {
                player.sendMessage(MessageFormat.format(Lang.WARMING_UP_SECONDS_LEFT.toString(), warmUpTimeLeft));
            }
        } else {
            try {
                scroll.doPostActions(player);
            } catch (SQLException e) {
                player.sendMessage(Lang.DB_EXCEPTION.toString());
                e.printStackTrace();
            }
            this.cancel();
            PlayerActionsListener.unregisterEvents(player);
            player.teleport(location);
            ScrollUseListener.getTPTask().remove(player.getName());
            if (scroll.getTargetName() != null) {
                player.sendMessage(MessageFormat.format(Lang.FINISHED_USING.toString(), scroll.getTargetName(),
                        location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            } else {
                player.sendMessage(MessageFormat.format(Lang.FINISHED_USING.toString(),
                        ScrollDatabase.destinationTypeIntegerToString(scroll.getDestinationType()),
                        location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            }
        }
    }

    public static void interrupt(Player player) {
        player.sendMessage(Lang.WARM_UP_FAIL.toString());
        ScrollUseListener.getTPTask().get(player.getName()).cancel();
        ScrollUseListener.getTPTask().remove(player.getName());
        ScrollUseListener.getMovedCount().remove(player.getName());
        PlayerActionsListener.unregisterEvents(player);
        if (Main.getInstance().getConfig().getBoolean("variable.use.WarmUpFailDoesHarmToScroll")) {
            try {
                ScrollUseListener.getTPTask().get(player.getName()).scroll.updateRemaingTimes(player);
            } catch (SQLException e) {
                player.sendMessage(Lang.DB_EXCEPTION.toString());
                e.printStackTrace();
            }
        }
    }

    public Integer getWarmUpTime() {
        return warmUpTime;
    }

    public Integer getWarmUpTimeLeft() {
        return warmUpTimeLeft;
    }

}