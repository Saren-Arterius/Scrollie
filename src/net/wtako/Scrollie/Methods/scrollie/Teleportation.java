package net.wtako.Scrollie.Methods.scrollie;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Teleportation {
    private Player player;
    private Location dest;

    public Teleportation(Player player, Location dest) {
        this.player = player;
        this.dest = dest;
    }
    
    public boolean perform() {
        player.teleport(dest);
        return true;
    }
}
