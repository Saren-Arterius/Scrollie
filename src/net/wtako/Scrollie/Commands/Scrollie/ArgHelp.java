package net.wtako.Scrollie.Commands.Scrollie;

import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;

public class ArgHelp {

    public ArgHelp(CommandSender sender) {
        sender.sendMessage("Scrollie v0.0.1");
        sender.sendMessage("Author: Saren");
        sender.sendMessage(Lang.HELP_CREATE.toString());
        sender.sendMessage(Lang.HELP_MAKE.toString());
        sender.sendMessage(Lang.HELP_LIST.toString());
        sender.sendMessage(Lang.HELP_DELETE.toString());
        sender.sendMessage(Lang.HELP_RELOAD.toString());
    }

}
