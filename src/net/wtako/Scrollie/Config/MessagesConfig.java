package net.wtako.Scrollie.Config;

import java.io.File;
import java.io.InputStream;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Messaging;

public class MessagesConfig extends CustomConfig {

    private static MessagesConfig singleton = null;

    public MessagesConfig(File file) {
        super(file);
        loadDefaults(file);
        loadFile();
        saveDefaults(file);
        singleton = this;
    }

    /**
     * Loads a file from the plugin jar and sets as default
     *
     * @param filename The filename to open
     */
    public final void loadDefaults(File file) {
        InputStream stream = Main.getInstance().getResource(file.getName());
        if(stream == null) return;

        setDefaults(YamlConfiguration.loadConfiguration(stream));
    }

    /**
     * Saves the configuration to disk
     *
     * @return True if saved successfully
     */
    public final boolean saved(File file) {
        try {
            save(file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Saves current configuration (plus defaults) to disk.
     *
     * If defaults and configuration are empty, saves blank file.
     *
     * @return True if saved successfully
     */
    public final boolean saveDefaults(File file) {
        options().copyDefaults(true);
        options().copyHeader(true);
        boolean success = saved(file);
        options().copyDefaults(false);
        options().copyHeader(false);
        return success;
    }

    private void loadFile() {
        this.load();
        this.save();
    }

    public void _(CommandSender sender, String msg) {
        String loc = (String) this.get(msg);
        if (loc == null) {
            loc =  "Error with Translation files; Please contact the admin for verify or update translation";
            Messaging messager = new Messaging("Error with the " + msg + " translation, verify in your messages.yml !");
            messager.logError();
        }
        for (String l : loc.split("&n")) {
            sender.sendMessage(l.replace("&", "\u00a7"));
        }
    }

    public String[] _(String msg) {
        int i = ((String) this.get(msg)).split("&n").length;
        String[] loc = new String[i];
        int a;
        for (a = 0 ; a < i ; a++) {
            loc[a] = ((String) this.get(msg)).split("&n")[a].replace("&", "\u00a7");
        }
        if (loc == null || loc.length == 0) {
            loc[0] =  "Error with " + msg + " translation; Please contact the admin for verify or update translation files";
            Messaging messager = new Messaging("Error with the " + msg + " translation, verify in your messages.yml !");
            messager.logError();
        }
        return loc;
    }

    public static MessagesConfig getInstance() {
        if (singleton == null) {
            singleton = new MessagesConfig(new File("messages.yml"));
        }        
        return singleton;
    }

}