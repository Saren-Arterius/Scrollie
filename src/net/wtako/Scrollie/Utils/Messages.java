package net.wtako.Scrollie.Utils;

import net.wtako.Scrollie.Main;

public class Messages {
    
    private Main instance;
    
    public Messages() {
        this.instance = Main.getInstance();
    }
    
    public String get(String node) {
        return this.instance.getConfig().getString(node);
    }
}
