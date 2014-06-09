package net.wtako.Scrollie.Schedulers;

import java.sql.SQLException;
import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.EventHandlers.PlayerActionsListener;
import net.wtako.Scrollie.EventHandlers.ScrollUseListener;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Methods.ScrollInstance;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;

public class TeleportationTask extends BukkitRunnable {

    private final Player         player;
    private Integer              warmUpTimeLeft;
    private Integer              warmUpTime;
    private final Location       location;
    private final ScrollInstance scroll;

    public TeleportationTask(Player player, ScrollInstance scroll) {
        this.player = player;
        this.scroll = scroll;
        location = scroll.getLocation();
        if (player.hasPermission(Main.getInstance().getProperty("artifactId") + ".overrideWUCD")) {
            warmUpTime = 0;
            warmUpTimeLeft = 0;
        } else {
            warmUpTime = scroll.getWarmUpTime();
            warmUpTimeLeft = scroll.getWarmUpTime();
        }
        if (warmUpTime > 0) {
            player.sendMessage(MessageFormat.format(Lang.WARMING_UP.toString(), warmUpTimeLeft));
        }
        if (Main.getInstance().getConfig().getBoolean("system.NCPSupport")) {
            try {
                NCPExemptionManager.exemptPermanently(player, CheckType.MOVING_SURVIVALFLY);
            } catch (final Error e) {
                player.sendMessage(MessageFormat.format(Lang.ERROR_HOOKING.toString(), "NoCheatPlus"));
            }
        }
        PlayerActionsListener.registerEvents(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000000, 1));
        runTaskTimer(Main.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if (warmUpTimeLeft > 0) {
            warmUpTimeLeft--;
            player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 31);
            if (warmUpTimeLeft % 5 == 0 && warmUpTimeLeft != 0) {
                player.sendMessage(MessageFormat.format(Lang.WARMING_UP_SECONDS_LEFT.toString(), warmUpTimeLeft));
            }
        } else {
            try {
                scroll.doPostActions(player);
            } catch (final SQLException e) {
                player.sendMessage(Lang.DB_EXCEPTION.toString());
                e.printStackTrace();
            }
            TeleportationTask.end(player);
            if (player.isInsideVehicle() && player.getVehicle() != null && player.getVehicle() instanceof Horse) {
                final Horse horse = (Horse) player.getVehicle();
                player.leaveVehicle();
                player.teleport(location);
                horse.teleport(location);
                horse.setPassenger(player);
            } else {
                player.teleport(location);
            }
            if (scroll.getTargetName() != null) {
                player.sendMessage(MessageFormat.format(Lang.FINISHED_USING.toString(), scroll.getTargetName(),
                        location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            } else {
                player.sendMessage(MessageFormat.format(Lang.FINISHED_USING.toString(),
                        ScrollDatabase.destinationTypeIntegerToString(scroll.getDestinationType()),
                        location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            }
            new PlayerPositionChecker(player, location);
        }
    }

    public static void interrupt(Player player) {
        TeleportationTask.end(player);
        player.sendMessage(Lang.WARM_UP_FAIL.toString());
        if (Main.getInstance().getConfig().getBoolean("variable.use.WarmUpFailDoesHarmToScroll")) {
            ScrollUseListener.getTPTask().get(player.getName()).scroll.updateRemainingTimes(player);
        }
    }

    public static void end(final Player player) {
        player.removePotionEffect(PotionEffectType.CONFUSION);
        ScrollUseListener.getTPTask().get(player.getName()).cancel();
        ScrollUseListener.getTPTask().remove(player.getName());
        ScrollUseListener.getMovedCount().remove(player.getName());
        PlayerActionsListener.unregisterEvents(player);
        if (Main.getInstance().getConfig().getBoolean("system.NCPSupport")) {
            try {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        NCPExemptionManager.unexempt(player);
                    }
                }.runTaskLater(Main.getInstance(), Main.getInstance().getConfig().getInt("system.NCPExemptTicks"));
            } catch (final Error e) {
                player.sendMessage(MessageFormat.format(Lang.ERROR_HOOKING.toString(), "NoCheatPlus"));
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