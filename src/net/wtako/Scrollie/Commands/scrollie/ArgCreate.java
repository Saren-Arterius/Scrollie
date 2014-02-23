package net.wtako.Scrollie.Commands.scrollie;

import org.bukkit.entity.Player;

public class ArgCreate {

    private Player sender;

    public ArgCreate(Player sender) {
        this.sender = sender;
    }

    public void goToWizard() {
        sender.sendMessage("Just a test lol.");
    }

}