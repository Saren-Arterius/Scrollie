package net.wtako.Scrollie.Commands.Scrollie;

import java.sql.SQLException;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArgList {

    public ArgList(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Main.getInstance().getProperty("artifactId") + ".make")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return;
        }
        try {
            sender.sendMessage(ScrollDatabase.listAll((Player) sender));
        } catch (final SQLException e) {
            e.printStackTrace();
            sender.sendMessage(Lang.DB_EXCEPTION.toString());
        }
    }
}
