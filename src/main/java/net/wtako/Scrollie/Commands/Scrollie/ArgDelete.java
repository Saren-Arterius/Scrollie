package net.wtako.Scrollie.Commands.Scrollie;

import java.sql.SQLException;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArgDelete {

    private int rowID;

    public ArgDelete(final CommandSender sender, final String[] args) {
        if (!sender.hasPermission(Main.getInstance().getProperty("artifactId") + ".make")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
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
        }.runTaskAsynchronously(Main.getInstance());

    }
}
