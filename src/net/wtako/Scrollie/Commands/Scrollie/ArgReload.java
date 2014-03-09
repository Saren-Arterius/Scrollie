package net.wtako.Scrollie.Commands.Scrollie;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;

public class ArgReload {

    public ArgReload(CommandSender sender) {
        if (!sender.hasPermission("Scrollie.reload")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return;
        }
        Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
        Main.getInstance().getServer().getPluginManager().enablePlugin(Main.getInstance());
        sender.sendMessage(Lang.PLUGIN_RELOADED.toString());
    }

}
