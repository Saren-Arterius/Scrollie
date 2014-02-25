package net.wtako.Scrollie.Methods;

import java.text.MessageFormat;
import java.util.List;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

public class Scroll {

    private Integer destinationType;
    private Integer warmUpTime;
    private Integer coolDownTime;
    private Integer timesBeUsed;

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

    public Integer getDestinationType() {
        return destinationType;
    }

    public String[] setDestinationType(String input) {
        try {
            return checkDestinationType(Integer.parseInt(input));
        } catch (final Exception ex) {
            return checkDestinationType(destinationTypeStringToInteger(input));
        }
    }

    public Integer destinationTypeStringToInteger(String destinationType) {
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
        }
        return -1;
    }

    public String destinationTypeIntegerToString(Integer destinationType) {
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
        }
        return Lang.DESTINATION_NOT_SET.toString();
    }

    private String[] checkDestinationType(Integer destinationType) {
        final List<String> enabledDestinationTypes = Main.getInstance().getConfig()
                .getStringList("variable.create.ScrollDestination.Enabled");
        String humanEnabledDestinationTypes = "";
        for (final String enabledDestinationType: enabledDestinationTypes) {
            final Integer enabledDestinationInteger = destinationTypeStringToInteger(enabledDestinationType);
            humanEnabledDestinationTypes += destinationTypeIntegerToString(enabledDestinationInteger) + ", ";
            if ((enabledDestinationInteger == destinationType) && (enabledDestinationInteger != -1)) {
                this.destinationType = destinationType;
                return success(Lang.DESTINATION_TYPE.toString(), destinationTypeIntegerToString(destinationType));
            }
        }
        return enterAgain(humanEnabledDestinationTypes, destinationTypeIntegerToString(destinationType));
    }

    public Integer getWarmUpTime() {
        return warmUpTime;
    }

    public String[] setWarmUpTime(String input) {
        try {
            return checkWarmUpTime(Integer.parseInt(input));
        } catch (final Exception ex) {
            return enterAgain(Lang.INTEGER.toString(), Lang.NOT_AN_INTEGER.toString());
        }
    }

    public String[] checkWarmUpTime(Integer warmUpTime) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Default");
        final Integer fallback = 10;

        if ((warmUpTime >= min) && (warmUpTime <= max) && (warmUpTime >= 0)) {
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
            final String expected = MessageFormat.format("{0} - {1} {2}", min, max, Lang.SECONDS.toString());
            return enterAgain(expected, warmUpTime.toString());
        }
    }

    public Integer getCoolDownTime() {
        return coolDownTime;
    }

    public String[] setCoolDownTime(String input) {
        try {
            return checkCoolDownTime(Integer.parseInt(input));
        } catch (final Exception ex) {
            return enterAgain(Lang.INTEGER.toString(), Lang.NOT_AN_INTEGER.toString());
        }
    }

    public String[] checkCoolDownTime(Integer coolDownTime) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Default");
        final Integer fallback = 600;

        if ((coolDownTime >= min) && (coolDownTime <= max) && (coolDownTime >= 0)) {
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
            final String expected = MessageFormat.format("{0} - {1} {2}", min, max, Lang.SECONDS.toString());
            return enterAgain(expected, coolDownTime.toString());
        }
    }

    public Integer getTimesBeUsed() {
        return timesBeUsed;
    }

    public String[] setTimesBeUsed(String input) {
        try {
            return checkTimesBeUsed(Integer.parseInt(input));
        } catch (final Exception ex) {
            return enterAgain(Lang.INTEGER.toString(), Lang.NOT_AN_INTEGER.toString());
        }
    }

    public String[] checkTimesBeUsed(Integer timesBeUsed) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Default");
        final Integer fallback = 1;

        if ((timesBeUsed >= min) && (timesBeUsed <= max) && (timesBeUsed >= 1)) {
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
            final String expected = MessageFormat.format("{0} - {1} {2}", min, max, Lang.TIMES_MEASURE_WORD.toString());
            return enterAgain(expected, timesBeUsed.toString());
        }
    }

}