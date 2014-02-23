package net.wtako.Scrollie.Methods;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.wtako.Scrollie.Utils.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Wizard implements Listener {
    public abstract void begin();

    public abstract void end();

    private static final Map<String, Wizard> inWizardMode = new HashMap<String, Wizard>();

    private static void enter(Player player, Wizard editor) {
        editor.begin();
        player.getServer().getPluginManager().registerEvents(editor, player.getServer().getPluginManager().getPlugin("Scrollie"));
        inWizardMode.put(player.getName(), editor);
    }

    public static void enterOrLeave(Player player, Wizard editor) {
        if (editor == null) {
            return;
        }
        Wizard edit = inWizardMode.get(player.getName());
        if (edit == null) {
            enter(player, editor);
        } else if (edit.getClass() == editor.getClass()) {
            leave(player);
        } else {
            Messaging messager = new Messaging("Player already in wizard!");
            messager.sendTo(player);
        }
    }

    public static boolean hasEditor(Player player) {
        return inWizardMode.containsKey(player.getName());
    }

    public static void leave(Player player) {
        if (!hasEditor(player)) {
            return;
        }
        Wizard editor = inWizardMode.remove(player.getName());
        HandlerList.unregisterAll(editor);
        editor.end();
    }

    public static void leaveAll() {
        for (Entry<String, Wizard> entry : inWizardMode.entrySet()) {
            entry.getValue().end();
            HandlerList.unregisterAll(entry.getValue());
        }
        inWizardMode.clear();
    }
}