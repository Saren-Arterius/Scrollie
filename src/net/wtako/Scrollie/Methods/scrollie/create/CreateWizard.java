package net.wtako.Scrollie.Methods.scrollie.create;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Scroll;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CreateWizard extends Wizard {

    private final Player                     player;
    private static final Map<String, Scroll> playerToScroll = new HashMap<String, Scroll>();

    public CreateWizard(Player player) {
        this.player = player;
    }

    @Override
    public void begin() {
        final Scroll wizardScroll = new Scroll();
        CreateWizard.playerToScroll.put(player.getName(), wizardScroll);
        player.sendMessage(Lang.WIZARD_ENTER.toString());
        player.sendMessage(Lang.WHAT_DESTINATION.toString());
        final List<String> enabledDestinationTypes = Main.getInstance().getConfig()
                .getStringList("variable.create.ScrollDestination.Enabled");
        final String messageTemplate = Lang.ALL_DESTINATIONS.toString();
        for (final String enabledDestinationType: enabledDestinationTypes) {
            final Integer destinationTypeInteger = wizardScroll.destinationTypeStringToInteger(enabledDestinationType);
            final String humanText = wizardScroll.destinationTypeIntegerToString(destinationTypeInteger);
            final String humanMessage = MessageFormat.format(messageTemplate, destinationTypeInteger,
                    Lang.TO_TEXT.toString(), humanText);
            player.sendMessage(humanMessage);
        }
    }

    @Override
    public void end() {
        player.sendMessage(Lang.WIZARD_EXIT.toString());
        CreateWizard.playerToScroll.remove(player.getName());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player chatPlayer = event.getPlayer();
        final Scroll wizardScroll = CreateWizard.playerToScroll.get(chatPlayer.getName());
        if (wizardScroll != null) {
            final String input = event.getMessage();
            if (input.equalsIgnoreCase("exit")) {
                Wizard.leave(chatPlayer);
            } else if (wizardScroll.getDestinationType() == null) {
                chatPlayer.sendMessage(wizardScroll.setDestinationType(input));
                if (wizardScroll.getDestinationType() == null) {
                    chatPlayer.sendMessage(Lang.WHAT_DESTINATION.toString());
                    final List<String> enabledDestinationTypes = Main.getInstance().getConfig()
                            .getStringList("variable.create.ScrollDestination.Enabled");
                    final String messageTemplate = Lang.ALL_DESTINATIONS.toString();
                    for (final String enabledDestinationType: enabledDestinationTypes) {
                        final Integer destinationTypeInteger = wizardScroll
                                .destinationTypeStringToInteger(enabledDestinationType);
                        final String humanText = wizardScroll.destinationTypeIntegerToString(destinationTypeInteger);
                        final String humanMessage = MessageFormat.format(messageTemplate, destinationTypeInteger,
                                Lang.TO_TEXT.toString(), humanText);
                        chatPlayer.sendMessage(humanMessage);
                    }
                } else {
                    chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_WARM_UP_TIME_BE.toString());
                }
            } else if (wizardScroll.getWarmUpTime() == null) {
                chatPlayer.sendMessage(wizardScroll.setWarmUpTime(input));
                if (wizardScroll.getWarmUpTime() == null) {
                    chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_WARM_UP_TIME_BE.toString());
                } else {
                    chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_COOL_DOWN_TIME_BE.toString());
                }
            } else if (wizardScroll.getCoolDownTime() == null) {
                chatPlayer.sendMessage(wizardScroll.setCoolDownTime(input));
                if (wizardScroll.getCoolDownTime() == null) {
                    chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_COOL_DOWN_TIME_BE.toString());
                } else {
                    Wizard.leave(chatPlayer);
                }
            }
            event.setCancelled(true);
        }
    }
}