package net.wtako.Scrollie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.wtako.Scrollie.Commands.CommandScrollie;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static YamlConfiguration LANG;
    public static File LANG_FILE;
    public static Logger log = Logger.getLogger("Scrollie");

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.getCommand("scrollie").setExecutor(new CommandScrollie());
        this.loadLang();
    }

    public void onDisable() {
        this.getLogger().info("Good-bye bloody chalon!");
    }

    public void loadLang() {
        File lang = new File(getDataFolder(), "messages.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = this.getResource("messages.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration
                            .loadConfiguration(defConfigStream);
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace(); // So they notice
                log.severe("[Scrollie] Couldn't create language file.");
                log.severe("[Scrollie] This is a fatal error. Now disabling");
                this.setEnabled(false); // Without it loaded, we can't send them
                                        // messages
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for (Lang item : Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        Main.LANG = conf;
        Main.LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch (IOException e) {

            log.log(Level.WARNING, "PluginName: Failed to save messages.yml.");
            log.log(Level.WARNING,
                    "PluginName: Report this stack trace to <your name>.");
            e.printStackTrace();
        }
    }

    /**
     * Gets the messages.yml config.
     * 
     * @return The messages.yml config.
     */
    public YamlConfiguration getLang() {
        return LANG;
    }

    /**
     * Get the messages.yml file.
     * 
     * @return The messages.yml file.
     */
    public File getLangFile() {
        return LANG_FILE;
    }

    public static Main getInstance() {
        return instance;
    }
}
