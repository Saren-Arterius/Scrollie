package net.wtako.Scrollie.Methods.scrollie.make.Locations;

import java.util.HashMap;
import java.util.Map;

import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.scrollie.make.MakeProcess;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
        player.sendMessage(Lang.EXIT_MAKING.toString());
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
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_AIR))  {
            Wizard.leave(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player)event.getDamager();
            final MakeProcess process = PlayerClickWizard.inProcess.get(player.getName());
            if (process != null) {
                if (event.getEntity() instanceof Player) {
                    final Player target = (Player)event.getEntity();
                    player.sendMessage(target.getName());
                    event.setCancelled(true);
                }
            }
        }
    }
}