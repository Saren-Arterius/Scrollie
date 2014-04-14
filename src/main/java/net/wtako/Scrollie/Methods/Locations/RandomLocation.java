package net.wtako.Scrollie.Methods.Locations;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.LocationSource;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

public class RandomLocation implements LocationSource {

    private final Player               player;
    private Location                   destLoc;
    private final World                destWorld;
    private final Map<String, Integer> limits = new HashMap<String, Integer>();

    public RandomLocation(Player player, World destWorld) {
        this.destWorld = destWorld;
        this.player = player;
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
            } while (!locationIsAir(destLoc) || locationIsInFaction(destLoc) || loopCount <= 100);
            destLoc.setY(destWorld.getHighestBlockYAt(destLoc));
            return destLoc;
        } else {
            do {
                destLoc = new Location(destWorld, RandomLocation.randInt(limits.get("MinX"), limits.get("MaxX")),
                        RandomLocation.randInt(limits.get("MinY"), limits.get("MaxY")), RandomLocation.randInt(
                                limits.get("MinZ"), limits.get("MaxZ")));
            } while (locationIsInFaction(destLoc));
            destLoc.setY(destWorld.getHighestBlockYAt(destLoc));
            return destLoc;
        }
    }

    private boolean locationIsAir(Location loc) {
        if (loc.getBlock().getType().equals(Material.AIR) && loc.add(0, 1, 0).getBlock().getType().equals(Material.AIR)
                && loc.subtract(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
            Main.log.info("A");
            return true;
        }
        Main.log.info("B");
        return false;
    }

    private boolean locationIsInFaction(Location loc) {
        if (Main.getInstance().getConfig().getBoolean("system.FactionSupport")) {
            try {
                final UPlayer factionPlayer = UPlayer.get(player);
                if (BoardColls.get().getFactionAt(PS.valueOf(loc)) == FactionColls.get()
                        .getForUniverse(factionPlayer.getUniverse()).getNone()) {
                    return false;
                } else {
                    return true;
                }
            } catch (final Error e) {
                return false;
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
