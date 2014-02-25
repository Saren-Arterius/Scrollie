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
                return success("Destination Type", destinationTypeIntegerToString(destinationType));
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
            return enterAgain("Integer", "Not integer");
        }
    }

    public String[] checkWarmUpTime(Integer warmUpTime) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.WarmUpTime.Default");
        final Integer fallback = 10;

        if ((warmUpTime >= min) && (warmUpTime <= max) && (warmUpTime >= 0)) {
            this.warmUpTime = warmUpTime;
            return success("Warm up time", warmUpTime.toString());
        } else if (min < 0) {
            if (defaultVal >= 0) {
                this.warmUpTime = defaultVal;
                return wrongConfigValue("variable.create.WarmUpTime.Min", "Min >= 0", min, defaultVal);
            } else {
                this.warmUpTime = fallback;
                return wrongConfigValue("variable.create.WarmUpTime.Default", "Default >= 0", defaultVal, fallback);
            }
        } else if ((max < 0) || (min > max)) {
            if (defaultVal >= 0) {
                this.warmUpTime = defaultVal;
                return wrongConfigValue("variable.create.WarmUpTime.Max", "Max >= 0, Min <= Max", max, defaultVal);
            } else {
                this.warmUpTime = fallback;
                return wrongConfigValue("variable.create.WarmUpTime.Default", "Default >= 0", defaultVal, fallback);
            }
        } else {
            final String expected = MessageFormat.format("{0} - {1} seconds", min, max);
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
            return enterAgain("Integer", "Not integer");
        }
    }

    public String[] checkCoolDownTime(Integer coolDownTime) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.CoolDownTime.Default");
        final Integer fallback = 600;

        if ((coolDownTime >= min) && (coolDownTime <= max) && (coolDownTime >= 0)) {
            this.coolDownTime = coolDownTime;
            return success("Cool down time", coolDownTime.toString());
        } else if (min < 0) {
            if (defaultVal >= 0) {
                this.coolDownTime = defaultVal;
                return wrongConfigValue("variable.create.CoolDownTime.Min", "Min >= 0", min, defaultVal);
            } else {
                this.coolDownTime = fallback;
                return wrongConfigValue("variable.create.CoolDownTime.Default", "Default >= 0", defaultVal, fallback);
            }
        } else if ((max < 0) || (min > max)) {
            if (defaultVal >= 0) {
                this.coolDownTime = defaultVal;
                return wrongConfigValue("variable.create.CoolDownTime.Max", "Max >= 0, Min <= Max", max, defaultVal);
            } else {
                this.coolDownTime = fallback;
                return wrongConfigValue("variable.create.CoolDownTime.Default", "Default >= 0", defaultVal, fallback);
            }
        } else {
            final String expected = MessageFormat.format("{0} - {1} seconds", min, max);
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
            return enterAgain("Integer", "Not integer");
        }
    }

    public String[] checkTimesBeUsed(Integer timesBeUsed) {
        final Integer min = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Min");
        final Integer max = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Max");
        final Integer defaultVal = Main.getInstance().getConfig().getInt("variable.create.TimesBeUsed.Default");
        final Integer fallback = 1;

        if ((timesBeUsed >= min) && (timesBeUsed <= max) && (timesBeUsed >= 1)) {
            this.timesBeUsed = timesBeUsed;
            return success("Times be used", timesBeUsed.toString());
        } else if (min < 1) {
            if (defaultVal >= 1) {
                this.timesBeUsed = defaultVal;
                return wrongConfigValue("variable.create.TimesBeUsed.Min", "Min >= 1", min, defaultVal);
            } else {
                this.timesBeUsed = fallback;
                return wrongConfigValue("variable.create.TimesBeUsed.Default", "Default >= 1", defaultVal, fallback);
            }
        } else if ((max < 1) || (min > max)) {
            if (defaultVal >= 1) {
                this.timesBeUsed = defaultVal;
                return wrongConfigValue("variable.create.TimesBeUsed.Max", "Max >= 1, Min <= Max", max, defaultVal);
            } else {
                this.timesBeUsed = fallback;
                return wrongConfigValue("variable.create.TimesBeUsed.Default", "Default >= 1", defaultVal, fallback);
            }
        } else {
            final String expected = MessageFormat.format("{0} - {1} times", min, max);
            return enterAgain(expected, timesBeUsed.toString());
        }
    }

}