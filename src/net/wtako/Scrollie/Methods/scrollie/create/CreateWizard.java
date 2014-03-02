package net.wtako.Scrollie.Methods.scrollie.create;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CreateWizard extends Wizard {

    private final Player                     player;
    private static final Map<String, ScrollDatabase> playerToScroll = new HashMap<String, ScrollDatabase>();

    public CreateWizard(Player player) {
        this.player = player;
    }

    @Override
    public void begin() {
        ScrollDatabase wizardScroll = null;
        try {
            wizardScroll = new ScrollDatabase(player);
        } catch (SQLException e) {
            player.sendMessage(Lang.DB_EXCEPTION.toString());
            e.printStackTrace();
            Wizard.leave(player);
        }
        CreateWizard.playerToScroll.put(player.getName(), wizardScroll);
        player.sendMessage(Lang.WIZARD_ENTER.toString());
        sendNextMessage(wizardScroll, player);
    }

    @Override
    public void end() {
        player.sendMessage(Lang.WIZARD_EXIT.toString());
        CreateWizard.playerToScroll.remove(player.getName());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player chatPlayer = event.getPlayer();
        final ScrollDatabase wizardScroll = CreateWizard.playerToScroll.get(chatPlayer.getName());
        if (wizardScroll == null) {
            return;
        }
        final String input = event.getMessage();
        if (input.equalsIgnoreCase("exit")) {
            Wizard.leave(chatPlayer);
        } else if (wizardScroll.getDestinationType() == null) {
            chatPlayer.sendMessage(wizardScroll.setDestinationType(input, false));
            sendNextMessage(wizardScroll, chatPlayer);
        } else if (wizardScroll.getWarmUpTime() == null) {
            chatPlayer.sendMessage(wizardScroll.setWarmUpTime(input, false));
            sendNextMessage(wizardScroll, chatPlayer);
        } else if (wizardScroll.getCoolDownTime() == null) {
            chatPlayer.sendMessage(wizardScroll.setCoolDownTime(input, false));
            sendNextMessage(wizardScroll, chatPlayer);
        } else if (wizardScroll.getAllowCrossWorldTP() == null) {
            chatPlayer.sendMessage(wizardScroll.setAllowCrossWorldTP(input, false));
            sendNextMessage(wizardScroll, chatPlayer);
        } else if (wizardScroll.getTimesBeUsed() == null) {
            chatPlayer.sendMessage(wizardScroll.setTimesBeUsed(input, false));
            sendNextMessage(wizardScroll, chatPlayer);
        } else if (wizardScroll.getScrollName() == null) {
            chatPlayer.sendMessage(wizardScroll.setScrollName(input, false));
            sendNextMessage(wizardScroll, chatPlayer);
            if (wizardScroll.getScrollName() != null) {
                try {
                    chatPlayer.sendMessage(wizardScroll.save());
                } catch (SQLException e) {
                    chatPlayer.sendMessage(Lang.DB_EXCEPTION.toString());
                    e.printStackTrace();
                } finally {
                    Wizard.leave(chatPlayer);
                }
            }
        }
        event.setCancelled(true);
    }

    public String[] getDestinationMessage() {
        final List<String> messageList = new ArrayList<String>();
        messageList.add(Lang.WHAT_DESTINATION.toString());
        final List<String> enabledDestinationTypes = Main.getInstance().getConfig()
                .getStringList("variable.create.ScrollDestination.Enabled");
        final String messageTemplate = Lang.ALL_DESTINATIONS.toString();
        for (final String enabledDestinationType: enabledDestinationTypes) {
            final Integer destinationTypeInteger = ScrollDatabase.destinationTypeStringToInteger(enabledDestinationType);
            final String humanText = ScrollDatabase.destinationTypeIntegerToString(destinationTypeInteger);
            final String humanMessage = MessageFormat.format(messageTemplate, destinationTypeInteger,
                    Lang.TO_TEXT.toString(), humanText);
            messageList.add(humanMessage);
        }
        final String[] messageSimpleArray = messageList.toArray(new String[messageList.size()]);
        return messageSimpleArray;
    }

    public void sendNextMessage(ScrollDatabase scroll, Player player) {
        if (scroll.getDestinationType() == null) {
            player.sendMessage(getDestinationMessage());
        } else if (scroll.getWarmUpTime() == null) {
            player.sendMessage(Lang.WHAT_WOULD_THE_WARM_UP_TIME_BE.toString());
            player.sendMessage(Lang.SHORTER_TIME_MORE_EXP.toString());
        } else if (scroll.getCoolDownTime() == null) {
            player.sendMessage(Lang.WHAT_WOULD_THE_COOL_DOWN_TIME_BE.toString());
            player.sendMessage(Lang.SHORTER_TIME_MORE_EXP.toString());
        } else if (scroll.getAllowCrossWorldTP() == null) {
            player.sendMessage(Lang.ALLOW_CROSS_WORLD_TP_OR_NOT.toString());
            player.sendMessage(MessageFormat.format(Lang.WILL_BE_MULTIPLIED_BY.toString(), Main.getInstance()
                    .getConfig().getInt("variable.make.CrossWorldTPExpFactor")));
        } else if (scroll.getTimesBeUsed() == null) {
            player.sendMessage(Lang.HOW_MANY_TIMES_COULD_THIS_SCROLL_BE_USED.toString());
            player.sendMessage(MessageFormat.format(Lang.WILL_BE_MULTIPLIED_BY.toString(), "*TIMES*"));
        } else if (scroll.getScrollName() == null) {
            player.sendMessage(Lang.ENTER_NAME.toString());
        }
        return;
    }
}