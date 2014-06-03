package net.wtako.Scrollie.Schedulers;

import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerPositionChecker extends BukkitRunnable {

    private final Player   player;
    private final Location location;
    private int            currentAttempt;
    private final int      maxAttempt;
    private int            confidence;

    public PlayerPositionChecker(Player player, Location location) {
        this.player = player;
        this.location = location;
        confidence = 0;
        currentAttempt = 1;
        maxAttempt = Main.getInstance().getConfig().getInt("variable.use.LocationFixer.MaxAttempt");
        runTaskTimer(Main.getInstance(), Main.getInstance().getConfig().getInt("variable.use.LocationFixer.Delay"),
                Main.getInstance().getConfig().getInt("variable.use.LocationFixer.Interval"));
    }

    @Override
    public void run() {
        Location upperLocation = player.getLocation();
        upperLocation.add(0, 1, 0);
        if (currentAttempt <= maxAttempt && (!isSafeBlockType(player.getLocation()) || !isSafeBlockType(upperLocation))) {
            player.sendMessage(MessageFormat.format(Lang.FIXING_LOCATION.toString(), currentAttempt, maxAttempt));
            location.add(0, 1, 0);
            player.teleport(location);
            currentAttempt++;
            confidence--;
        } else if (confidence >= Main.getInstance().getConfig().getInt("variable.use.LocationFixer.ConfidenceToCancel")) {
            cancel();
        } else {
            confidence++;
        }
    }

    public static boolean isSafeBlockType(Location location) {
        if (location.getBlock().getType() == Material.AIR) {
            return true;
        }
        if (location.getBlock().getType() == Material.WATER) {
            return true;
        }
        if (location.getBlock().getType() == Material.STATIONARY_WATER) {
            return true;
        }
        return false;
    }

}
