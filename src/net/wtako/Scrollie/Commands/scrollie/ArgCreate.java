package net.wtako.Scrollie.Commands.scrollie;

import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.scrollie.create.CreateWizard;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArgCreate {

    private CommandSender sender;

    public ArgCreate(CommandSender sender) {
        this.sender = sender;
    }

    public void goToWizard() {
        Wizard.enterOrLeave((Player) sender, new CreateWizard((Player) sender));
    }

}