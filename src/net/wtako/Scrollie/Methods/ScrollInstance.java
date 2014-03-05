package net.wtako.Scrollie.Methods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.wtako.Scrollie.Utils.Database;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public ScrollInstance(ItemStack item) throws SQLException {
        super();
        this.instanceID = ScrollInstance.getScrollInstanceID(item);
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

    public boolean doPreActions(Player player) {
        if (instanceID == null) {
            return false;
        }
        return true;
        // TODO Auto-generated method stub

    }

    public void doPostActions(Player player) throws SQLException {

        /*
         * if (getTimesRemaining() == 1) {
         * final PreparedStatement delStmt = Database.getInstance().conn
         * .prepareStatement("DELETE FROM 'scrolls' WHERE rowid = ?");
         * delStmt.setInt(1, instanceID);
         * delStmt.execute();
         * delStmt.close();
         * } else {
         * final PreparedStatement updateStmt = Database.getInstance().conn
         * .prepareStatement(
         * "UPDATE 'scrolls' SET times_remaining = ? WHERE rowid = ?");
         * updateStmt.setInt(1, getTimesRemaining() - 1);
         * updateStmt.setInt(2, instanceID);
         * updateStmt.execute();
         * updateStmt.close();
         * }
         */
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
}
