package net.wtako.Scrollie.Methods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Commands.Make.MakeProcess;
import net.wtako.Scrollie.Methods.Locations.FactionHomeLocation;
import net.wtako.Scrollie.Methods.Locations.HomeLocation;
import net.wtako.Scrollie.Methods.Locations.PlayerLocation;
import net.wtako.Scrollie.Methods.Locations.RandomLocation;
import net.wtako.Scrollie.Methods.Locations.SpawnLocation;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ScrollInstance extends Database {

    public String     lorePattern = Lang.LORE_PATTERN.toString();
    private Integer   destinationType;
    private Integer   timesRemaining;
    private Integer   warmUpTime;
    private Integer   coolDownTime;
    private Integer   destX;
    private Integer   destY;
    private Integer   destZ;
    private Boolean   allowCrossWorldTP;
    private String    destWorld;
    private String    targetName;
    private ItemStack item;
    private Location  destLoc;

    public ScrollInstance(ItemStack item) throws SQLException {
        this.item = item;
        loadProperties(this.item);
    }

    public ScrollInstance(MakeProcess makeProcess) throws SQLException {
        loadProperties(makeProcess);
    }

    public static String toInvisible(String s) {
        String hidden = "";
        for (final char c: s.toCharArray()) {
            hidden += ChatColor.COLOR_CHAR + "" + c;
        }
        return hidden;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isValid(ItemStack item) {
        try {
            if (item.getType() == Material.AIR) {
                return false;
            }

            if (!item.hasItemMeta()) {
                return false;
            }

            final List<String> itemLore = item.getItemMeta().getLore();

            if (itemLore.size() < 6) {
                return false;
            }

            final Map scrollProperties = (Map) JSONValue.parse(item.getItemMeta().getLore()
                    .get(item.getItemMeta().getLore().size() - 2).replaceAll("§", ""));
            if (((Long) scrollProperties.get("destinationType")).intValue() < 0) {
                return false;
            }
        } catch (final Exception e) {
            return false;
        }
        return true;
    }

    public ItemStack getScrollItem(String name) throws SQLException {
        final String itemTypeRequiredString = Main.getInstance().getConfig().getString("variable.make.ScrollItem");
        final Material itemTypeRequired = Material.getMaterial(itemTypeRequiredString.toUpperCase());
        final ItemStack scrollItem = new ItemStack(itemTypeRequired, 1);
        final ItemMeta meta = scrollItem.getItemMeta();
        meta.setLore(getLores());
        meta.setDisplayName(name);
        scrollItem.setItemMeta(meta);
        scrollItem.addUnsafeEnchantment(Enchantment.LUCK, 1);
        return scrollItem;
    }

    public List<String> getLores() throws SQLException {
        final List<String> lore = new ArrayList<String>();

        lore.add(MessageFormat.format(lorePattern, Lang.DESTINATION_TYPE,
                ScrollDatabase.destinationTypeIntegerToString(getDestinationType())));
        if (targetName != null) {
            lore.add(MessageFormat.format(lorePattern, Lang.TARGET_PLAYER.toString(), targetName));
        }
        if (destX != null && destY != null && destZ != null && destWorld != null) {
            final String locationRepr = MessageFormat.format("<{0}: {1} - X: {2}, Y: {3}, Z: {4}>",
                    Lang.WORLD.toString(), destWorld, destX, destY, destZ);
            lore.add(MessageFormat.format(lorePattern, Lang.TARGET_LOCATION.toString(), ""));
            lore.add(locationRepr);
        }
        lore.add(MessageFormat.format(lorePattern, Lang.WARM_UP_TIME.toString(), warmUpTime + " " + Lang.SECONDS));
        lore.add(MessageFormat.format(lorePattern, Lang.COOL_DOWN_TIME.toString(), coolDownTime + " " + Lang.SECONDS));
        if (allowCrossWorldTP) {
            lore.add(MessageFormat.format(lorePattern, Lang.CROSS_WORLD_TP.toString(), Lang.ALLOWED));
        } else {
            lore.add(MessageFormat.format(lorePattern, Lang.CROSS_WORLD_TP.toString(), Lang.NOT_ALLOWED));
        }
        lore.add(ScrollInstance.toInvisible(getJSONFromProperties()));
        lore.add(MessageFormat.format(lorePattern, Lang.TIMES_REMAINING.toString(), getTimesRemaining()));
        return lore;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getJSONFromProperties() {
        final Map scrollProperties = new HashMap();
        scrollProperties.put("destinationType", destinationType);
        scrollProperties.put("warmUpTime", warmUpTime);
        scrollProperties.put("coolDownTime", coolDownTime);
        scrollProperties.put("timesRemaining", timesRemaining);
        if (allowCrossWorldTP) {
            scrollProperties.put("allowCrossWorldTP", 1);
        } else {
            scrollProperties.put("allowCrossWorldTP", 0);
        }
        if (destX != null) {
            scrollProperties.put("destX", destX);
            scrollProperties.put("destY", destY);
            scrollProperties.put("destZ", destZ);
            scrollProperties.put("destWorld", destWorld);
        }
        if (targetName != null) {
            scrollProperties.put("targetName", targetName);
        }
        scrollProperties.put("random", Math.random());
        scrollProperties.put("createTime", System.currentTimeMillis());
        return JSONObject.toJSONString(scrollProperties);
    }

    @SuppressWarnings({"rawtypes"})
    private void loadProperties(ItemStack item) {
        final Map scrollProperties = (Map) JSONValue.parse(item.getItemMeta().getLore()
                .get(item.getItemMeta().getLore().size() - 2).replaceAll("§", ""));
        destinationType = ((Long) scrollProperties.get("destinationType")).intValue();
        warmUpTime = ((Long) scrollProperties.get("warmUpTime")).intValue();
        coolDownTime = ((Long) scrollProperties.get("coolDownTime")).intValue();
        timesRemaining = ((Long) scrollProperties.get("timesRemaining")).intValue();
        if (((Long) scrollProperties.get("allowCrossWorldTP")).intValue() == 1) {
            allowCrossWorldTP = true;
        } else {
            allowCrossWorldTP = false;
        }
        if (scrollProperties.get("destX") != null) {
            destX = ((Long) scrollProperties.get("destX")).intValue();
            destY = ((Long) scrollProperties.get("destY")).intValue();
            destZ = ((Long) scrollProperties.get("destZ")).intValue();
            destWorld = (String) scrollProperties.get("destWorld");
        }
        if (scrollProperties.get("targetName") != null) {
            targetName = (String) scrollProperties.get("targetName");
        }

    }

    private void loadProperties(MakeProcess proc) {
        destinationType = proc.getDestinationType();
        warmUpTime = proc.getWarmUpTime();
        coolDownTime = proc.getCoolDownTime();
        timesRemaining = proc.getTimesBeUsed();
        if (proc.getAllowCrossWorldTP()) {
            allowCrossWorldTP = true;
        } else {
            allowCrossWorldTP = false;
        }
        if (proc.targetName != null) {
            targetName = proc.targetName;
        }
        if (proc.getDestX() != null) {
            destX = proc.getDestX();
        }
        if (proc.getDestY() != null) {
            destY = proc.getDestY();
        }
        if (proc.getDestZ() != null) {
            destZ = proc.getDestZ();
        }
        if (proc.getDestWorld() != null) {
            destWorld = proc.getDestWorld();
        }
    }

    private boolean checkCoolDownTime(Player player) throws SQLException {
        Boolean notInCoolDown = null;
        Integer until;
        final PreparedStatement selStmt = Database.getInstance().conn
                .prepareStatement("SELECT * FROM 'cooldowns' WHERE player = ?");
        selStmt.setString(1, player.getName().toLowerCase());
        final ResultSet result = selStmt.executeQuery();
        if (!result.next()) {
            notInCoolDown = true;
        } else {
            if (destinationType == 6) {
                until = result.getInt(3);
            } else {
                until = result.getInt(2);
            }
            if (until == null) {
                notInCoolDown = true;
            } else {
                final int unixTime = (int) (System.currentTimeMillis() / 1000L);
                if (unixTime < until) {
                    player.sendMessage(MessageFormat.format(Lang.COOLING_DOWN.toString(), until - unixTime));
                    notInCoolDown = false;
                } else {
                    notInCoolDown = true;
                }
            }
        }
        result.close();
        selStmt.close();
        return notInCoolDown;
    }

    private Location getLocation(Player player) {
        Location destLoc = null;
        switch (destinationType) {
            case 0:
                destLoc = new SpawnLocation(player).get();
                break;
            case 1:
                destLoc = new HomeLocation(player).get();
                break;
            case 2:
                destLoc = new FactionHomeLocation(player).get();
                break;
            case 3:
                destLoc = new PlayerLocation(targetName, player).get();
                break;
            case 4:
                destLoc = new Location(Main.getInstance().getServer().getWorld(destWorld), destX, destY, destZ);
                break;
            case 5:
                destLoc = new RandomLocation(player, player.getWorld()).get();
                break;
            case 6:
                destLoc = new SpawnLocation(player).get();
                break;
        }
        return destLoc;
    }

    public boolean doPreActions(Player player) throws SQLException {
        if (!player.hasPermission(Main.getInstance().getProperty("artifactId") + ".overrideWUCD")
                && !checkCoolDownTime(player)) {
            return false;
        }
        if (Main.getInstance().getConfig().getBoolean("variable.make.BanCreative")
                && ((HumanEntity) player).getGameMode() == GameMode.CREATIVE
                && !player.hasPermission(Main.getInstance().getProperty("artifactId") + ".noCostRequiredToMake")) {
            return false;
        }
        if ((destLoc = getLocation(player)) == null) {
            return false;
        }
        if (!player.hasPermission(Main.getInstance().getProperty("artifactId") + ".overrideWUCD") && !allowCrossWorldTP
                && !destLoc.getWorld().equals(player.getWorld())) {
            player.sendMessage(MessageFormat.format(Lang.NOT_POWERFUL_ENOUGH_CROSS_WORLD_TP.toString(), item
                    .getItemMeta().getDisplayName()));
            return false;
        }
        if (Main.getInstance().getConfig().getBoolean("system.FactionsSupport")
                && !FactionLocationChecker.checkFaction(player, destLoc)) {
            return false;
        }
        if (Main.getInstance().getConfig().getBoolean("system.WorldGuardSupport")) {
            if (!WorldGuardLocationChecker.checkWorldGuard(player, player.getLocation())) {
                player.sendMessage(Lang.SOURCE_LOCATION_NOT_ALLOWED.toString());
                return false;
            }
            if (!WorldGuardLocationChecker.checkWorldGuard(player, destLoc)) {
                player.sendMessage(Lang.TARGET_LOCATION_NOT_ALLOWED.toString());
                return false;
            }
        }

        if (item.getAmount() > 1 && player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Lang.YOUR_INVENTORY_IS_FULL.toString());
            return false;
        }

        return true;
    }

    private void updateCoolDown(Player player) throws SQLException {
        final int unixTime = (int) (System.currentTimeMillis() / 1000L);
        final PreparedStatement selStmt = Database.getInstance().conn
                .prepareStatement("SELECT * FROM 'cooldowns' WHERE player = ?");
        selStmt.setString(1, player.getName().toLowerCase());
        final ResultSet result = selStmt.executeQuery();
        if (!result.next()) {
            final PreparedStatement insStmt = Database.getInstance().conn
                    .prepareStatement("INSERT INTO 'cooldowns' VALUES (?, ?, ?)");
            insStmt.setString(1, player.getName().toLowerCase());
            if (destinationType != 6) {
                insStmt.setInt(2, unixTime + coolDownTime);
            } else {
                insStmt.setInt(3, unixTime + coolDownTime);
            }
            insStmt.execute();
            insStmt.close();
        } else {
            PreparedStatement updateStmt;
            if (destinationType != 6) {
                updateStmt = Database.getInstance().conn
                        .prepareStatement("UPDATE 'cooldowns' SET normal_until = ? WHERE player = ?");
            } else {
                updateStmt = Database.getInstance().conn
                        .prepareStatement("UPDATE 'cooldowns' SET rescue_until = ? WHERE player = ?");
            }
            updateStmt.setInt(1, unixTime + coolDownTime);
            updateStmt.setString(2, player.getName().toLowerCase());
            updateStmt.execute();
            updateStmt.close();
        }
        result.close();
        selStmt.close();
    }

    public void updateRemainingTimes(Player player) throws SQLException {
        timesRemaining--;
        player.getInventory().remove(item);
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().addItem(item);
            item.setAmount(1);
        }
        if (timesRemaining == 0) {
            if (Main.getInstance().getConfig().getBoolean("variable.use.DestroyItemOnNoRemainingTimes")) {
                player.sendMessage(MessageFormat.format(Lang.SCROLL_DISAPPEARED.toString(), item.getItemMeta()
                        .getDisplayName()));
            } else {
                final String itemTypeString = Main.getInstance().getConfig().getString("variable.make.ScrollItem");
                final Material itemType = Material.getMaterial(itemTypeString.toUpperCase());
                player.getInventory().addItem(new ItemStack(itemType, 1));
                player.sendMessage(MessageFormat.format(Lang.SCROLL_MAGIC_DISAPPEARED.toString(), item.getItemMeta()
                        .getDisplayName()));
            }
        } else {
            final ItemMeta meta = item.getItemMeta();
            meta.setLore(getLores());
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
            player.sendMessage(MessageFormat.format(Lang.SCROLL_MAGIC_GETTING_WEAKER.toString(), item.getItemMeta()
                    .getDisplayName()));
        }
    }

    public void doPostActions(Player player) throws SQLException {
        updateCoolDown(player);
        updateRemainingTimes(player);
    }

    public Integer getDestinationType() {
        return destinationType;
    }

    public Integer getTimesRemaining() {
        return timesRemaining;
    }

    public Integer getWarmUpTime() {
        return warmUpTime;
    }

    public Location getLocation() {
        return destLoc;
    }

    public String getTargetName() {
        return targetName;
    }

}
