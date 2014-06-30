package net.wtako.Scrollie.Methods;

import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

public class FactionLocationChecker {

    public static boolean checkFaction(Player player, Location destLoc) {
        if (FactionLocationChecker.canTeleportTo(player, destLoc)
                && FactionLocationChecker.canTeleportFrom(player)) {
            return true;
        }
        return false;
    }

    private static boolean canTeleportTo(Player player, Location destLoc) {
        if (player.hasPermission(Main.getInstance().getProperty("artifactId") + ".canUseScrollInRestrictedAreas")) {
            return true;
        }
        final UPlayer factionPlayer = UPlayer.get(player);
        final Faction playerSelfFaction = factionPlayer.getFaction();
        final Faction destFaction = BoardColls.get().getFactionAt(PS.valueOf(destLoc));
        if (destFaction.isNone()) {
            return true;
        }
        if (destFaction.getFlag(FFlag.PEACEFUL) && destFaction.getFlag(FFlag.PERMANENT)) {
            return true;
        }
        if (destFaction == playerSelfFaction) {
            return true;
        }
        final String relationWish = destFaction.getRelationWish(playerSelfFaction).toString();
        final String node = MessageFormat.format("variable.use.Factions.{0}.CanTeleportToTerritories", relationWish);
        if (!Main.getInstance().getConfig().getBoolean(node)) {
            if (Main.getInstance().getConfig()
                    .getBoolean("variable.use.Factions.IfSelfFactionPowerIsHigherThenOverrideTPTo")
                    && FactionLocationChecker.isPowerHigher(playerSelfFaction, destFaction)) {
                player.sendMessage(Lang.YOUR_SCROLL_STILL_WORKS.toString());
                return true;
            }
            player.sendMessage(MessageFormat.format(Lang.CANT_TP_TO_ENEMY_TERRITORY.toString(), destFaction.getName()));
            return false;
        }
        return true;
    }

    public static boolean canTeleportFrom(Player player) {
        if (player.hasPermission(Main.getInstance().getProperty("artifactId") + ".canUseScrollInRestrictedAreas")) {
            return true;
        }
        final UPlayer factionPlayer = UPlayer.get(player);
        final Faction playerSelfFaction = factionPlayer.getFaction();

        if (playerSelfFaction.getFlag(FFlag.PEACEFUL)) {
            return true;
        }
        final Faction sourceLocationFaction = BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()));
        if (sourceLocationFaction.isNone()) {
            return true;
        }
        if (sourceLocationFaction == playerSelfFaction) {
            return true;
        }
        final String relationWish = sourceLocationFaction.getRelationWish(playerSelfFaction).toString();
        final String node = MessageFormat.format("variable.use.Factions.{0}.CanTeleportFromTerritories", relationWish);
        if (!Main.getInstance().getConfig().getBoolean(node)) {
            if (Main.getInstance().getConfig()
                    .getBoolean("variable.use.Factions.IfSelfFactionPowerIsHigherThenOverrideTPFrom")
                    && FactionLocationChecker.isPowerHigher(playerSelfFaction, sourceLocationFaction)) {
                player.sendMessage(Lang.YOUR_SCROLL_STILL_WORKS.toString());
                return true;
            }
            player.sendMessage(MessageFormat.format(Lang.CANT_TP_FROM_ENEMY_TERRITORY.toString(),
                    sourceLocationFaction.getName()));
            return false;
        }
        return true;
    }

    public static boolean checkIfCanTeleportPlayer(Player teleporter, Player target) {
        if (teleporter.hasPermission(Main.getInstance().getProperty("artifactId") + ".canUseScrollInRestrictedAreas")) {
            return true;
        }
        final UPlayer teleporterFactionPlayer = UPlayer.get(teleporter);
        final UPlayer targetFactionPlayer = UPlayer.get(target);
        final Faction teleporterSelfFaction = teleporterFactionPlayer.getFaction();
        final Faction targetPlayerFaction = targetFactionPlayer.getFaction();
        if (targetPlayerFaction == teleporterSelfFaction) {
            return Main.getInstance().getConfig().getBoolean("variable.make.CanTeleportToFactionPlayer.SELF");
        }
        final String relationWish = targetPlayerFaction.getRelationWish(teleporterSelfFaction).toString();
        final String node = MessageFormat.format("variable.make.CanTeleportToFactionPlayer.{0}", relationWish);
        if (!Main.getInstance().getConfig().getBoolean(node)) {
            if (Main.getInstance().getConfig().getBoolean("variable.make.IfSelfFactionPowerIsHigherThenOverrideTPTo")
                    && FactionLocationChecker.isPowerHigher(teleporterSelfFaction, targetPlayerFaction)) {
                teleporter.sendMessage(MessageFormat.format(Lang.YOU_CAN_TP_TO_HIM_BECAUSE_POWER_HIGHER.toString(),
                        target.getName()));
                return true;
            }
            teleporter.sendMessage(MessageFormat.format(Lang.YOU_CANT_TP_TO_HIM_BECAUSE_FACTION_BAD.toString(),
                    target.getName()));
            return false;
        }
        return true;
    }

    private static boolean isPowerHigher(Faction f1, Faction f2) {
        return f1.getPower() > f2.getPower();
    }
}
