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

    public PlayerPositionChecker(Player player, Location location) {
        this.player = player;
        this.location = location;
        currentAttempt = 1;
        maxAttempt = Main.getInstance().getConfig().getInt("variable.use.LocationFixer.MaxAttempt");
        runTaskTimer(Main.getInstance(), Main.getInstance().getConfig().getInt("variable.use.LocationFixer.Delay"),
                Main.getInstance().getConfig().getInt("variable.use.LocationFixer.Interval"));
    }

    @Override
    public void run() {
        if (currentAttempt <= maxAttempt && player.getLocation().getBlock().getType() != Material.AIR) {
            player.sendMessage(MessageFormat.format(Lang.FIXING_LOCATION.toString(), currentAttempt, maxAttempt));
            location.add(0, 1, 0);
            player.teleport(location);
            currentAttempt++;
        } else {
            cancel();
        }
    }

}
