package net.wtako.Scrollie.Methods.scrollie.create;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.wtako.Scrollie.Methods.Scroll;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Utils.Lang;

public class CreateWizard extends Wizard {

    private final Player player;
    private static final Map<Player, Scroll> playerToScroll = new HashMap<Player, Scroll>();

    public CreateWizard(Player player) {
        this.player = player;
    }

    @Override
    public void begin() {
        playerToScroll.put(player, new Scroll());
        player.sendMessage(Lang.WIZARD_ENTER.toString());
        player.sendMessage(Lang.WHAT_DESTINATION.toString());
        player.sendMessage(Lang.ALL_DESTINATIONS.toString());
    }

    @Override
    public void end() {
        player.sendMessage(Lang.WIZARD_EXIT.toString());
        playerToScroll.remove(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player chatPlayer = event.getPlayer();
        Scroll wizardScroll = playerToScroll.get(chatPlayer);
        String input = event.getMessage();
        if (wizardScroll != null) {
            if (wizardScroll.getDestinationType() == null) {
                chatPlayer.sendMessage(wizardScroll.setDestinationType(input));
                if (wizardScroll.getDestinationType() == null) {
                    chatPlayer.sendMessage(Lang.WHAT_DESTINATION.toString());
                    chatPlayer.sendMessage(Lang.ALL_DESTINATIONS.toString());
                } else {
                    chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_WARM_UP_TIME_BE
                            .toString());
                }
            } else if (wizardScroll.getWarmUpTime() == null) {
                chatPlayer.sendMessage(wizardScroll.setWarmUpTime(input));
                if (wizardScroll.getWarmUpTime() == null) {
                    chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_WARM_UP_TIME_BE
                            .toString());
                } else {
                    chatPlayer
                            .sendMessage(Lang.WHAT_WOULD_THE_COOL_DOWN_TIME_BE
                                    .toString());
                }
            } else if (wizardScroll.getCoolDownTime() == null) {
                chatPlayer.sendMessage(wizardScroll.setCoolDownTime(input));
                if (wizardScroll.getCoolDownTime() == null) {
                    chatPlayer
                            .sendMessage(Lang.WHAT_WOULD_THE_COOL_DOWN_TIME_BE
                                    .toString());
                } else {
                    leave(chatPlayer);
                }
            }
            event.setCancelled(true);
        }
    }
}