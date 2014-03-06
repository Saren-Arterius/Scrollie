package net.wtako.Scrollie.Methods.Locations;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

import net.wtako.Scrollie.Methods.LocationSource;
import net.wtako.Scrollie.Utils.Lang;

public class FactionHomeLocation implements LocationSource {

    private Location destLoc;
    private Player   player;
    private UPlayer  uplayer;

    public FactionHomeLocation(Player player) {
        this.player = player;
        try {
            this.uplayer = UPlayer.get(player);
        } catch (Error e) {
            player.sendMessage(Lang.FACTION_EXCEPTION.toString());
            this.uplayer = null;
        }
    }

    @Override
    public Location get() {
        if (uplayer == null) {
            return null;
        }
        Faction faction = uplayer.getFaction();
        if (faction == null) {
            player.sendMessage(Lang.YOU_ARE_NOT_FACTION_MEMBER.toString());
            return null;
        }
        if ((destLoc = faction.getHome().asBukkitLocation()) == null) {
            player.sendMessage(Lang.YOUR_FACTION_DOES_NOT_HAVE_HOME.toString());
        }
        return destLoc;
    }

}
