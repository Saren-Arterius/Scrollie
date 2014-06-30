package net.wtako.Scrollie.Commands.Scrollie;

import java.sql.SQLException;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArgList {

    public ArgList(final CommandSender sender) {
        if (!sender.hasPermission(Main.getInstance().getProperty("artifactId") + ".make")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    sender.sendMessage(ScrollDatabase.listAll((Player) sender));
                } catch (final SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(Lang.DB_EXCEPTION.toString());
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

    }
}
