package net.wtako.Scrollie.Methods;

import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

public class FactionLocationChecker {

    public static boolean checkFaction(Player player, Location destLoc) {
        if (player.hasPermission("Scrollie.canUseScrollInRestrictedAreas")) {
            return true;
        }
        if (FactionLocationChecker.checkIfCanTeleportTo(player, destLoc)
                && FactionLocationChecker.checkIfCanTeleportFrom(player)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkIfCanTeleportTo(Player player, Location destLoc) {
        final UPlayer factionPlayer = UPlayer.get(player);
        final Faction playerSelfFaction = factionPlayer.getFaction();
        final Faction destFaction = BoardColls.get().getFactionAt(PS.valueOf(destLoc));
        final Faction wilderness = FactionColls.get().getForUniverse(factionPlayer.getUniverse()).getNone();
        if (destFaction == wilderness) {
            return true;
        }
        if (destFaction == playerSelfFaction) {
            return Main.getInstance().getConfig().getBoolean("variable.use.Factions.SELF.CanTeleportToTerritories");
        } else {
            final String relationWish = destFaction.getRelationWish(playerSelfFaction).toString();
            final String node = MessageFormat
                    .format("variable.use.Factions.{0}.CanTeleportToTerritories", relationWish);
            if (!Main.getInstance().getConfig().getBoolean(node)) {
                if (Main.getInstance().getConfig()
                        .getBoolean("variable.use.Factions.IfSelfFactionPowerIsHigherThenOverrideTPTo")
                        && FactionLocationChecker.isPowerHigher(playerSelfFaction, destFaction)) {
                    player.sendMessage(Lang.YOUR_SCROLL_STILL_WORKS.toString());
                    return true;
                } else {
                    player.sendMessage(Lang.CANT_TP_TO_ENEMY_TERRITORY.toString());
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public static boolean checkIfCanTeleportFrom(Player player) {
        final UPlayer factionPlayer = UPlayer.get(player);
        final Faction playerSelfFaction = factionPlayer.getFaction();
        final Faction sourceLocationFaction = BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()));
        final Faction wilderness = FactionColls.get().getForUniverse(factionPlayer.getUniverse()).getNone();
        if (sourceLocationFaction == wilderness) {
            return true;
        }
        if (sourceLocationFaction == playerSelfFaction) {
            return Main.getInstance().getConfig().getBoolean("variable.use.Factions.SELF.CanTeleportFromTerritories");
        } else {
            final String relationWish = sourceLocationFaction.getRelationWish(playerSelfFaction).toString();
            final String node = MessageFormat.format("variable.use.Factions.{0}.CanTeleportFromTerritories",
                    relationWish);
            if (!Main.getInstance().getConfig().getBoolean(node)) {
                if (Main.getInstance().getConfig()
                        .getBoolean("variable.use.Factions.IfSelfFactionPowerIsHigherThenOverrideTPFrom")
                        && FactionLocationChecker.isPowerHigher(playerSelfFaction, sourceLocationFaction)) {
                    player.sendMessage(Lang.YOUR_SCROLL_STILL_WORKS.toString());
                    return true;
                } else {
                    player.sendMessage(Lang.CANT_TP_FROM_ENEMY_TERRITORY.toString());
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public static boolean checkIfCanTeleportPlayer(Player teleporter, Player target) {
        final UPlayer teleporterFactionPlayer = UPlayer.get(teleporter);
        final UPlayer targetFactionPlayer = UPlayer.get(target);
        final Faction teleporterSelfFaction = teleporterFactionPlayer.getFaction();
        final Faction targetPlayerFaction = targetFactionPlayer.getFaction();
        if (targetPlayerFaction == teleporterSelfFaction) {
            return Main.getInstance().getConfig().getBoolean("variable.make.CanTeleportToFactionPlayer.SELF");
        } else {
            final String relationWish = targetPlayerFaction.getRelationWish(teleporterSelfFaction).toString();
            final String node = MessageFormat.format("variable.make.CanTeleportToFactionPlayer.{0}", relationWish);
            if (!Main.getInstance().getConfig().getBoolean(node)) {
                if (Main.getInstance().getConfig()
                        .getBoolean("variable.make.IfSelfFactionPowerIsHigherThenOverrideTPTo")
                        && FactionLocationChecker.isPowerHigher(teleporterSelfFaction, targetPlayerFaction)) {
                    teleporter.sendMessage(MessageFormat.format(Lang.YOU_CAN_TP_TO_HIM_BECAUSE_POWER_HIGHER.toString(),
                            target.getName()));
                    return true;
                } else {
                    teleporter.sendMessage(MessageFormat.format(Lang.YOU_CANT_TP_TO_HIM_BECAUSE_FACTION_BAD.toString(),
                            target.getName()));
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    private static boolean isPowerHigher(Faction f1, Faction f2) {
        return f1.getPower() > f2.getPower();
    }
}
