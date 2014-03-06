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

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.mcore.ps.PS;

public class RandomLocation implements LocationSource {

    private Location             destLoc;
    private World                destWorld;
    private Map<String, Integer> limits = new HashMap<String, Integer>();

    public RandomLocation(World destWorld) {
        this.destWorld = destWorld;
        limits.put("MinX", -1000);
        limits.put("MaxX", 1000);
        limits.put("MinY", 64);
        limits.put("MaxY", 80);
        limits.put("MinZ", -1000);
        limits.put("MaxZ", 1000);
        for (Entry<String, Integer> entry: limits.entrySet()) {
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
                destLoc = new Location(destWorld, randInt(limits.get("MinX"), limits.get("MaxX")), randInt(
                        limits.get("MinY"), limits.get("MaxY")), randInt(limits.get("MinZ"), limits.get("MaxZ")));
            } while (!locationIsAir(destLoc) || locationIsInFaction(destLoc) || loopCount <= 100);
            destLoc.setY(destWorld.getHighestBlockYAt(destLoc));
            return destLoc;
        } else {
            do {
                destLoc = new Location(destWorld, randInt(limits.get("MinX"), limits.get("MaxX")), randInt(
                        limits.get("MinY"), limits.get("MaxY")), randInt(limits.get("MinZ"), limits.get("MaxZ")));
            } while (locationIsInFaction(destLoc));
            destLoc.setY(destWorld.getHighestBlockYAt(destLoc));
            return destLoc;
        }
    }

    public static boolean locationIsAir(Location loc) {
        if (loc.getBlock().getType().equals(Material.AIR) && loc.add(0, 1, 0).getBlock().getType().equals(Material.AIR)
                && loc.subtract(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
            Main.log.info("A");
            return true;
        }
        Main.log.info("B");
        return false;
    }

    public static boolean locationIsInFaction(Location loc) {
        try {
            if (BoardColls.get().getFactionAt(PS.valueOf(loc)) != null) {
                return true;
            }
            return false;
        } catch (Error e) {
            return false;
        }
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
