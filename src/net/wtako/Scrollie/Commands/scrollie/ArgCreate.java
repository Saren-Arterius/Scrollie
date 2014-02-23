package net.wtako.Scrollie.Commands.scrollie;

import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.scrollie.create.CreateWizard;

import org.bukkit.entity.Player;

public class ArgCreate {

    private Player sender;

    public ArgCreate(Player sender) {
        this.sender = sender;
    }

    public void goToWizard() {
        Wizard.enterOrLeave(sender, new CreateWizard(sender));
    }

}