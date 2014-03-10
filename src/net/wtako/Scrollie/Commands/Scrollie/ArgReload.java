package net.wtako.Scrollie.Commands.Scrollie;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.Commands.Create.Wizards.CreateWizard;
import net.wtako.Scrollie.Methods.Commands.Make.Wizards.PlayerClickWizard;
import net.wtako.Scrollie.Methods.Commands.Make.Wizards.SetScrollNameWizard;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;

public class ArgReload {

    public ArgReload(CommandSender sender) {
        if (!sender.hasPermission("Scrollie.reload")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return;
        }
        Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
        PlayerClickWizard.getInprocess().clear();
        SetScrollNameWizard.getInprocess().clear();
        CreateWizard.getPlayertoscroll().clear();
        Wizard.getInwizardmode().clear();
        Main.getInstance().getServer().getPluginManager().enablePlugin(Main.getInstance());
        sender.sendMessage(Lang.PLUGIN_RELOADED.toString());
    }

}
