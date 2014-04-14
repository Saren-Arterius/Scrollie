package net.wtako.Scrollie.Commands.Scrollie;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;

public class ArgHelp {

    public ArgHelp(CommandSender sender) {
        sender.sendMessage(Main.getInstance().getName() + " v" + Main.getInstance().getProperty("version"));
        sender.sendMessage("Author: " + Main.getInstance().getProperty("author"));
        sender.sendMessage(Lang.HELP_CREATE.toString());
        sender.sendMessage(Lang.HELP_MAKE.toString());
        sender.sendMessage(Lang.HELP_LIST.toString());
        sender.sendMessage(Lang.HELP_DELETE.toString());
        sender.sendMessage(Lang.HELP_RELOAD.toString());
    }

}
