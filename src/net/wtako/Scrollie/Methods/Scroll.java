package net.wtako.Scrollie.Methods;

import java.text.MessageFormat;
import java.util.List;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Messages;

public class Scroll {
    
    private Integer destinationType;

    public String setDestinationType(Integer destinationType) {
        return checkDestinationType(destinationType);
    }
    
    public String setDestinationType(String destinationType) {
        return checkDestinationType(destinationTypeStringToInteger(destinationType));
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
        Messages messages = Messages.getInstance();
        List<String> enabledDestinationTypes = Main.getInstance().getConfig().getStringList("variable.create.ScrollDestination.Enabled");
        for (String enabledDestinationType: enabledDestinationTypes) {
            Integer enabledDestinationInteger = destinationTypeStringToInteger(enabledDestinationType);
            if ((enabledDestinationInteger == destinationType)
                    && (enabledDestinationInteger != -1)) {
                this.destinationType = destinationType;
                String message = messages.getMsg("general.create.ValueSet");
                return MessageFormat.format(message, "Destination Type", enabledDestinationType);
            }
        }
        String message = messages.getMsg("error.create.EnterAgain");
        return MessageFormat.format(message, enabledDestinationTypes.toString(), destinationTypeIntegerToString(destinationType));
    }

    public Integer getDestinationType() {
        return destinationType;
    }
}