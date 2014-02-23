package net.wtako.Scrollie.Utils;

import java.util.logging.Level;
import org.bukkit.entity.Player;
import net.wtako.Scrollie.Main;

public class Messaging {

    private String msg;
    private Main instance;

    public Messaging(String msg) {
        this.msg = msg;
        this.instance = Main.getInstance();
    }
    
    public boolean sendTo(Player player) {
        player.sendMessage(this.msg);
        return true;
    }
    
    public boolean logInfo() {
        this.instance.getLogger().info(this.msg);
        return true;
    }
    
    public boolean logWarning() {
        this.instance.getLogger().log(Level.WARNING, this.msg);
        return true;
    }
    
    public boolean logError() {
        this.instance.getLogger().log(Level.SEVERE, this.msg);
        return true;
    }
}
