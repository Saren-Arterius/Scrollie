package net.wtako.Scrollie.Commands.scrollie;

import java.sql.SQLException;

import net.wtako.Scrollie.Utils.Lang;
import net.wtako.Scrollie.Methods.scrollie.make.MakeProcess;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArgMake {

    private final CommandSender sender;
    private String[]            args;

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
                    } catch (SQLException e) {
                        sender.sendMessage(Lang.DB_EXCEPTION.toString());
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.HELP_MAKE.toString());
                }
            } else {
                try {
                    scrollID = Integer.parseInt(args[1]);
                    try {
                        new MakeProcess((Player) sender, scrollID).makeScroll();
                    } catch (SQLException e) {
                        sender.sendMessage(Lang.DB_EXCEPTION.toString());
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.HELP_MAKE.toString());
                }
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