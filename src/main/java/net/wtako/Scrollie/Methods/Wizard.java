package net.wtako.Scrollie.Methods;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

public abstract class Wizard implements Listener {

    public abstract void begin();

    public abstract void end();

    private static final Map<String, Wizard> inWizardMode = new HashMap<String, Wizard>();

    private static void enter(Player player, Wizard wizard) {
        wizard.begin();

        boolean needToRegisterEvent = true;
        final String wizardInstanceClassName = wizard.toString().split("@")[0];
        for (final RegisteredListener listener: HandlerList.getRegisteredListeners(Main.getInstance())) {
            if (listener.getListener().toString().contains(wizardInstanceClassName)) {
                needToRegisterEvent = false;
                break;
            }
        }
        if (needToRegisterEvent) {
            player.getServer().getPluginManager().registerEvents(wizard, Main.getInstance());
        }
        Wizard.inWizardMode.put(player.getName(), wizard);
    }

    public static void enterOrLeave(Player player, Wizard wizard) {
        if (wizard == null) {
            return;
        }
        final Wizard wizarding = Wizard.inWizardMode.get(player.getName());
        if (wizarding == null) {
            Wizard.enter(player, wizard);
        } else if (wizarding.getClass() == wizard.getClass()) {
            Wizard.leave(player);
        } else {
            player.sendMessage(Lang.ALREADY_IN_WIZARD.toString());
        }
    }

    public static boolean hasEditor(Player player) {
        return Wizard.inWizardMode.containsKey(player.getName());
    }

    public static void leave(Player player) {
        if (!Wizard.hasEditor(player)) {
            return;
        }
        boolean needToUnregisterEvent = true;
        final Wizard wizard = Wizard.inWizardMode.remove(player.getName());
        for (final Entry<String, Wizard> entry: Wizard.inWizardMode.entrySet()) {
            if (entry.getValue().toString().split("@")[0].equals(wizard.toString().split("@")[0])) {
                needToUnregisterEvent = false;
                break;
            }
        }
        if (needToUnregisterEvent) {
            HandlerList.unregisterAll(wizard);
        }
        wizard.end();
    }

    public static void leaveAll() {
        for (final Entry<String, Wizard> entry: Wizard.inWizardMode.entrySet()) {
            entry.getValue().end();
            HandlerList.unregisterAll(entry.getValue());
        }
        Wizard.inWizardMode.clear();
    }

    public static Map<String, Wizard> getInwizardmode() {
        return Wizard.inWizardMode;
    }
}