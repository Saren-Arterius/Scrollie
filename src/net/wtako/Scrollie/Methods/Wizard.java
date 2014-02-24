package net.wtako.Scrollie.Methods;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Wizard implements Listener {
    
    public abstract void begin();

    public abstract void end();

    private static final Map<String, Wizard> inWizardMode = new HashMap<String, Wizard>();

    private static void enter(Player player, Wizard wizard) {
        wizard.begin();
        if (inWizardMode.size() == 0) {
            player.sendMessage("EVENT REGISTERED");
            player.getServer().getPluginManager().registerEvents(wizard, player.getServer().getPluginManager().getPlugin("Scrollie"));
        }
        inWizardMode.put(player.getName(), wizard);
    }

    public static void enterOrLeave(Player player, Wizard wizard) {
        if (wizard == null) {
            return;
        }
        Wizard wizarding = inWizardMode.get(player.getName());
        if (wizarding == null) {
            enter(player, wizard);
        } else if (wizarding.getClass() == wizard.getClass()) {
            leave(player);
        } else {
            player.sendMessage("Player already in wizard!");
        }
    }

    public static boolean hasEditor(Player player) {
        return inWizardMode.containsKey(player.getName());
    }

    public static void leave(Player player) {
        if (!hasEditor(player)) {
            return;
        }
        Wizard wizard = inWizardMode.remove(player.getName());
        if (inWizardMode.size() == 0) {
            HandlerList.unregisterAll(wizard);
            player.sendMessage("EVENT UNREGISTERED");
        }
        wizard.end();
    }

    public static void leaveAll() {
        for (Entry<String, Wizard> entry : inWizardMode.entrySet()) {
            entry.getValue().end();
            if (inWizardMode.size() == 0) {
                HandlerList.unregisterAll(entry.getValue());
            }
        }
        inWizardMode.clear();
    }
}