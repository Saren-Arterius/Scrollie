package net.wtako.Scrollie.Methods.Commands.Make.Wizards;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.FactionLocationChecker;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.Commands.Make.MakeProcess;
import net.wtako.Scrollie.Methods.Locations.PlayerLocation;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerClickWizard extends Wizard {

    private final Player                          player;
    private final MakeProcess                     process;
    private static final Map<String, MakeProcess> inProcess = new HashMap<String, MakeProcess>();

    public PlayerClickWizard(Player player, MakeProcess process) {
        this.player = player;
        this.process = process;
    }

    @Override
    public void begin() {
        PlayerClickWizard.inProcess.put(player.getName(), process);
        player.sendMessage(Lang.PLEASE_CLICK_ON_A_PLAYER.toString());
        player.sendMessage(Lang.HOW_TO_CANCEL.toString());
    }

    @Override
    public void end() {
        player.sendMessage(Lang.WIZARD_EXIT.toString());
        PlayerClickWizard.inProcess.remove(player.getName());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final MakeProcess process = PlayerClickWizard.inProcess.get(player.getName());
        if (process == null) {
            return;
        }
        final String input = event.getMessage();
        if (input.equalsIgnoreCase("exit")) {
            player.sendMessage(Lang.EXIT_MAKING.toString());
            Wizard.leave(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final MakeProcess process = PlayerClickWizard.inProcess.get(player.getName());
        if (process == null) {
            return;
        }
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_AIR)) {
            player.sendMessage(Lang.EXIT_MAKING.toString());
            Wizard.leave(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerLeftClick(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player) event.getDamager();
            final MakeProcess process = PlayerClickWizard.inProcess.get(player.getName());
            if ((process != null) && (event.getEntity() instanceof Player)) {
                event.setCancelled(true);
                final Player target = (Player) event.getEntity();
                final String itemTypeRequiredString = Main.getInstance().getConfig()
                        .getString("variable.make.ScrollItem");
                final Material itemTypeRequired = Material.getMaterial(itemTypeRequiredString.toUpperCase());
                if (!player.getItemInHand().isSimilar(new ItemStack(itemTypeRequired, 1))
                        && !player.hasPermission("Scrollie.noCostRequiredToMake")) {
                    final String msg = Lang.PLEASE_HOLD_ITEM.toString();
                    player.sendMessage(MessageFormat.format(msg, itemTypeRequiredString));
                    return;
                }
                if (target.hasPermission("Scrollie.cantBeTeleportTarget")) {
                    player.sendMessage(Lang.NOT_PREMITTED_TO_TELEPORT_TO_THAT_PLAYER.toString());
                    return;
                }
                if (PlayerLocation.hasTargetTurnedOffTP(target.getName())) {
                    player.sendMessage(MessageFormat.format(Lang.TARGET_HAS_TURNED_TP_OFF.toString(), target.getName()));
                    return;
                }
                if (Main.getInstance().getConfig().getBoolean("system.FactionsSupport")
                        && !FactionLocationChecker.checkIfCanTeleportPlayer(player, target)) {
                    return;
                }
                process.targetName = target.getName();
                if (process.magickScroll(true)) {
                    Wizard.leave((Player) event.getDamager());
                }
                return;
            }
        }
    }
}