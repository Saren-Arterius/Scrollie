package net.wtako.Scrollie.Utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * An enum for requesting strings from the language file.
 * 
 * @author gomeow
 */
public enum Lang {
    TITLE("title", "&4[&fScrollie&4]:"),
    WIZARD_ENTER("wizard-enter", "Enter wizard mode. Type 'exit' to exit."),
    WIZARD_EXIT("wizard-exit", "Exiting wizard mode."),
    WHAT_DESTINATION("what-destination", "What is the destination of the new scroll?"),
    ALL_DESTINATIONS("all-destinations", "{0}. {1} <{2}>"),
    TO_TEXT("to-text", "To"),
    WHAT_WOULD_THE_WARM_UP_TIME_BE("what-would-the-warm-up-time-be", "How about the warm up time? (The shorter time the more exp required)"),
    WHAT_WOULD_THE_COOL_DOWN_TIME_BE("what-would-the-cool-down-time-be", "How about the cool down time? (The shorter time the more exp required)"),
    ALLOW_CROSS_WORLD_T_P_OR_NOT("allow-cross-world-t-p-or-not", "Allow cross-world teleportation? (Need {0}*xp to make) <yes/no>"),
    HOW_MANY_TIMES_COULD_THIS_SCROLL_BE_USED("how-many-times-could-this-scroll-be-used", "How many times could this scroll be used? (Need times*xp to make)"),
    ENTER_NAME("enter-name", "Enter any name to identify your new scroll."),
    FINISHED_CREATING("finished-creating", "Finished creating a new scroll."),
    
    VALUE_SET("value-set", "Value set."),
    KEY_VALUE("key-value", "{0}: {1}"),

    FINISHED_MAKING("finished-making", "You made the scroll <{0}>. Right click on the paper to use it."),
    WARMING_UP("warming-up", "Warming up for {0} seconds."),
    COOLING_DOWN("cooling-down", "Cooling down is in process. You have to wait for {0} seconds before using teleportation scrolls again."),
    SCROLL_DISAPPEARED("scroll-disappeared", "Your scroll <{0}> is faded away."),
    FINISHED_USING("finished-using", "You used the scroll <{0}>. You are teleported to {1}. >X:{2} Y:{3} Z:{4}<"),

    FINISHED_DELETING("finished-deleting", "You deleted the scroll <{0}>."),
    PLUGIN_RELOADED("plugin-reloaded", "Plugin reloaded."),

    NO_SUCH_SCROLL("no-such-scroll", "Could not find scroll ID {0}."),
    WRONG_VALUE("wrong-value", "Value {0} in config.yml is invalid. Falling back to hard-coded default."),
    FALLING_BACK_TO("falling-back-to", "Falling back to: {0}"),

    ENTER_AGAIN("enter-again", "Sorry, please enter again."),
    EXPECTED_GOT("expected-got", "Expected: {0}. Got: {1}"),
    PLEASE_HOLD_A_PAPER("please-hold-a-paper", "Please hold an unused paper in your hand."),
    YOU_DONT_HAVE_ENOUGH_EXP("you-dont-have-enough-exp", "You don't have enough experience. >Required: {0}xp. You have: {1}xp<"),
    WARM_UP_FAIL("warm-up-fail", "Warming up process has been interrupted."),
    NOT_POWERFUL_ENOUGH("not-powerful-enough", "Your scroll <{0}> is not powerful enough to perform cross-world teleportation."),

    HELP_TEXT1("help-text1", "Type '/scrollie create' to create a new scroll."),
    HELP_TEXT2("help-text2", "Type '/scrollie make <Scroll ID> [<Max use times> (overrides)], [] = Optional' to make a predefined scroll."),
    HELP_TEXT3("help-text3", "Type '/scrollie list' to the scrolls you have created."),
    HELP_TEXT4("help-text4", "Type '/scrollie delete <Scroll ID>' to delete a specific scroll you have created. (Use '/scrollie list' first)"),
    HELP_TEXT5("help-text5", "Type '/scrollie reload' to reload the plugin."),
    
    MAKE_THIS_SCROLL("make-this-scroll", "Type '/scrollie make {0}' to make this scroll."),
    VIEW_SCROLL_LIST("view-scroll-list", "Type '/scrollie list' to view other scrolls."),
    DELETE_THIS_SCROLL("delete-this-scroll", "Type '/scrollie delete {0}' to delete this scroll."),
    MAKE_USAGE("make-usage", "Usage: /scrollie make <Scroll ID> [<Max use times> (overrides)], [] = Optional"),
    DELETE_USAGE("delete-usage", "Usage: /scrollie delete <Scroll ID>"),
    
    DESTINATION_SPAWN("destination-spawn", "Spawn"),
    DESTINATION_HOME("destination-home", "Home"),
    DESTINATION_FACTION_HOME("destination-faction-home", "Faction home"),
    DESTINATION_PLAYER("destination-player", "Player's location"),
    DESTINATION_CURRENT_LOCATION("destination-current-location", "Current location"),
    DESTINATION_RANDOM("destination-random", "Random location"),
    DESTINATION_NOT_SET("destination-not-set", "Not a location");

    private String path;
    private String def;
    private static YamlConfiguration LANG;

    /**
     * Lang enum constructor.
     * 
     * @param path
     *            The string path.
     * @param start
     *            The default string.
     */
    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     * 
     * @param config
     *            The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    @Override
    public String toString() {
        if (this == TITLE)
            return ChatColor.translateAlternateColorCodes('&',
                    LANG.getString(this.path, def))
                    + " ";
        return ChatColor.translateAlternateColorCodes('&',
                LANG.getString(this.path, def));
    }

    /**
     * Get the default value of the path.
     * 
     * @return The default value of the path.
     */
    public String getDefault() {
        return this.def;
    }

    /**
     * Get the path to the string.
     * 
     * @return The path to the string.
     */
    public String getPath() {
        return this.path;
    }
}