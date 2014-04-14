package net.wtako.Scrollie.Methods.Locations;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Database;
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
        if (PlayerLocation.hasTargetTurnedOffTP(targetName)) {
            player.sendMessage(MessageFormat.format(Lang.TARGET_HAS_TURNED_TP_OFF.toString(), targetName));
            return null;
        }
        final Player target = Main.getInstance().getServer().getPlayer(targetName);
        if (target != null) {
            if (target.hasPermission(Main.getInstance().getProperty("artifactId") + ".cantBeTeleportTarget")) {
                player.sendMessage(Lang.NOT_PREMITTED_TO_TELEPORT_TO_THAT_PLAYER.toString());
                return null;
            }
            return target.getLocation();
        }
        player.sendMessage(MessageFormat.format(Lang.TARGET_PLAYER_IS_OFFLINE.toString(), targetName));
        if (Main.getInstance().getConfig().getBoolean("variable.use.Player.AllowOfflineTP")
                && Main.getInstance().getConfig().getBoolean("system.EssentialsSupport")) {
            final File configFile = FileTool.getChildFile(
                    FileTool.getChildFile(FileTool.getChildFile(Main.getInstance().getDataFolder().getParentFile()
                            .getAbsoluteFile(), "Essentials", false), "userdata", false), targetName + ".yml", true);
            if (configFile != null) {
                final YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                final World world = Main.getInstance().getServer().getWorld(config.getString("logoutlocation.world"));
                if (world == null) {
                    player.sendMessage(MessageFormat.format(Lang.CANT_FIND_OFFLINE_PLAYER_LOCATION.toString(),
                            targetName));
                    return null;
                }
                player.sendMessage(MessageFormat.format(Lang.WILL_TP_TO_OFFLINE_PLAYER_LOCATION.toString(), targetName));
                destLoc = new Location(world, config.getDouble("logoutlocation.x"),
                        config.getDouble("logoutlocation.y"), config.getDouble("logoutlocation.z"),
                        (float) config.getDouble("logoutlocation.z"), (float) config.getDouble("logoutlocation.pitch"));
                return destLoc;
            }
        }
        return null;
    }

    public static boolean hasTargetTurnedOffTP(String targetName) {
        boolean targetHasTurnedOffTP = false;
        try {
            final PreparedStatement selStmt = Database.getInstance().conn
                    .prepareStatement("SELECT * FROM `tp_denies` WHERE player = ?");
            selStmt.setString(1, targetName);
            final ResultSet result = selStmt.executeQuery();
            if (result.next()) {
                targetHasTurnedOffTP = true;
            }
            result.close();
            selStmt.close();
            return targetHasTurnedOffTP;
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return targetHasTurnedOffTP;
    }

}
