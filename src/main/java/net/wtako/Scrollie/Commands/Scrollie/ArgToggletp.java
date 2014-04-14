package net.wtako.Scrollie.Commands.Scrollie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Database;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;

public class ArgToggletp {

    private final CommandSender sender;

    public ArgToggletp(CommandSender sender) {
        this.sender = sender;
        toggleTP();
    }

    private void toggleTP() {
        if (!sender.hasPermission(Main.getInstance().getProperty("artifactId") + ".canToggleTP")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return;
        }
        try {
            final PreparedStatement selStmt = Database.getInstance().conn
                    .prepareStatement("SELECT * FROM 'tp_denies' WHERE player = ?");
            selStmt.setString(1, sender.getName());
            final ResultSet result = selStmt.executeQuery();
            if (result.next()) {
                final PreparedStatement delStmt = Database.getInstance().conn
                        .prepareStatement("DELETE FROM 'tp_denies' WHERE player = ?");
                delStmt.setString(1, sender.getName());
                delStmt.execute();
                delStmt.close();
                sender.sendMessage(Lang.YOU_TURNED_ON_TP.toString());
            } else {
                final PreparedStatement insStmt = Database.getInstance().conn
                        .prepareStatement("INSERT INTO 'tp_denies' ('player') VALUES (?)");
                insStmt.setString(1, sender.getName());
                insStmt.execute();
                insStmt.close();
                sender.sendMessage(Lang.YOU_TURNED_OFF_TP.toString());
            }
            result.close();
            selStmt.close();
        } catch (final SQLException e) {
            sender.sendMessage(Lang.DB_EXCEPTION.toString());
            e.printStackTrace();
        }
    }
}
