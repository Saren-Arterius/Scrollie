package net.wtako.Scrollie.Methods;

import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class WorldGuardLocationChecker {

    public static boolean checkWorldGuard(Player player, Location location) {
        if (player.hasPermission(Main.getInstance().getProperty("artifactId") + ".canUseScrollInRestrictedAreas")) {
            return true;
        }
        try {
            final WorldGuardPlugin worldGuard = WorldGuardLocationChecker.getWorldGuard();
            final RegionManager regionManager = worldGuard.getRegionManager(location.getWorld());
            final ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitUtil.toVector(location));
            if (Main.getInstance().getConfig().getBoolean("variable.use.RestrictedAreas.WhiteList")) {
                return !set.allows(DefaultFlag.GRASS_SPREAD);
            }
            return set.allows(DefaultFlag.GRASS_SPREAD);
        } catch (final Error e) {
            player.sendMessage(MessageFormat.format(Lang.ERROR_HOOKING.toString(), "WorldGuard"));
            return false;
        }
    }

    private static WorldGuardPlugin getWorldGuard() {
        final Plugin plugin = Main.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

}
