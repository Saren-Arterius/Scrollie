package net.wtako.Scrollie.Methods.scrollie.create;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.wtako.Scrollie.Methods.Scroll;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Utils.Messages;

public class CreateWizard extends Wizard {
    
    private final Player player;
    private final Scroll scroll;

    public CreateWizard(Player player) {
        this.player = player;
        this.scroll = new Scroll();
    }

    @Override
    public void begin() {
        Messages messages = Messages.getInstance();
        player.sendMessage(messages.getMsg("general.create.Wizard"));
        player.sendMessage(messages.getMsg("general.create.WhatDestination"));
        player.sendMessage(messages.getMsg("general.create.AllDestinations"));
    }

    @Override
    public void end() {
        Messages messages = Messages.getInstance();
        player.sendMessage(messages.getMsg("general.create.WizardInterrupt"));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Messages messages = Messages.getInstance();
        String input = event.getMessage();
        if (this.scroll.getDestinationType() == null) {
            try {
                player.sendMessage(this.scroll.setDestinationType(Integer.parseInt(input)));
            } catch (Exception ex) {
                player.sendMessage(this.scroll.setDestinationType(input));
            }
            if (this.scroll.getDestinationType() == null) {
                player.sendMessage(messages.getMsg("general.create.WhatDestination"));
                player.sendMessage(messages.getMsg("general.create.AllDestinations"));
            }
        }
        event.setCancelled(true);
    }
}