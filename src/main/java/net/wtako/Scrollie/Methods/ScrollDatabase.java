package net.wtako.Scrollie.Methods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;

public class ScrollDatabase extends Database {

    private final Player owner;
    private Integer      destinationType;
    private Integer      warmUpTime;
    private Integer      coolDownTime;
    private Boolean      allowCrossWorldTP;
    private Integer      timesBeUsed;
    private String       name;

    public ScrollDatabase(Player player) throws SQLException {
        owner = player;
        setDefaultValue();
    }

    public ScrollDatabase(Player player, Integer destinationType, Integer warmUpTime, Integer coolDownTime,
            Boolean allowCrossWorldTP, Integer timesBeUsed, String name) throws SQLException {
        owner = player;
        this.destinationType = destinationType;
        this.warmUpTime = warmUpTime;
        this.coolDownTime = coolDownTime;
        this.allowCrossWorldTP = allowCrossWorldTP;
        this.timesBeUsed = timesBeUsed;
        this.name = name;
    }

    private void setDefaultValue() {
        if (!Main.getInstance().getConfig().getBoolean("variable.create.ScrollDestination.CanCustomize")
                && !owner.hasPermission(Main.getInstance().getProperty("artifactId")
                        + ".overrideCanCustomize.ScrollDestination")) {
            setDestinationType(Main.getInstance().getConfig().getString("variable.create.ScrollDestination.Default"),
                    false);
        }
        if (!Main.getInstance().getConfig().getBoolean("variable.create.WarmUpTime.CanCustomize")
                && !owner.hasPermission(Main.getInstance().getProperty("artifactId")
                        + ".overrideCanCustomize.WarmUpTime")) {
            setWarmUpTime(Main.getInstance().getConfig().getString("variable.create.WarmUpTime.Default"), false);
        }
        if (!Main.getInstance().getConfig().getBoolean("variable.create.CoolDownTime.CanCustomize")
                && !owner.hasPermission(Main.getInstance().getProperty("artifactId")
                        + ".overrideCanCustomize.CoolDownTime")) {
            setCoolDownTime(Main.getInstance().getConfig().getString("variable.create.CoolDownTime.Default"), false);
        }
        if (!Main.getInstance().getConfig().getBoolean("variable.create.CrossWorldTP.CanCustomize")
                && !owner.hasPermission(Main.getInstance().getProperty("artifactId")
                        + ".overrideCanCustomize.CrossWorldTP")) {
            setAllowCrossWorldTP(Main.getInstance().getConfig().getString("variable.create.CrossWorldTP.Default"),
                    false);
        }
        if (!Main.getInstance().getConfig().getBoolean("variable.create.TimesBeUsed.CanCustomize")
                && !owner.hasPermission(Main.getInstance().getProperty("artifactId")
                        + ".overrideCanCustomize.TimesBeUsed")) {
            setTimesBeUsed(Main.getInstance().getConfig().getString("variable.create.TimesBeUsed.Default"), false);
        }
        return;
    }

    public String[] wrongConfigValue(String node, String expected, Integer got, Integer fallback) {
        final String msg1 = MessageFormat.format(Lang.WRONG_VALUE.toString(), node);
        final String msg2 = MessageFormat.format(Lang.EXPECTED_GOT.toString(), expected, got);
        final String msg3 = MessageFormat.format(Lang.FALLING_BACK_TO.toString(), fallback);
        final String[] finalMessage = {msg1, msg2, msg3};
        return finalMessage;
    }

    public String[] enterAgain(String expected, String got) {
        final String msg1 = Lang.ENTER_AGAIN.toString();
        final String msg2 = MessageFormat.format(Lang.EXPECTED_GOT.toString(), expected, got);
        final String[] finalMessage = {msg1, msg2};
        return finalMessage;
    }

    public String[] success(String key, String value) {
        final String msg1 = MessageFormat.format(Lang.VALUE_SET.toString(), getEXPRequired());
        final String msg2 = MessageFormat.format(Lang.KEY_VALUE.toString(), key, value);
        final String[] finalMessage = {msg1, msg2};
        return finalMessage;
    }

