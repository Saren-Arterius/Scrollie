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
    
    public Integer getDestinationType() {
        return destinationType;
    }

    public String setDestinationType(String input) {
        try {
            return checkDestinationType(Integer.parseInt(input));
        } catch (Exception ex) {
            return checkDestinationType(destinationTypeStringToInteger(input));
        }
    }

    private Integer destinationTypeStringToInteger(String destinationType) {
        switch (destinationType.toLowerCase()) {
        case "spawn":
            return 0;
        case "home":
            return 1;
        case "currentlocation":
            return 2;
        case "random":
            return 3;
        }
        return -1;
    }

    private String destinationTypeIntegerToString(Integer destinationType) {
        switch (destinationType) {
        case 0:
            return "Spawn";
        case 1:
            return "Home";
        case 2:
            return "CurrentLocation";
        case 3:
            return "Random";
        }
        return "NotSet";
    }

    private String checkDestinationType(Integer destinationType) {

        List<String> enabledDestinationTypes = Main.getInstance().getConfig()
                .getStringList("variable.create.ScrollDestination.Enabled");
        for (String enabledDestinationType : enabledDestinationTypes) {
            Integer enabledDestinationInteger = destinationTypeStringToInteger(enabledDestinationType);
            if ((enabledDestinationInteger == destinationType)
                    && (enabledDestinationInteger != -1)) {
                this.destinationType = destinationType;
                String message = Lang.VALUE_SET.toString();
                return MessageFormat.format(message, "Destination Type",
                        enabledDestinationType);
            }
        }
        String message = Lang.ENTER_AGAIN.toString();
        return MessageFormat.format(message,
                enabledDestinationTypes.toString(),
                destinationTypeIntegerToString(destinationType));
    }

    public Integer getWarmUpTime() {
        return warmUpTime;
    }

    public String setWarmUpTime(String input) {
        try {
            return checkWarmUpTime(Integer.parseInt(input));
        } catch (Exception ex) {

            String message = Lang.ENTER_AGAIN.toString();
            return MessageFormat.format(message, "Integer", "Not integer");
        }
    }

    public String checkWarmUpTime(Integer warmUpTime) {

        Integer min = Main.getInstance().getConfig()
                .getInt("variable.create.WarmUpTime.Min");
        Integer max = Main.getInstance().getConfig()
                .getInt("variable.create.WarmUpTime.Max");
        Integer defaultVal = Main.getInstance().getConfig()
                .getInt("variable.create.WarmUpTime.Default");
        Integer fallback = 10;

        if ((warmUpTime >= min) && (warmUpTime <= max) && (warmUpTime >= 0)) {
            this.warmUpTime = warmUpTime;
            String message = Lang.VALUE_SET.toString();
            return MessageFormat.format(message, "Warm up time", warmUpTime);
        } else if (min < 0) {
            String message = Lang.WRONG_VALUE.toString();
            if (defaultVal >= 0) {
                this.warmUpTime = defaultVal;
                return MessageFormat.format(message,
                        "variable.create.WarmUpTime.Min", "Min >= 0", min,
                        defaultVal);
            } else {
                this.warmUpTime = fallback;
                return MessageFormat.format(message,
                        "variable.create.WarmUpTime.Default", "Default >= 0",
                        defaultVal, fallback);
            }
        } else if ((max < 0) || (min > max)) {
            String message = Lang.WRONG_VALUE.toString();
            if (defaultVal >= 0) {
                this.warmUpTime = defaultVal;
                return MessageFormat.format(message,
                        "variable.create.WarmUpTime.Max",
                        "Max >= 0, Min <= Max", max, defaultVal);
            } else {
                this.warmUpTime = fallback;
                return MessageFormat.format(message,
                        "variable.create.WarmUpTime.Default", "Default >= 0",
                        defaultVal, fallback);
            }
        } else {
            String message = Lang.ENTER_AGAIN.toString();
            String expected = MessageFormat.format("{0} - {1} seconds", min,
                    max);
            return MessageFormat.format(message, expected, warmUpTime);
        }
    }

    public Integer getCoolDownTime() {
        return coolDownTime;
    }

    public String setCoolDownTime(String input) {
        try {
            return checkCoolDownTime(Integer.parseInt(input));
        } catch (Exception ex) {
            String message = Lang.ENTER_AGAIN.toString();
            return MessageFormat.format(message, "Integer", "Not integer");
        }
    }

    public String checkCoolDownTime(Integer coolDownTime) {
        Integer min = Main.getInstance().getConfig()
                .getInt("variable.create.CoolDownTime.Min");
        Integer max = Main.getInstance().getConfig()
                .getInt("variable.create.CoolDownTime.Max");
        Integer defaultVal = Main.getInstance().getConfig()
                .getInt("variable.create.CoolDownTime.Default");
        Integer fallback = 600;

        if ((coolDownTime >= min) && (coolDownTime <= max)
                && (coolDownTime >= 0)) {
            this.coolDownTime = coolDownTime;
            String message = Lang.VALUE_SET.toString();
            return MessageFormat
                    .format(message, "Cool down time", coolDownTime);
        } else if (min < 0) {
            String message = Lang.WRONG_VALUE.toString();
            if (defaultVal >= 0) {
                this.coolDownTime = defaultVal;
                return MessageFormat.format(message,
                        "variable.create.CoolDownTime.Min", "Min >= 0", min,
                        defaultVal);
            } else {
                this.coolDownTime = fallback;
                return MessageFormat.format(message,
                        "variable.create.CoolDownTime.Default", "Default >= 0",
                        defaultVal, fallback);
            }
        } else if ((max < 0) || (min > max)) {
            String message = Lang.WRONG_VALUE.toString();
            if (defaultVal >= 0) {
                this.coolDownTime = defaultVal;
                return MessageFormat.format(message,
                        "variable.create.CoolDownTime.Max",
                        "Max >= 0, Min <= Max", max, defaultVal);
            } else {
                this.coolDownTime = fallback;
                return MessageFormat.format(message,
                        "variable.create.CoolDownTime.Default", "Default >= 0",
                        defaultVal, fallback);
            }
        } else {
            String message = Lang.ENTER_AGAIN.toString();
            String expected = MessageFormat.format("{0} - {1} seconds", min,
                    max);
            return MessageFormat.format(message, expected, coolDownTime);
        }
    }

    public Integer getTimesBeUsed() {
        return timesBeUsed;
    }

    public String setTimesBeUsed(String input) {
        try {
            return checkDestinationType(Integer.parseInt(input));
        } catch (Exception ex) {
            String message = Lang.ENTER_AGAIN.toString();
            return MessageFormat.format(message, "Integer", "Not integer");
        }
    }

    public String checkTimesBeUsed(Integer timesBeUsed) {
        Integer min = Main.getInstance().getConfig()
                .getInt("variable.create.TimesBeUsed.Min");
        Integer max = Main.getInstance().getConfig()
                .getInt("variable.create.TimesBeUsed.Max");
        Integer defaultVal = Main.getInstance().getConfig()
                .getInt("variable.create.TimesBeUsed.Default");
        Integer fallback = 1;

        if ((timesBeUsed >= min) && (timesBeUsed <= max) && (timesBeUsed >= 1)) {
            this.timesBeUsed = timesBeUsed;
            String message = Lang.VALUE_SET.toString();
            return MessageFormat.format(message, "Times be used", timesBeUsed);
        } else if (min < 1) {
            String message = Lang.WRONG_VALUE.toString();
            if (defaultVal >= 1) {
                this.timesBeUsed = defaultVal;
                return MessageFormat.format(message,
                        "variable.create.TimesBeUsed.Min", "Min >= 1", min,
                        defaultVal);
            } else {
                this.timesBeUsed = fallback;
                return MessageFormat.format(message,
                        "variable.create.TimesBeUsed.Default", "Default >= 1",
                        defaultVal, fallback);
            }
        } else if ((max < 1) || (min > max)) {
            String message = Lang.WRONG_VALUE.toString();
            if (defaultVal >= 1) {
                this.timesBeUsed = defaultVal;
                return MessageFormat.format(message,
                        "variable.create.TimesBeUsed.Max",
                        "Max >= 1, Min <= Max", max, defaultVal);
            } else {
                this.timesBeUsed = fallback;
                return MessageFormat.format(message,
                        "variable.create.TimesBeUsed.Default", "Default >= 1",
                        defaultVal, fallback);
            }
        } else {
            String message = Lang.ENTER_AGAIN.toString();
            String expected = MessageFormat.format("{0} - {1} seconds", min,
                    max);
            return MessageFormat.format(message, expected, timesBeUsed);
        }
    }

}