package net.wtako.Scrollie.Methods.scrollie.create;

import java.text.MessageFormat;
import java.util.ArrayList;
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
        final Scroll wizardScroll = new Scroll(this.player);
        CreateWizard.playerToScroll.put(player.getName(), wizardScroll);
        player.sendMessage(Lang.WIZARD_ENTER.toString());
        setDefaultValue();
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
        if (wizardScroll == null) {
            return;
        }
        final String input = event.getMessage();
        if (input.equalsIgnoreCase("exit")) {
            Wizard.leave(chatPlayer);
        } else if (wizardScroll.getDestinationType() == null) {
            chatPlayer.sendMessage(wizardScroll.setDestinationType(input));
            if (wizardScroll.getDestinationType() == null) {
                chatPlayer.sendMessage(getDestinationMessage());
            } else {
                chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_WARM_UP_TIME_BE.toString());
                chatPlayer.sendMessage(Lang.SHORTER_TIME_MORE_EXP.toString());
            }
        } else if (wizardScroll.getWarmUpTime() == null) {
            chatPlayer.sendMessage(wizardScroll.setWarmUpTime(input));
            if (wizardScroll.getWarmUpTime() == null) {
                chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_WARM_UP_TIME_BE.toString());
                chatPlayer.sendMessage(Lang.SHORTER_TIME_MORE_EXP.toString());
            } else {
                chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_COOL_DOWN_TIME_BE.toString());
                chatPlayer.sendMessage(Lang.SHORTER_TIME_MORE_EXP.toString());
            }
        } else if (wizardScroll.getCoolDownTime() == null) {
            chatPlayer.sendMessage(wizardScroll.setCoolDownTime(input));
            if (wizardScroll.getCoolDownTime() == null) {
                chatPlayer.sendMessage(Lang.WHAT_WOULD_THE_COOL_DOWN_TIME_BE.toString());
                chatPlayer.sendMessage(Lang.SHORTER_TIME_MORE_EXP.toString());
            } else {
                if (wizardScroll.getAllowCrossWorldTP() == null) {
                    chatPlayer.sendMessage(Lang.ALLOW_CROSS_WORLD_TP_OR_NOT.toString());
                    chatPlayer.sendMessage(MessageFormat.format(Lang.WILL_BE_MULTIPLIED_BY.toString(), Main
                            .getInstance().getConfig().getInt("variable.make.CrossWorldTPExpFactor")));
                } else {
                    chatPlayer.sendMessage(Lang.HOW_MANY_TIMES_COULD_THIS_SCROLL_BE_USED.toString());
                    chatPlayer.sendMessage(MessageFormat.format(Lang.WILL_BE_MULTIPLIED_BY.toString(), "*TIMES*"));
                }
            }
        } else if (wizardScroll.getAllowCrossWorldTP() == null) {
            chatPlayer.sendMessage(wizardScroll.setAllowCrossWorldTP(input));
            if (wizardScroll.getAllowCrossWorldTP() == null) {
                chatPlayer.sendMessage(Lang.ALLOW_CROSS_WORLD_TP_OR_NOT.toString());
                chatPlayer.sendMessage(MessageFormat.format(Lang.WILL_BE_MULTIPLIED_BY.toString(), Main.getInstance()
                        .getConfig().getInt("variable.make.CrossWorldTPExpFactor")));
            } else {
                chatPlayer.sendMessage(Lang.HOW_MANY_TIMES_COULD_THIS_SCROLL_BE_USED.toString());
                chatPlayer.sendMessage(MessageFormat.format(Lang.WILL_BE_MULTIPLIED_BY.toString(), "*TIMES*"));
            }
        } else if (wizardScroll.getTimesBeUsed() == null) {
            chatPlayer.sendMessage(wizardScroll.setTimesBeUsed(input));
            if (wizardScroll.getTimesBeUsed() == null) {
                chatPlayer.sendMessage(Lang.HOW_MANY_TIMES_COULD_THIS_SCROLL_BE_USED.toString());
                chatPlayer.sendMessage(MessageFormat.format(Lang.WILL_BE_MULTIPLIED_BY.toString(), "*TIMES*"));
            } else {
                chatPlayer.sendMessage(Lang.FINISHED_CREATING.toString());
                chatPlayer.sendMessage(Lang.MAKE_THIS_SCROLL.toString());
                chatPlayer.sendMessage(Lang.VIEW_SCROLL_LIST.toString());
                chatPlayer.sendMessage(Lang.DELETE_THIS_SCROLL.toString());
                Wizard.leave(chatPlayer);
            }
        }
        event.setCancelled(true);
    }

    public String[] getDestinationMessage() {
        List<String> messageList = new ArrayList<String>();
        messageList.add(Lang.WHAT_DESTINATION.toString());
        final List<String> enabledDestinationTypes = Main.getInstance().getConfig()
                .getStringList("variable.create.ScrollDestination.Enabled");
        final String messageTemplate = Lang.ALL_DESTINATIONS.toString();
        for (final String enabledDestinationType: enabledDestinationTypes) {
            final Integer destinationTypeInteger = Scroll.destinationTypeStringToInteger(enabledDestinationType);
            final String humanText = Scroll.destinationTypeIntegerToString(destinationTypeInteger);
            final String humanMessage = MessageFormat.format(messageTemplate, destinationTypeInteger,
                    Lang.TO_TEXT.toString(), humanText);
            messageList.add(humanMessage);
        }
        String[] messageSimpleArray = new String[messageList.size()];
        return messageSimpleArray;
    }

    private void setDefaultValue() {
        // TODO Auto-generated method stub
    }
}