    @SuppressWarnings("deprecation")
    public static String[] listAll(Player player) throws SQLException {
        final String pattern1 = Lang.LIST_PATTERN1.toString();
        final String pattern2 = Lang.LIST_PATTERN2.toString();
        boolean playerHasNoScrolls = true;
        final PreparedStatement selStmt = Database.getInstance().conn
                .prepareStatement("SELECT * FROM 'scroll_creations' WHERE owner = ?");
        selStmt.setString(1, player.getName().toLowerCase());
        final ResultSet result = selStmt.executeQuery();
        final List<String> tableArrayList = new ArrayList<String>();
        tableArrayList.add(Lang.SCROLL_LIST.toString());
        while (result.next()) {
            playerHasNoScrolls = false;
            Boolean crossWorldTP;
            if (result.getInt(8) == 1) {
                crossWorldTP = true;
            } else {
                crossWorldTP = false;
            }
            final ScrollDatabase scrollInTable = new ScrollDatabase(Main.getInstance().getServer()
                    .getPlayer(result.getString(2)), result.getInt(5), result.getInt(6), result.getInt(7),
                    crossWorldTP, result.getInt(9), result.getString(4));
            tableArrayList.add(MessageFormat.format(pattern1, result.getInt(3), result.getString(4)));
            tableArrayList.add(MessageFormat.format(pattern2,
                    ScrollDatabase.destinationTypeIntegerToString(result.getInt(5)), result.getInt(6),
                    result.getInt(7), ScrollDatabase.getAllowCrossWorldTPRepr(result.getInt(8)), result.getInt(9),
                    scrollInTable.getEXPRequired()));
        }
        result.close();
        selStmt.close();
        if (playerHasNoScrolls) {
            tableArrayList.add(Lang.YOU_DONT_HAVE_ANY_CREATIONS.toString());
            tableArrayList.add(Lang.HELP_CREATE.toString());
            tableArrayList.remove(0);
        }
        final String[] finalMessage = new String[tableArrayList.size()];
        return tableArrayList.toArray(finalMessage);
    }

    public static String delete(int rowID, Player player) throws SQLException {
        final PreparedStatement selStmt = Database.getInstance().conn
                .prepareStatement("SELECT name FROM 'scroll_creations' WHERE owner = ? AND scroll_id = ?");
        selStmt.setString(1, player.getName().toLowerCase());
        selStmt.setInt(2, rowID);
        final ResultSet result = selStmt.executeQuery();
        if (!result.next()) {
            result.close();
            selStmt.close();
            return MessageFormat.format(Lang.NO_SUCH_SCROLL.toString(), rowID);
        }
        final String scrollName = result.getString(1);
        result.close();
        selStmt.close();
        final PreparedStatement delStmt = Database.getInstance().conn
                .prepareStatement("DELETE FROM 'scroll_creations' WHERE owner = ? AND scroll_id = ?");
        delStmt.setString(1, player.getName().toLowerCase());
        delStmt.setInt(2, rowID);
        delStmt.execute();
        delStmt.close();
        return MessageFormat.format(Lang.FINISHED_DELETING.toString(), scrollName);
    }

