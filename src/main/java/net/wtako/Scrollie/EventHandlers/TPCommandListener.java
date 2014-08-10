package net.wtako.Scrollie.EventHandlers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Database;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Methods.Commands.Make.MakeProcess;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TPCommandListener implements Listener {

    @EventHandler
    public void onSpawnCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().equalsIgnoreCase("/spawn")) {
            return;
        }
        if (event.getPlayer().hasPermission(Main.getInstance().getProperty("artifactId") + ".admin")) {
            return;
        }
        event.setCancelled(true);
        Integer spawnScrollID;
        try {
            spawnScrollID = TPCommandListener.getSpawnScrollID(event.getPlayer());
            if (spawnScrollID == null) {
                TPCommandListener.createSpawnScroll(event.getPlayer());
                spawnScrollID = TPCommandListener.getSpawnScrollID(event.getPlayer());
            }
            new MakeProcess(event.getPlayer(), spawnScrollID).makeScroll();
        } catch (final SQLException e) {
            event.getPlayer().sendMessage(Lang.DB_EXCEPTION.toString());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onHomeCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().equalsIgnoreCase("/home")) {
            return;
        }
        if (event.getPlayer().hasPermission(Main.getInstance().getProperty("artifactId") + ".admin")) {
            return;
        }
        event.setCancelled(true);
        event.getPlayer().sendMessage(Lang.PLEASE_USE_SCROLLIE.toString());
        event.getPlayer().sendMessage(Lang.HELP_CREATE.toString());
        event.getPlayer().sendMessage(Lang.HELP_MAKE.toString());
        event.getPlayer().sendMessage(Lang.TUTORIAL.toString());
    }

    @EventHandler
    public void onTPCommands(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().toLowerCase().startsWith("/tp")) {
            return;
        }
        if (event.getPlayer().hasPermission(Main.getInstance().getProperty("artifactId") + ".admin")) {
            return;
        }
        event.setCancelled(true);
        event.getPlayer().sendMessage(Lang.PLEASE_USE_SCROLLIE.toString());
        event.getPlayer().sendMessage(Lang.HELP_CREATE.toString());
        event.getPlayer().sendMessage(Lang.HELP_MAKE.toString());
        event.getPlayer().sendMessage(Lang.TUTORIAL_TPA.toString());
        event.getPlayer().sendMessage(Lang.TUTORIAL.toString());
    }

    public static Integer getSpawnScrollID(Player player) throws SQLException {
        final PreparedStatement selStmt = Database.getInstance().conn
                .prepareStatement("SELECT scroll_id FROM 'scroll_creations' WHERE owner = ? AND scroll_destination = ?");
        selStmt.setString(1, player.getName().toLowerCase());
        selStmt.setInt(2, 6);
        final ResultSet result = selStmt.executeQuery();
        if (!result.next()) {
            result.close();
            selStmt.close();
            return null;
        }
        final int scrollID = result.getInt(1);
        result.close();
        selStmt.close();
        return scrollID;
    }

    public static void createSpawnScroll(Player player) throws SQLException {
        final ScrollDatabase scroll = new ScrollDatabase(player);
        scroll.setDestinationType("6", true);
        scroll.setScrollName(Main.getInstance().getConfig().getString("variable.create.EnterName.Default"), true);
        scroll.save();
    }

}
