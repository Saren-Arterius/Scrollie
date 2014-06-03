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
        if (currentAttempt <= maxAttempt && !isSafeLocation(player.getLocation())) {
            player.sendMessage(MessageFormat.format(Lang.FIXING_LOCATION.toString(), currentAttempt, maxAttempt));
            player.teleport(location);
            location.add(0, 1, 0);
            currentAttempt++;
        } else if (confidence >= Main.getInstance().getConfig().getInt("variable.use.LocationFixer.ConfidenceToCancel")) {
            cancel();
        } else {
            confidence++;
        }
    }

    public static boolean isSafeLocation(Location location) {
        if (location.getY() < 0) {
            return false;
        }
        for (String blockTypeString: Main.getInstance().getConfig()
                .getStringList("variable.use.LocationFixer.SafeBlocks")) {
            if (location.getBlock().getType() == Material.getMaterial(blockTypeString.toUpperCase())
                    && location.add(0, 1, 0).getBlock().getType() == Material
                            .getMaterial(blockTypeString.toUpperCase())
                    && location.subtract(0, 1, 0).getBlock().getType() == Material.getMaterial(blockTypeString
                            .toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