    public String[] save() throws SQLException {
        final PreparedStatement selStmt = conn
                .prepareStatement("SELECT max(scroll_id) FROM 'scroll_creations' WHERE owner = ?");
        selStmt.setString(1, owner.getName().toLowerCase());
        final int scrollID = selStmt.executeQuery().getInt(1) + 1;
        selStmt.close();

        final PreparedStatement insStmt = conn
                .prepareStatement("INSERT INTO `scroll_creations` (`owner`, `scroll_id`, `name`, `scroll_destination`, `warm_up_time`, `cool_down_time`, `allow_cross_world_tp`, `times_be_used`, `timestamp`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insStmt.setString(1, owner.getName().toLowerCase());
        insStmt.setInt(2, scrollID);
        insStmt.setString(3, name);
        insStmt.setInt(4, destinationType);
        insStmt.setInt(5, warmUpTime);
        insStmt.setInt(6, coolDownTime);
        insStmt.setInt(8, timesBeUsed);
        insStmt.setInt(9, (int) (System.currentTimeMillis() / 1000L));
        if (allowCrossWorldTP) {
            insStmt.setInt(7, 1);
        } else {
            insStmt.setInt(7, 0);
        }
        insStmt.execute();
        insStmt.close();

        final String msg1 = Lang.FINISHED_CREATING.toString();
        final String msg2 = MessageFormat.format(Lang.EXP_REQUIRED.toString(), getEXPRequired());
        final String msg3 = MessageFormat.format(Lang.MAKE_THIS_SCROLL.toString(), scrollID);
        final String msg4 = Lang.VIEW_SCROLL_LIST.toString();
        final String msg5 = MessageFormat.format(Lang.DELETE_THIS_SCROLL.toString(), scrollID);
        final String[] finalMessage = {msg1, msg2, msg3, msg4, msg5};
        return finalMessage;
    }

    public boolean scrollIsReady() {
        if (getDestinationType() == null) {
            return false;
        } else if (getWarmUpTime() == null) {
            return false;
        } else if (getCoolDownTime() == null) {
            return false;
        } else if (getAllowCrossWorldTP() == null) {
            return false;
        } else if (getTimesBeUsed() == null) {
            return false;
        } else if (getScrollName() == null) {
            return false;
        }
        return true;
    }

    public double getEXPRequired() {
        double EXPRequired = Main.getInstance().getConfig().getDouble("variable.make.BaseEXPRequired");

        if (owner.hasPermission(Main.getInstance().getProperty("artifactId") + ".VIP")) {
            EXPRequired *= Main.getInstance().getConfig().getDouble("variable.make.VIPExpFactor");
        }

        if (warmUpTime != null) {
            EXPRequired += Main.getInstance().getConfig().getDouble("variable.make.WarmUpEXPBase")
                    / (warmUpTime + Main.getInstance().getConfig().getDouble("variable.make.WarmUpEXPDenominatorSum"));
        }
        if (coolDownTime != null) {
            EXPRequired += Main.getInstance().getConfig().getDouble("variable.make.CoolDownExpBase")
                    / (coolDownTime + Main.getInstance().getConfig()
                            .getDouble("variable.make.CoolDownEXPDenominatorSum"));
        }
        if (timesBeUsed != null) {
            EXPRequired *= timesBeUsed;
        }
        if (allowCrossWorldTP != null && allowCrossWorldTP) {
            EXPRequired *= Main.getInstance().getConfig().getDouble("variable.make.CrossWorldTPExpFactor");
        }

        if (destinationType != null) {
            switch (destinationType) {
                case 0:
                    EXPRequired *= Main.getInstance().getConfig()
                            .getDouble("variable.make.ScrollDestinationExpFactor.Spawn");
                    break;
                case 1:
                    EXPRequired *= Main.getInstance().getConfig()
                            .getDouble("variable.make.ScrollDestinationExpFactor.Home");
                    break;
                case 2:
                    EXPRequired *= Main.getInstance().getConfig()
                            .getDouble("variable.make.ScrollDestinationExpFactor.FactionHome");
                    break;
                case 3:
                    EXPRequired *= Main.getInstance().getConfig()
                            .getDouble("variable.make.ScrollDestinationExpFactor.Player");
                    break;
                case 4:
                    EXPRequired *= Main.getInstance().getConfig()
                            .getDouble("variable.make.ScrollDestinationExpFactor.CurrentLocation");
                    break;
                case 5:
                    EXPRequired *= Main.getInstance().getConfig()
                            .getDouble("variable.make.ScrollDestinationExpFactor.Random");
                    break;
                case 6:
                    EXPRequired *= Main.getInstance().getConfig()
                            .getDouble("variable.make.ScrollDestinationExpFactor.SelfRescue");
                    break;
            }
        }
        return EXPRequired;
    }

    public Integer getDestinationType() {
        return destinationType;
    }

    public String[] setDestinationType(String input, boolean useDefaultOnFail) {
        try {
            return checkDestinationType(Integer.parseInt(input), useDefaultOnFail);
        } catch (final Exception ex) {
            return checkDestinationType(ScrollDatabase.destinationTypeStringToInteger(input), useDefaultOnFail);
        }
    }

    public static Integer destinationTypeStringToInteger(String destinationType) {
        switch (destinationType.toLowerCase()) {
            case "spawn":
                return 0;
            case "home":
                return 1;
            case "factionhome":
                return 2;
            case "player":
                return 3;
            case "currentlocation":
                return 4;
            case "random":
                return 5;
            case "selfrescue":
                return 6;
        }
        return -1;
    }

    public static String destinationTypeIntegerToString(Integer destinationType) {
        switch (destinationType) {
            case 0:
                return Lang.DESTINATION_SPAWN.toString();
            case 1:
                return Lang.DESTINATION_HOME.toString();
            case 2:
                return Lang.DESTINATION_FACTION_HOME.toString();
            case 3:
                return Lang.DESTINATION_PLAYER.toString();
            case 4:
                return Lang.DESTINATION_CURRENT_LOCATION.toString();
            case 5:
                return Lang.DESTINATION_RANDOM.toString();
            case 6:
                return Lang.DESTINATION_SELF_RESCUE.toString();
        }
        return Lang.DESTINATION_NOT_SET.toString();
    }

    private String[] checkDestinationType(Integer destinationType, boolean useDefaultOnFail) {
        final List<String> enabledDestinationTypes = Main.getInstance().getConfig()
                .getStringList("variable.create.ScrollDestination.Enabled");
        String humanEnabledDestinationTypes = "";
        for (final String enabledDestinationType: enabledDestinationTypes) {
            final Integer enabledDestinationInteger = ScrollDatabase
                    .destinationTypeStringToInteger(enabledDestinationType);
            humanEnabledDestinationTypes += ScrollDatabase.destinationTypeIntegerToString(enabledDestinationInteger)
                    + ", ";
            if ((enabledDestinationInteger == destinationType) && (enabledDestinationInteger != -1)) {
                switch (destinationType) {
                    case 2:
                        if (!Main.getInstance().getConfig().getBoolean("system.FactionsSupport")) {
                            return enterAgain("-", Lang.FACTIONS_UNSUPPORTED.toString());
                        }
                        break;
                    case 4:
                        setScrollName(Lang.NOT_SET.toString(), false);
                        break;
                    case 5:
                        allowCrossWorldTP = false;
                        break;
                    case 6:
                        allowCrossWorldTP = true;
                        setWarmUpTime(Main.getInstance().getConfig().getString("variable.create.WarmUpTime.Default"),
                                false);
                        setCoolDownTime(Main.getInstance().getConfig()
                                .getString("variable.create.CoolDownTime.Default"), false);
                        setTimesBeUsed(Main.getInstance().getConfig().getString("variable.create.TimesBeUsed.Default"),
                                false);
                        break;
                }
                this.destinationType = destinationType;
                return success(Lang.DESTINATION_TYPE.toString(),
                        ScrollDatabase.destinationTypeIntegerToString(destinationType));
            }
        }
        if (useDefaultOnFail) {
            final String defaultVal = Main.getInstance().getConfig()
                    .getString("variable.create.ScrollDestination.Default");
            return setDestinationType(defaultVal, false);
        }
        return enterAgain(humanEnabledDestinationTypes, ScrollDatabase.destinationTypeIntegerToString(destinationType));
    }

    public Integer getWarmUpTime() {
        return warmUpTime;
    }

    public String[] setWarmUpTime(String input, boolean useDefaultOnFail) {
        try {
            return checkWarmUpTime(Integer.parseInt(input), useDefaultOnFail);
        } catch (final Exception ex) {
            return enterAgain(Lang.INTEGER.toString(), Lang.NOT_AN_INTEGER.toString());
        }
    }

    public String[] checkWarmUpTime(Integer warmUpTime, boolean useDefaultOnFail) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Default");
        final Integer fallback = 10;

        if ((warmUpTime >= min) && (warmUpTime <= max) && (warmUpTime >= 0)) {
            this.warmUpTime = warmUpTime;
            return success(Lang.WARM_UP_TIME.toString(), warmUpTime.toString());
        } else if (owner.hasPermission(Main.getInstance().getProperty("artifactId") + ".overrideLimit.WarmUpTime")
                && (warmUpTime >= 0)) {
            this.warmUpTime = warmUpTime;
            return success(Lang.WARM_UP_TIME.toString(), warmUpTime.toString());
        } else if (min < 0) {
            if (defaultVal >= 0) {
                this.warmUpTime = defaultVal;
                final String expected = MessageFormat.format("{0} >= 0", Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.WarmUpTime.Min", expected, min, defaultVal);
            }
            this.warmUpTime = fallback;
            final String expected = MessageFormat.format("{0} >= 0", Lang.DEFAULT_VALUE.toString());
            return wrongConfigValue("variable.create.WarmUpTime.Default", expected, defaultVal, fallback);
        } else if ((max < 0) || (min > max)) {
            if (defaultVal >= 0) {
                this.warmUpTime = defaultVal;
                final String expected = MessageFormat.format("{0} >= 0, {1} <= {0}", Lang.MAX_VALUE.toString(),
                        Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.WarmUpTime.Max", expected, max, defaultVal);
            }
            this.warmUpTime = fallback;
            final String expected = MessageFormat.format("{0} >= 0", Lang.DEFAULT_VALUE.toString());
            return wrongConfigValue("variable.create.WarmUpTime.Default", expected, defaultVal, fallback);
        } else {
            if (useDefaultOnFail) {
                return checkWarmUpTime(defaultVal, false);
            }
            final String expected = MessageFormat.format("{0} - {1} {2}", min, max, Lang.SECONDS.toString());
            return enterAgain(expected, warmUpTime.toString());
        }
    }

    public Integer getCoolDownTime() {
        return coolDownTime;
    }

    public String[] setCoolDownTime(String input, boolean useDefaultOnFail) {
        try {
            return checkCoolDownTime(Integer.parseInt(input), useDefaultOnFail);
        } catch (final Exception ex) {
            return enterAgain(Lang.INTEGER.toString(), Lang.NOT_AN_INTEGER.toString());
        }
    }

    public String[] checkCoolDownTime(Integer coolDownTime, boolean useDefaultOnFail) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Default");
        final Integer fallback = 600;

        if ((coolDownTime >= min) && (coolDownTime <= max) && (coolDownTime >= 0)) {
            this.coolDownTime = coolDownTime;
            return success(Lang.COOL_DOWN_TIME.toString(), coolDownTime.toString());
        } else if (owner.hasPermission(Main.getInstance().getProperty("artifactId") + ".overrideLimit.CoolDownTime")
                && (coolDownTime >= 0)) {
            this.coolDownTime = coolDownTime;
            return success(Lang.COOL_DOWN_TIME.toString(), coolDownTime.toString());
        } else if (min < 0) {
            if (defaultVal >= 0) {
                this.coolDownTime = defaultVal;
                final String expected = MessageFormat.format("{0} >= 0", Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.CoolDownTime.Min", expected, min, defaultVal);
            }
            this.coolDownTime = fallback;
            final String expected = MessageFormat.format("{0} >= 0", Lang.DEFAULT_VALUE.toString());
            return wrongConfigValue("variable.create.CoolDownTime.Default", expected, defaultVal, fallback);
        } else if ((max < 0) || (min > max)) {
            if (defaultVal >= 0) {
                this.coolDownTime = defaultVal;
                final String expected = MessageFormat.format("{0} >= 0, {1} <= {0}", Lang.MAX_VALUE.toString(),
                        Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.CoolDownTime.Max", expected, max, defaultVal);
            }
            this.coolDownTime = fallback;
            final String expected = MessageFormat.format("{0} >= 0", Lang.DEFAULT_VALUE.toString());
            return wrongConfigValue("variable.create.CoolDownTime.Default", expected, defaultVal, fallback);
        } else {
            if (useDefaultOnFail) {
                return checkCoolDownTime(defaultVal, false);
            }
            final String expected = MessageFormat.format("{0} - {1} {2}", min, max, Lang.SECONDS.toString());
            return enterAgain(expected, coolDownTime.toString());
        }
    }

    public Boolean getAllowCrossWorldTP() {
        return allowCrossWorldTP;
    }

    public static String getAllowCrossWorldTPRepr(Integer allowCrossWorldTP) {
        if (allowCrossWorldTP == null) {
            return Lang.NOT_SET.toString();
        }
        if (allowCrossWorldTP == 1) {
            return Lang.ALLOWED.toString();
        }
        return Lang.NOT_ALLOWED.toString();
    }

    public static String getAllowCrossWorldTPRepr(Boolean allowCrossWorldTP) {
        if (allowCrossWorldTP == null) {
            return Lang.NOT_SET.toString();
        }
        if (allowCrossWorldTP) {
            return Lang.ALLOWED.toString();
        }
        return Lang.NOT_ALLOWED.toString();
    }

    public String[] setAllowCrossWorldTP(String input, boolean useDefaultOnFail) {
        final String[] yesList = {"1", "true", "t", "yes", "y"};
        final String[] noList = {"0", "false", "f", "no", "n"};
        for (final String yes: yesList) {
            if (input.equalsIgnoreCase(yes)) {
                allowCrossWorldTP = true;
                return success(Lang.CROSS_WORLD_TP.toString(), "Yes");
            }
        }
        for (final String no: noList) {
            if (input.equalsIgnoreCase(no)) {
                allowCrossWorldTP = false;
                return success(Lang.CROSS_WORLD_TP.toString(), "No");
            }
        }
        if (useDefaultOnFail) {
            final boolean defaultVal = Main.getInstance().getConfig()
                    .getBoolean("variable.create.CrossWorldTP.Default");
            allowCrossWorldTP = defaultVal;
        }
        return enterAgain("<Yes/No>", "Not <Yes/No>");
    }

    public Integer getTimesBeUsed() {
        return timesBeUsed;
    }

    public String[] setTimesBeUsed(String input, boolean useDefaultOnFail) {
        try {
            return checkTimesBeUsed(Integer.parseInt(input), useDefaultOnFail);
        } catch (final Exception ex) {
            return enterAgain(Lang.INTEGER.toString(), Lang.NOT_AN_INTEGER.toString());
        }
    }

    public String[] checkTimesBeUsed(Integer timesBeUsed, boolean useDefaultOnFail) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Default");
        final Integer fallback = 1;

        if ((timesBeUsed >= min) && (timesBeUsed <= max) && (timesBeUsed >= 1)) {
            this.timesBeUsed = timesBeUsed;
            return success(Lang.TIMES_BE_USED.toString(), timesBeUsed.toString());
        } else if (owner.hasPermission(Main.getInstance().getProperty("artifactId") + ".overrideLimit.TimesBeUsed")
                && (timesBeUsed >= 1)) {
            this.timesBeUsed = timesBeUsed;
            return success(Lang.TIMES_BE_USED.toString(), timesBeUsed.toString());
        } else if (min < 1) {
            if (defaultVal >= 1) {
                this.timesBeUsed = defaultVal;
                final String expected = MessageFormat.format("{0} >= 1", Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.TimesBeUsed.Min", expected, min, defaultVal);
            }
            this.timesBeUsed = fallback;
            final String expected = MessageFormat.format("{0} >= 1", Lang.DEFAULT_VALUE.toString());
            return wrongConfigValue("variable.create.TimesBeUsed.Default", expected, defaultVal, fallback);
        } else if ((max < 1) || (min > max)) {
            if (defaultVal >= 1) {
                this.timesBeUsed = defaultVal;
                final String expected = MessageFormat.format("{0} >= 1, {1} <= {0}", Lang.MAX_VALUE.toString(),
                        Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.TimesBeUsed.Max", expected, max, defaultVal);
            }
            this.timesBeUsed = fallback;
            final String expected = MessageFormat.format("{0} >= 1", Lang.DEFAULT_VALUE.toString());
            return wrongConfigValue("variable.create.TimesBeUsed.Default", expected, defaultVal, fallback);
        } else {
            if (useDefaultOnFail) {
                return checkTimesBeUsed(defaultVal, false);
            }
            final String expected = MessageFormat.format("{0} - {1} {2}", min, max, Lang.TIMES_MEASURE_WORD.toString());
            return enterAgain(expected, timesBeUsed.toString());
        }
    }

    public String getScrollName() {
        return name;
    }

    public String[] setScrollName(String input, boolean useDefaultOnFail) {
        final String regex = Main.getInstance().getConfig().getString("variable.create.EnterName.Regex");
        if (input.matches(regex)) {
            final List<String> bannedRegexs = Main.getInstance().getConfig()
                    .getStringList("variable.create.EnterName.BannedRegexs");
            for (final String bannedRegex: bannedRegexs) {
                if (input.toLowerCase().matches(bannedRegex)) {
                    final String expected = MessageFormat.format(Lang.NOT_MATCHING_REGEX.toString(), bannedRegex)
                            + ". " + Lang.IS_SCROLL_NAME_CONTAINING_BAD_WORD.toString();
                    final String got = MessageFormat.format(Lang.MATCHING_REGEX.toString(), bannedRegex);
                    if (useDefaultOnFail) {
                        final String defaultVal = Main.getInstance().getConfig()
                                .getString("variable.create.EnterName.Default");
                        name = defaultVal;
                    }
                    return enterAgain(expected, got);
                }
            }
            name = input;
            return success(Lang.SCROLL_NAME.toString(), input);
        }
        final String expected = MessageFormat.format(Lang.MATCHING_REGEX.toString(), regex);
        final String got = MessageFormat.format(Lang.NOT_MATCHING_REGEX.toString(), regex);
        if (useDefaultOnFail) {
            final String defaultVal = Main.getInstance().getConfig().getString("variable.create.EnterName.Default");
            name = defaultVal;
        }
        return enterAgain(expected, got);
    }

}