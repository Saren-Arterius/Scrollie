package net.wtako.Scrollie.Methods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Commands.Make.MakeProcess;
import net.wtako.Scrollie.Methods.Locations.FactionHomeLocation;
import net.wtako.Scrollie.Methods.Locations.HomeLocation;
import net.wtako.Scrollie.Methods.Locations.PlayerLocation;
import net.wtako.Scrollie.Methods.Locations.RandomLocation;
import net.wtako.Scrollie.Methods.Locations.SpawnLocation;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ScrollInstance extends Database {

    public static String  lorePattern = "{0}: {1}";
    private final Integer instanceID;
    private Integer       destinationType;
    private Integer       timesRemaining;
    private Integer       warmUpTime;
    private Integer       coolDownTime;
    private Integer       destX;
    private Integer       destY;
    private Integer       destZ;
    private Boolean       allowCrossWorldTP;
    private String        destWorld;
    private String        targetName;
    private ItemStack     item;
    private Location      destLoc;

    public ScrollInstance(ItemStack item) throws SQLException {
        super();
        instanceID = ScrollInstance.getScrollInstanceID(item);
        this.item = item;
        if (instanceID != null) {
            loadInfoFromDB();
        }
    }

    public ScrollInstance(Integer instanceID) throws SQLException {
        super();
        this.instanceID = instanceID;
        loadInfoFromDB();
    }

    public List<String> getLores() throws SQLException {
        final List<String> lore = new ArrayList<String>();

        lore.add(MessageFormat.format(ScrollInstance.lorePattern, "Scrollie ID", instanceID));
        lore.add(MessageFormat.format(ScrollInstance.lorePattern, Lang.DESTINATION_TYPE,
                ScrollDatabase.destinationTypeIntegerToString(getDestinationType())));
        if (targetName != null) {
            lore.add(MessageFormat.format(ScrollInstance.lorePattern, Lang.TARGET_PLAYER.toString(), targetName));
        }
        if (destX != null && destY != null && destZ != null && destWorld != null) {
            final String locationRepr = MessageFormat.format("<{0}: {1} - X: {2}, Y: {3}, Z: {4}>",
                    Lang.WORLD.toString(), destWorld, destX, destY, destZ);
            lore.add(MessageFormat.format(ScrollInstance.lorePattern, Lang.TARGET_LOCATION.toString(), ""));
            lore.add(locationRepr);
        }
        lore.add(MessageFormat.format(ScrollInstance.lorePattern, Lang.WARM_UP_TIME.toString(), warmUpTime + " "
                + Lang.SECONDS));
        lore.add(MessageFormat.format(ScrollInstance.lorePattern, Lang.COOL_DOWN_TIME.toString(), coolDownTime + " "
                + Lang.SECONDS));
        if (allowCrossWorldTP) {
            lore.add(MessageFormat.format(ScrollInstance.lorePattern, Lang.CROSS_WORLD_TP.toString(), Lang.ALLOWED));
        } else {
            lore.add(MessageFormat.format(ScrollInstance.lorePattern, Lang.CROSS_WORLD_TP.toString(), Lang.NOT_ALLOWED));
        }
        lore.add(MessageFormat.format(ScrollInstance.lorePattern, Lang.TIMES_REMAINING.toString(), getTimesRemaining()));
        return lore;
    }

    public static Integer getScrollInstanceID(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return null;
        }
        try {
            final String IDRow = item.getItemMeta().getLore().get(0);
            final String regex = "^" + MessageFormat.format(ScrollInstance.lorePattern, "Scrollie ID", "(\\d+)") + "$";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(IDRow);
            matcher.find();
            return Integer.parseInt(matcher.group(1));
        } catch (final Exception e) {
            return null;
        }
    }

    private void loadInfoFromDB() throws SQLException {
        final PreparedStatement selStmt = Database.getInstance().conn
                .prepareStatement("SELECT * FROM 'scrolls' WHERE rowid = ?");
        selStmt.setInt(1, instanceID);
        final ResultSet result = selStmt.executeQuery();

        destinationType = result.getInt(2);
        targetName = result.getString(3);
        destX = result.getInt(4);
        destY = result.getInt(5);
        destZ = result.getInt(6);
        destWorld = result.getString(7);
        warmUpTime = result.getInt(8);
        coolDownTime = result.getInt(9);
        if (result.getInt(10) == 1) {
            allowCrossWorldTP = true;
        } else {
            allowCrossWorldTP = false;
        }
        timesRemaining = result.getInt(11);
        result.close();
        selStmt.close();
    }

    public static int saveScrollInstance(MakeProcess proc) throws SQLException {
        final PreparedStatement selStmt = Database.getInstance().conn
                .prepareStatement("SELECT max(rowid) FROM 'scrolls'");
        final int rowid = selStmt.executeQuery().getInt(1) + 1;
        selStmt.close();

        final PreparedStatement insStmt = Database.getInstance().conn
                .prepareStatement("INSERT INTO `scrolls` (`rowid`, `scroll_destination`, `target_player`, `destination_x`, `destination_y`, `destination_z`, `destination_world`, `warm_up_time`, `cool_down_time`, `allow_cross_world_tp`, `times_remaining`, `timestamp`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insStmt.setInt(1, rowid);
        insStmt.setInt(2, proc.getDestinationType());
        insStmt.setInt(8, proc.getWarmUpTime());
        insStmt.setInt(9, proc.getCoolDownTime());
        insStmt.setInt(11, proc.getTimesBeUsed());
        insStmt.setInt(12, (int) (System.currentTimeMillis() / 1000L));
        if (proc.getAllowCrossWorldTP()) {
            insStmt.setInt(10, 1);
        } else {
            insStmt.setInt(10, 0);
        }
        if (proc.targetName != null) {
            insStmt.setString(3, proc.targetName);
        }
        if (proc.getDestX() != null) {
            insStmt.setInt(4, proc.getDestX());
        }
        if (proc.getDestY() != null) {
            insStmt.setInt(5, proc.getDestY());
        }
        if (proc.getDestZ() != null) {
            insStmt.setInt(6, proc.getDestZ());
        }
        if (proc.getDestWorld() != null) {
            insStmt.setString(7, proc.getDestWorld());
        }
        insStmt.execute();
        insStmt.close();
        return rowid;
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
        if (!player.hasPermission("Scrollie.overrideWUCD") && !checkCoolDownTime(player)) {
            return false;
        }
        if (Main.getInstance().getConfig().getBoolean("variable.make.BanCreative")
                && ((HumanEntity) player).getGameMode() == GameMode.CREATIVE
                && !player.hasPermission("Scrollie.noCostRequiredToMake")) {
            return false;
        }
        if ((destLoc = getLocation(player)) == null) {
            return false;
        }
        if (!player.hasPermission("Scrollie.overrideWUCD") && !allowCrossWorldTP
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
        if (timesRemaining == 0) {
            final PreparedStatement delStmt = Database.getInstance().conn
                    .prepareStatement("DELETE FROM 'scrolls' WHERE rowid = ?");
            delStmt.setInt(1, instanceID);
            delStmt.execute();
            delStmt.close();
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
            final PreparedStatement updateStmt = Database.getInstance().conn
                    .prepareStatement("UPDATE 'scrolls' SET times_remaining = ? WHERE rowid = ?");
            updateStmt.setInt(1, timesRemaining);
            updateStmt.setInt(2, instanceID);
            updateStmt.execute();
            updateStmt.close();
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

    public ItemStack getItem() {
        return item;
    }

    public Location getLocation() {
        return destLoc;
    }

    public Integer getScrollInstanceID() {
        return instanceID;
    }

    public String getTargetName() {
        return targetName;
    }
}
