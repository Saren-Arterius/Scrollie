package net.wtako.Scrollie.Commands.Scrollie;

import java.sql.SQLException;

import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArgDelete {

    private int rowID;

    public ArgDelete(CommandSender sender, String[] args) {
        if (!sender.hasPermission("Scrollie.make")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return;
        }
        if (args.length >= 2) {
            try {
                try {
                    rowID = Integer.parseInt(args[1]);
                } catch (final NumberFormatException e) {
                    sender.sendMessage(Lang.DELETE_USAGE.toString());
                    return;
                }
                sender.sendMessage(ScrollDatabase.delete(rowID, (Player) sender));
            } catch (final SQLException e) {
                e.printStackTrace();
                sender.sendMessage(Lang.DB_EXCEPTION.toString());
            }
        } else {
            sender.sendMessage(Lang.DELETE_USAGE.toString());
        }
    }
}
