package net.wtako.Scrollie.Methods.Locations;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.LocationSource;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnLocation implements LocationSource {

    public SpawnLocation(Player player) {
    }

    @Override
    public Location get() {
        return Main.getInstance().getServer()
                .getWorld(Main.getInstance().getConfig().getString("variable.use.SpawnWorld")).getSpawnLocation();
    }

}
