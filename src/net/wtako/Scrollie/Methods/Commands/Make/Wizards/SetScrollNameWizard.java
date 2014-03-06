package net.wtako.Scrollie.Methods.Commands.Make.Wizards;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.Commands.Make.MakeProcess;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class SetScrollNameWizard extends Wizard {

    private final Player                          player;
    private final MakeProcess                     process;
    private static final Map<String, MakeProcess> inProcess = new HashMap<String, MakeProcess>();

    public SetScrollNameWizard(Player player, MakeProcess process) {
        this.player = player;
        this.process = process;
    }

    @Override
    public void begin() {
        SetScrollNameWizard.inProcess.put(player.getName(), process);
        player.sendMessage(Lang.WIZARD_ENTER.toString());
        player.sendMessage(Lang.ENTER_NAME.toString());
    }

    @Override
    public void end() {
        player.sendMessage(Lang.WIZARD_EXIT.toString());
        SetScrollNameWizard.inProcess.remove(player.getName());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final MakeProcess process = SetScrollNameWizard.inProcess.get(player.getName());
        if (process == null) {
            return;
        }
        event.setCancelled(true);
        final String itemTypeRequiredString = Main.getInstance().getConfig().getString("variable.make.ScrollItem");
        final Material itemTypeRequired = Material.getMaterial(itemTypeRequiredString.toUpperCase());
        if (!player.getItemInHand().isSimilar(new ItemStack(itemTypeRequired, 1))
                && !player.hasPermission("Scrollie.noCostRequiredToMake")) {
            final String msg = Lang.PLEASE_HOLD_ITEM.toString();
            player.sendMessage(MessageFormat.format(msg, itemTypeRequiredString));
            return;
        }
        final String input = event.getMessage();
        if (input.equalsIgnoreCase("exit")) {
            player.sendMessage(Lang.EXIT_MAKING.toString());
            Wizard.leave(player);
        } else {
            player.sendMessage(process.setScrollName(input, false));
            if (process.getScrollName() != null && process.magickScroll(true)) {
                Wizard.leave(player);
            } else {
                player.sendMessage(Lang.ENTER_NAME.toString());
            }
        }
    }
}