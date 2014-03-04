package net.wtako.Scrollie.Commands.scrollie;

import java.sql.SQLException;

import net.wtako.Scrollie.Methods.scrollie.make.MakeProcess;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArgMake {

    private final CommandSender sender;
    private final String[]      args;

    public ArgMake(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public void goToWizard() {
        if (canMakeScroll()) {
            Integer scrollID = null;
            if (args.length >= 3) {
                Integer timesBeUsed = null;
                try {
                    scrollID = Integer.parseInt(args[1]);
                    timesBeUsed = Integer.parseInt(args[2]);
                    try {
                        new MakeProcess((Player) sender, scrollID, timesBeUsed).makeScroll();
                    } catch (final SQLException e) {
                        sender.sendMessage(Lang.DB_EXCEPTION.toString());
                        e.printStackTrace();
                    }
                } catch (final NumberFormatException e) {
                    sender.sendMessage(Lang.HELP_MAKE.toString());
                }
            } else if (args.length == 2) {
                try {
                    scrollID = Integer.parseInt(args[1]);
                    try {
                        new MakeProcess((Player) sender, scrollID).makeScroll();
                    } catch (final SQLException e) {
                        sender.sendMessage(Lang.DB_EXCEPTION.toString());
                        e.printStackTrace();
                    }
                } catch (final NumberFormatException e) {
                    sender.sendMessage(Lang.HELP_MAKE.toString());
                }
            } else {
                sender.sendMessage(Lang.HELP_MAKE.toString());
            }
        }
    }

    public boolean canMakeScroll() {
        if (!sender.hasPermission("Scrollie.make")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return false;
        } else {
            return true;
        }
    }
}