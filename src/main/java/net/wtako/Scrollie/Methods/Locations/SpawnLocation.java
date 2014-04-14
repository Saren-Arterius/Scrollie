package net.wtako.Scrollie.Methods.Locations;

import java.io.File;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.LocationSource;
import net.wtako.Scrollie.Utils.FileTool;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpawnLocation implements LocationSource {

    private Location     destLoc;
    private final Player player;

    public SpawnLocation(Player player) {
        this.player = player;
    }

    @Override
    public Location get() {
        if (Main.getInstance().getConfig().getBoolean("system.EssentialsSupport")) {
            final File spawnConfigFile = FileTool.getChildFile(FileTool.getChildFile(Main.getInstance().getDataFolder()
                    .getParentFile().getAbsoluteFile(), "Essentials", false), "spawn.yml", false);
            if (spawnConfigFile != null) {
                final YamlConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnConfigFile);
                final World world = Main.getInstance().getServer()
                        .getWorld(spawnConfig.getString("spawns.default.world"));
                if (world == null) {
                    return null;
                }
                destLoc = new Location(world, spawnConfig.getDouble("spawns.default.x"),
                        spawnConfig.getDouble("spawns.default.y"), spawnConfig.getDouble("spawns.default.z"),
                        (float) spawnConfig.getDouble("spawns.default.yaw"),
                        (float) spawnConfig.getDouble("spawns.default.pitch"));
                return destLoc;
            }
            return player.getWorld().getSpawnLocation();
        } else {
            return player.getWorld().getSpawnLocation();
        }
    }

}
