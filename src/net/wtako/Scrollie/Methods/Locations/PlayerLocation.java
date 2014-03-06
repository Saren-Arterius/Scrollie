package net.wtako.Scrollie.Methods.Locations;

import java.io.File;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.LocationSource;
import net.wtako.Scrollie.Utils.FileTool;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerLocation implements LocationSource {

    private Location     destLoc;
    private final String targetName;
    private final Player player;

    public PlayerLocation(String targetName, Player player) {
        this.targetName = targetName;
        this.player = player;
    }

    @Override
    public Location get() {
        final Player target = Main.getInstance().getServer().getPlayer(targetName);
        if (target != null) {
            if (target.hasPermission("Scrollie.cantBeTeleportTarget")) {
                player.sendMessage(Lang.NOT_PREMITTED_TO_TELEPORT_TO_THAT_PLAYER.toString());
                return null;
            }
            return target.getLocation();
        }
        player.sendMessage(Lang.TARGET_PLAYER_IS_OFFLINE.toString());
        if (Main.getInstance().getConfig().getBoolean("variable.use.Player.AllowOfflineTP")) {
            final File configFile = FileTool.getChildFile(
                    FileTool.getChildFile(FileTool.getChildFile(Main.getInstance().getDataFolder().getParentFile()
                            .getAbsoluteFile(), "Essentials", false), "userdata", false), targetName + ".yml", true);
            if (configFile != null) {
                final YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                final World world = Main.getInstance().getServer().getWorld(config.getString("logoutlocation.world"));
                if (world == null) {
                    player.sendMessage(Lang.CANT_FIND_OFFLINE_PLAYER_LOCATION.toString());
                    return null;
                }
                player.sendMessage(Lang.WILL_TP_TO_OFFLINE_PLAYER_LOCATION.toString());
                destLoc = new Location(world, config.getDouble("logoutlocation.x"),
                        config.getDouble("logoutlocation.y"), config.getDouble("logoutlocation.z"),
                        (float) config.getDouble("logoutlocation.z"), (float) config.getDouble("logoutlocation.pitch"));
                return destLoc;
            }
        }
        return null;
    }

}
