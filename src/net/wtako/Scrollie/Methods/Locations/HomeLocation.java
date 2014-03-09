package net.wtako.Scrollie.Methods.Locations;

import net.wtako.Scrollie.Methods.LocationSource;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeLocation implements LocationSource {

    private Location     destLoc;
    private final Player player;

    public HomeLocation(Player player) {
        this.player = player;
    }

    @Override
    public Location get() {
        destLoc = player.getBedSpawnLocation();
        if (destLoc == null) {
            player.sendMessage(Lang.YOU_HAVE_NO_HOME.toString());
        }
        return destLoc;
    }

}
