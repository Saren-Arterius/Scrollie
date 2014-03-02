package net.wtako.Scrollie.Methods;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Database;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;

public class ScrollDatabase extends Database {

    private final Player owner;
    private Integer      destinationType;
    private Integer      warmUpTime;
    private Integer      coolDownTime;
    private Integer      timesBeUsed;
    private Boolean      allowCrossWorldTP;
    private String       name;

    public ScrollDatabase(Player player) throws SQLException {
        owner = player;
        setDefaultValue();
    }

    private void setDefaultValue() {
        if (!Main.getInstance().getConfig().getBoolean("variable.create.ScrollDestination.CanCustomize")
                && !owner.hasPermission("Scrollie.overrideCanCustomize.ScrollDestination")) {
            setDestinationType(Main.getInstance().getConfig().getString("variable.create.ScrollDestination.Default"),
                    false);
        }
        if (!Main.getInstance().getConfig().getBoolean("variable.create.WarmUpTime.CanCustomize")
                && !owner.hasPermission("Scrollie.overrideCanCustomize.WarmUpTime")) {
            setWarmUpTime(Main.getInstance().getConfig().getString("variable.create.WarmUpTime.Default"), false);
        }
        if (!Main.getInstance().getConfig().getBoolean("variable.create.CoolDownTime.CanCustomize")
                && !owner.hasPermission("Scrollie.overrideCanCustomize.CoolDownTime")) {
            setCoolDownTime(Main.getInstance().getConfig().getString("variable.create.CoolDownTime.Default"), false);
        }
        if (!Main.getInstance().getConfig().getBoolean("variable.create.CrossWorldTP.CanCustomize")
                && !owner.hasPermission("Scrollie.overrideCanCustomize.CrossWorldTP")) {
            setAllowCrossWorldTP(Main.getInstance().getConfig().getString("variable.create.CrossWorldTP.Default"),
                    false);
        }
        if (!Main.getInstance().getConfig().getBoolean("variable.create.TimesBeUsed.CanCustomize")
                && !owner.hasPermission("Scrollie.overrideCanCustomize.TimesBeUsed")) {
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
        final String msg1 = Lang.VALUE_SET.toString();
        final String msg2 = MessageFormat.format(Lang.KEY_VALUE.toString(), key, value);
        final String[] finalMessage = {msg1, msg2};
        return finalMessage;
    }

    public String[] save() throws SQLException {
        final PreparedStatement selStmt = conn.prepareStatement("SELECT max(scroll_id) FROM 'scrolls' WHERE owner = ?");
        selStmt.setString(1, owner.getName().toLowerCase());
        final int scrollID = selStmt.executeQuery().getInt(1) + 1;
        selStmt.close();

        final PreparedStatement insStmt = conn
                .prepareStatement("INSERT INTO `scrolls` (`owner`, `scroll_id`, `name`, `scroll_destination`, `warm_up_time`, `cool_down_time`, `allow_cross_world_tp`, `times_be_used`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        insStmt.setString(1, owner.getName().toLowerCase());
        insStmt.setInt(2, scrollID);
        insStmt.setString(3, name);
        insStmt.setInt(4, destinationType);
        insStmt.setInt(5, warmUpTime);
        insStmt.setInt(6, coolDownTime);
        insStmt.setInt(8, timesBeUsed);
        if (allowCrossWorldTP) {
            insStmt.setInt(7, 1);
        } else {
            insStmt.setInt(7, 0);
        }
        insStmt.execute();
        insStmt.close();

        final String msg1 = Lang.FINISHED_CREATING.toString();
        final String msg2 = MessageFormat.format(Lang.MAKE_THIS_SCROLL.toString(), scrollID);
        final String msg3 = Lang.VIEW_SCROLL_LIST.toString();
        final String msg4 = MessageFormat.format(Lang.DELETE_THIS_SCROLL.toString(), scrollID);
        final String[] finalMessage = {msg1, msg2, msg3, msg4};
        return finalMessage;
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
                this.destinationType = destinationType;
                if (destinationType == 5) { // Random location in a world
                    allowCrossWorldTP = false;
                } else if (destinationType == 6) { // Self rescue scroll
                    allowCrossWorldTP = true;
                    setWarmUpTime(Main.getInstance().getConfig().getString("variable.create.WarmUpTime.Default"), false);
                    setCoolDownTime(Main.getInstance().getConfig().getString("variable.create.CoolDownTime.Default"),
                            false);
                    setTimesBeUsed(Main.getInstance().getConfig().getString("variable.create.TimesBeUsed.Default"),
                            false);
                }
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
        } else if (owner.hasPermission("Scrollie.overrideLimit.WarmUpTime") && (warmUpTime >= 0)) {
            this.warmUpTime = warmUpTime;
            return success(Lang.WARM_UP_TIME.toString(), warmUpTime.toString());
        } else if (min < 0) {
            if (defaultVal >= 0) {
                this.warmUpTime = defaultVal;
                final String expected = MessageFormat.format("{0} >= 0", Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.WarmUpTime.Min", expected, min, defaultVal);
            } else {
                this.warmUpTime = fallback;
                final String expected = MessageFormat.format("{0} >= 0", Lang.DEFAULT_VALUE.toString());
                return wrongConfigValue("variable.create.WarmUpTime.Default", expected, defaultVal, fallback);
            }
        } else if ((max < 0) || (min > max)) {
            if (defaultVal >= 0) {
                this.warmUpTime = defaultVal;
                final String expected = MessageFormat.format("{0} >= 0, {1} <= {0}", Lang.MAX_VALUE.toString(),
                        Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.WarmUpTime.Max", expected, max, defaultVal);
            } else {
                this.warmUpTime = fallback;
                final String expected = MessageFormat.format("{0} >= 0", Lang.DEFAULT_VALUE.toString());
                return wrongConfigValue("variable.create.WarmUpTime.Default", expected, defaultVal, fallback);
            }
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
        } else if (owner.hasPermission("Scrollie.overrideLimit.CoolDownTime") && (coolDownTime >= 0)) {
            this.coolDownTime = coolDownTime;
            return success(Lang.COOL_DOWN_TIME.toString(), coolDownTime.toString());
        } else if (min < 0) {
            if (defaultVal >= 0) {
                this.coolDownTime = defaultVal;
                final String expected = MessageFormat.format("{0} >= 0", Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.CoolDownTime.Min", expected, min, defaultVal);
            } else {
                this.coolDownTime = fallback;
                final String expected = MessageFormat.format("{0} >= 0", Lang.DEFAULT_VALUE.toString());
                return wrongConfigValue("variable.create.CoolDownTime.Default", expected, defaultVal, fallback);
            }
        } else if ((max < 0) || (min > max)) {
            if (defaultVal >= 0) {
                this.coolDownTime = defaultVal;
                final String expected = MessageFormat.format("{0} >= 0, {1} <= {0}", Lang.MAX_VALUE.toString(),
                        Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.CoolDownTime.Max", expected, max, defaultVal);
            } else {
                this.coolDownTime = fallback;
                final String expected = MessageFormat.format("{0} >= 0", Lang.DEFAULT_VALUE.toString());
                return wrongConfigValue("variable.create.CoolDownTime.Default", expected, defaultVal, fallback);
            }
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
        } else if (owner.hasPermission("Scrollie.overrideLimit.TimesBeUsed") && (timesBeUsed >= 1)) {
            this.timesBeUsed = timesBeUsed;
            return success(Lang.TIMES_BE_USED.toString(), timesBeUsed.toString());
        } else if (min < 1) {
            if (defaultVal >= 1) {
                this.timesBeUsed = defaultVal;
                final String expected = MessageFormat.format("{0} >= 1", Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.TimesBeUsed.Min", expected, min, defaultVal);
            } else {
                this.timesBeUsed = fallback;
                final String expected = MessageFormat.format("{0} >= 1", Lang.DEFAULT_VALUE.toString());
                return wrongConfigValue("variable.create.TimesBeUsed.Default", expected, defaultVal, fallback);
            }
        } else if ((max < 1) || (min > max)) {
            if (defaultVal >= 1) {
                this.timesBeUsed = defaultVal;
                final String expected = MessageFormat.format("{0} >= 1, {1} <= {0}", Lang.MAX_VALUE.toString(),
                        Lang.MIN_VALUE.toString());
                return wrongConfigValue("variable.create.TimesBeUsed.Max", expected, max, defaultVal);
            } else {
                this.timesBeUsed = fallback;
                final String expected = MessageFormat.format("{0} >= 1", Lang.DEFAULT_VALUE.toString());
                return wrongConfigValue("variable.create.TimesBeUsed.Default", expected, defaultVal, fallback);
            }
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
        } else {
            final String expected = MessageFormat.format(Lang.MATCHING_REGEX.toString(), regex);
            final String got = MessageFormat.format(Lang.NOT_MATCHING_REGEX.toString(), regex);
            if (useDefaultOnFail) {
                final String defaultVal = Main.getInstance().getConfig().getString("variable.create.EnterName.Default");
                name = defaultVal;
            }
            return enterAgain(expected, got);
        }
    }

}