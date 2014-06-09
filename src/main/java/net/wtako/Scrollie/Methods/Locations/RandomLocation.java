package net.wtako.Scrollie.Methods.Locations;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.LocationSource;
import net.wtako.Scrollie.Schedulers.PlayerPositionChecker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.mcore.ps.PS;

public class RandomLocation implements LocationSource {

    private Location                   destLoc;
    private final World                destWorld;
    private final Map<String, Integer> limits = new HashMap<String, Integer>();

    public RandomLocation(Player player, World destWorld) {
        this.destWorld = destWorld;
        limits.put("MinX", -1000);
        limits.put("MaxX", 1000);
        limits.put("MinY", 64);
        limits.put("MaxY", 80);
        limits.put("MinZ", -1000);
        limits.put("MaxZ", 1000);
        for (final Entry<String, Integer> entry: limits.entrySet()) {
            Integer value;
            if ((value = Main.getInstance().getConfig()
                    .getInt(MessageFormat.format("variable.use.Random.{0}.{1}", destWorld.getName(), entry.getKey()))) != 0) {
                limits.put(entry.getKey(), value);
            } else if ((value = Main.getInstance().getConfig()
                    .getInt(MessageFormat.format("variable.use.Random.{0}.{1}", "Default", entry.getKey()))) != 0) {
                limits.put(entry.getKey(), value);
            }
        }
    }

    @Override
    public Location get() {
        if (Main.getInstance().getConfig().getBoolean("variable.use.Random.UseAlternativeMethod")) {
            int loopCount = 0;
            do {
                loopCount++;
                destLoc = new Location(destWorld, RandomLocation.randInt(limits.get("MinX"), limits.get("MaxX")),
                        RandomLocation.randInt(limits.get("MinY"), limits.get("MaxY")), RandomLocation.randInt(
                                limits.get("MinZ"), limits.get("MaxZ")));
            } while ((locationIsInBadBiome(destLoc) || !PlayerPositionChecker.isSafeLocation(destLoc) || locationIsInFaction(destLoc))
                    || locationHasBadBlocksOnAnyY(destLoc) && loopCount <= 100);
            if (loopCount > 100) {
                return null;
            }
            return destLoc;
        }
        int loopCount = 0;
        do {
            loopCount++;
            destLoc = new Location(destWorld, RandomLocation.randInt(limits.get("MinX"), limits.get("MaxX")), 255,
                    RandomLocation.randInt(limits.get("MinZ"), limits.get("MaxZ")));
        } while ((locationIsInBadBiome(destLoc) || locationIsInFaction(destLoc) || locationHasBadBlocksOnAnyY(destLoc))
                && loopCount <= 20);
        if (loopCount > 20) {
            return null;
        }
        destLoc.setY(destWorld.getHighestBlockYAt(destLoc));
        return destLoc;
    }

    private boolean locationIsInFaction(Location loc) {
        if (Main.getInstance().getConfig().getBoolean("system.FactionSupport")) {
            try {
                return BoardColls.get().getFactionAt(PS.valueOf(loc)).isNone();
            } catch (final Error e) {
                return false;
            }
        }
        return false;
    }

    private boolean locationIsInBadBiome(Location loc) {
        final Biome biome = loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ());
        for (final String biomeName: Main.getInstance().getConfig().getStringList("variable.use.Random.NoTPBiomes")) {
            if (Biome.valueOf(biomeName.toUpperCase()) == biome) {
                return true;
            }
        }
        return false;
    }

    private boolean locationHasBadBlocksOnAnyY(Location loc) {
        final Location checkLocation = destLoc.clone();
        final List<String> noTPBlocks = Main.getInstance().getConfig()
                .getStringList("variable.use.Random.NoTPAnyYBlocks");
        for (int y = 0; y < 256; y++) {
            checkLocation.setY(y);
            final Block checkBlock = checkLocation.getBlock();
            for (final String blockName: noTPBlocks) {
                if (Material.getMaterial(blockName.toUpperCase()) == checkBlock.getType()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int randInt(int min, int max) {
        final Random rand = new Random();
        final int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
