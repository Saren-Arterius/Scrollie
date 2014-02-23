package net.wtako.Scrollie.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {

    public static Logger ScrollieLogger = Logger.getLogger("Scrollie");

    public boolean logInfo(String msg) {
        ScrollieLogger.info(msg);
        return true;
    }
    
    public boolean logWarning(String msg) {
        ScrollieLogger.log(Level.WARNING, msg);
        return true;
    }
    
    public boolean logError(String msg) {
        ScrollieLogger.log(Level.SEVERE, msg);
        return true;
    }
}
