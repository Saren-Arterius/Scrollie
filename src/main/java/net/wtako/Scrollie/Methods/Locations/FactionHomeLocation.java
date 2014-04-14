package net.wtako.Scrollie.Methods.Locations;

import java.text.MessageFormat;

import net.wtako.Scrollie.Methods.LocationSource;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class FactionHomeLocation implements LocationSource {

    private final Player player;
    private UPlayer      uplayer;

    public FactionHomeLocation(Player player) {
        this.player = player;
        try {
            uplayer = UPlayer.get(player);
        } catch (final Error e) {
            player.sendMessage(MessageFormat.format(Lang.ERROR_HOOKING.toString(), "Factions"));
            uplayer = null;
        }
    }

    @Override
    public Location get() {
        if (uplayer == null) {
            return null;
        }
        if (!uplayer.hasFaction()) {
            player.sendMessage(Lang.YOU_ARE_NOT_FACTION_MEMBER.toString());
            return null;
        }
        final Faction faction = uplayer.getFaction();
        if (faction.getHome() == null) {
            player.sendMessage(Lang.YOUR_FACTION_DOES_NOT_HAVE_HOME.toString());
            return null;
        }
        return faction.getHome().asBukkitLocation();
    }

}
