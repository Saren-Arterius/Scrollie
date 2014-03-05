package net.wtako.Scrollie.Utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * An enum for requesting strings from the language file.
 * 
 * @author gomeow
 */
public enum Lang {

    TITLE("title", "[Scrollie]"),
    WIZARD_ENTER("wizard-enter", "Enter wizard mode. Type 'exit' to exit."),
    WIZARD_EXIT("wizard-exit", "Exiting wizard mode."),
    WHAT_DESTINATION("what-destination", "What is the destination of the new scroll?"),
    ALL_DESTINATIONS("all-destinations", "{0}. {1} <{2}>"),
    TO_TEXT("to-text", "To"),
    WHAT_WOULD_THE_WARM_UP_TIME_BE("what-would-the-warm-up-time-be", "How about the warm up time?"),
    WHAT_WOULD_THE_COOL_DOWN_TIME_BE("what-would-the-cool-down-time-be", "How about the cool down time?"),   
    SHORTER_TIME_MORE_EXP("shorter-time-more-exp", "(The shorter time the more exp required)"),
    ALLOW_CROSS_WORLD_TP_OR_NOT("allow-cross-world-tp-or-not", "Allow cross-world teleportation? <Yes/No>"),
    HOW_MANY_TIMES_COULD_THIS_SCROLL_BE_USED("how-many-times-could-this-scroll-be-used", "How many times could this scroll be used?"),
    WILL_BE_MULTIPLIED_BY("will-multiply-by", "(Total experience required to make this scroll will be multiplied by {0})"),
    ENTER_NAME("enter-name", "Enter any name to identify your new scroll."),
    EXP_REQUIRED("exp-required", "EXP required to make this scroll: {0} xp"),
    FINISHED_CREATING("finished-creating", "Finished creating a new scroll."),
    
    VALUE_SET("value-set", "Value set. EXP Required: {0} xp"),
    KEY_VALUE("key-value", "{0}: {1}"),

    LIST_PATTERN("list-pattern", "{0}. <{1}> - (D: {2}, W: {3}, C: {4}, CW: {5}, T:{6})"),
    SCROLL_LIST("scroll-list", "Scroll list:"),
    YOU_DONT_HAVE_ANY_CREATIONS("you-dont-have-any-creations", "You don't have any creations."),
    
    PLEASE_CLICK_ON_A_PLAYER("please-click-on-a-player", "Please left click on a player so that you can teleport to him/her using the scroll."),
    HOW_TO_CANCEL("how-to-cancel", "Right click any block or type 'exit' to cancel."),
    NOT_PREMITTED_TO_TELEPORT_TO_THAT_PLAYER("not-permitted-to-teleport-to-that-player", "You are not powerful enough to teleport to that player."),
    EXIT_MAKING("exit-making", "Cancelled making a scroll."),
    FINISHED_MAKING("finished-making", "You made the scroll <{0}>. Right click on the scroll to use it."),
    
    WARMING_UP("warming-up", "Warming up for {0} second(s)."),
    WARMING_UP_SECONDS_LEFT("warming-up-seconds left", "{0} seconds left..."),
    COOLING_DOWN("cooling-down", "Cooling down is in process. You have to wait for {0} seconds before using teleportation scrolls again."),
    SCROLL_DISAPPEARED("scroll-disappeared", "Your scroll <{0}> is faded away."),
    SCROLL_MAGIC_GETTING_WEAKER("scroll-magic-getting-weaker", "Your scroll <{0}>'s magic is getting weaker."),
    SCROLL_MAGIC_DISAPPEARED("scroll-magic-disappeared", "Your scroll <{0}>'s magic is disappeared."),
    FINISHED_USING("finished-using", "You used the scroll <{0}>. You are teleported to {1}. >X:{2} Y:{3} Z:{4}<"),

    FINISHED_DELETING("finished-deleting", "You deleted the scroll <{0}>."),
    PLUGIN_RELOADED("plugin-reloaded", "Plugin reloaded."),

    NO_SUCH_SCROLL("no-such-scroll", "Could not find scroll ID {0}."),
    WRONG_VALUE("wrong-value", "Value {0} in config.yml is invalid. Falling back to hard-coded default."),
    FALLING_BACK_TO("falling-back-to", "Falling back to: {0}"),
    DB_EXCEPTION("db-exception", "A database error occured! Please contact server administrators."),

    TOO_MANY_SCROLLS("too-many-scrolls", "You have created too many scrolls."),
    ENTER_AGAIN("enter-again", "Sorry, please enter again."),
    EXPECTED_GOT("expected-got", "Expected: {0}. Got: {1}"),
    NOT_MATCHING_REGEX("not-matching-regex", "Not matching regex <{0}>"),
    MATCHING_REGEX("matching-regex", "Matching regex <{0}>"),
    IS_SCROLL_NAME_CONTAINING_BAD_WORD("is-scroll-name-contains-bad-word", "Is your scroll name containing bad words?"),
    PLEASE_HOLD_ITEM("please-hold-item", "Please hold at least 1 unenchanted {0} in your hand."),
    YOUR_INVENTORY_IS_FULL("your-inventory-is-full", "Your inventory is full."),
    YOU_ALREADY_OWN_RESCUE_SCROLL("you-already-own-rescue-scroll", "You are already owning at least 1 rescue scroll in your inventory."),
    YOU_DONT_HAVE_ENOUGH_EXP("you-dont-have-enough-exp", "You are too weak to make that scroll. >Required: {0} xp. You have: {1} xp<"),
    WARM_UP_FAIL("warm-up-fail", "Warming up process has been interrupted."),
    NOT_POWERFUL_ENOUGH_CROSS_WORLD_TP("not-powerful-enough", "Your scroll <{0}> is not powerful enough to perform cross-world teleportation."),
    CANT_TP_FROM_ENEMY_TERRITORY("cant-tp-from-enemy-territory", "Your scroll isn't going to work! Are you in enemy's territory?"),
    CANT_TP_FROM_TO_TERRITORY("cant-tp-to-enemy-territory", "Your scroll isn't going to work! Are you teleporting to enemy's territory."),

    HELP_CREATE("help-create", "Type '/scrollie create' to create a new scroll."),
    HELP_MAKE("help-make", "Type '/scrollie make <Scroll ID> [Max use times]' to make a predefined scroll."),
    HELP_LIST("help-list", "Type '/scrollie list' to view the scrolls you have created."),
    HELP_DELETE("help-delete", "Type '/scrollie delete <Scroll ID>' to delete a specific scroll."),
    HELP_RESTRICT("help-restrict", "Type '/scrollie restrict' to get help of restricted area settings. (OP only)"),
    HELP_RELOAD("help-reload", "Type '/scrollie reload' to reload the plugin. (OP only)"),
    
    RESTRICT_HELP_LIST_1("restrict-help-list-1", "Type '/scrollie restrict list' to list all restricted worlds and regions."),
    RESTRICT_HELP_LIST_2("restrict-help-list-2", "Rows with no region value are restricted world rows."),
    RESTRICT_HELP_SET_WORLD_NAME("restrict-help-set-world-name", "Type '/scrollie restrict world <World name>' to invert the restrict rule of whole world."),
    RESTRICT_HELP_SET_REGION("restrict-help-set-region", "Type '/scrollie restrict region' to invert the restrict rule in a region. Perform WorldEdit selection first."),
    RESTRICT_HELP_DELETE("restrict-help-delete", "Type '/scrollie restrict delete <Row ID>' to delete that restrict rule."),

    MAKE_THIS_SCROLL("make-this-scroll", "Type </scrollie make {0}> to make this scroll."),
    VIEW_SCROLL_LIST("view-scroll-list", "Type </scrollie list> to view all scrolls."),
    DELETE_THIS_SCROLL("delete-this-scroll", "Type </scrollie delete {0}> to delete this scroll."),
    MAKE_USAGE("make-usage", "Usage: /scrollie make <Scroll ID> [<Max use times> (overrides)], [] = Optional"),
    DELETE_USAGE("delete-usage", "Usage: /scrollie delete <Scroll ID>"),
    
    DEFAULT_VALUE("default-value", "Default"),
    MIN_VALUE("min-value", "Min"),
    MAX_VALUE("max-value", "Max"),
    INTEGER("integer", "Integer"),
    NOT_AN_INTEGER("not-an-integer", "Not an integer"),
    TIMES_MEASURE_WORD("times-measure-word", "time(s)"),
    SECONDS("seconds", "second(s)"),
    WORLD("world", "World"),
    ALLOWED("allowed", "Allowed"),
    NOT_ALLOWED("not-allowed", "Not allowed"),
    
    DESTINATION_TYPE("destination-type", "Destination type"),
    WARM_UP_TIME("warm-up-time", "Warm up time"),
    COOL_DOWN_TIME("cool-down-time", "Cool down time"),
    CROSS_WORLD_TP("cross-world-tp", "Cross world TP"),
    TIMES_BE_USED("times-be-used", "Times available"),
    TIMES_REMAINING("times-remaining", "Times remaining"),
    SCROLL_NAME("scroll-name", "Scroll name"),
    TARGET_PLAYER("target-player", "Target player"),
    TARGET_LOCATION("target-location", "Target location"),
    NOT_SET("not-set", "Not set"),
    
    DESTINATION_SPAWN("destination-spawn", "Spawn"),
    DESTINATION_HOME("destination-home", "Home"),
    DESTINATION_FACTION_HOME("destination-faction-home", "Faction home"),
    DESTINATION_PLAYER("destination-player", "Player's location"),
    
    DESTINATION_CURRENT_LOCATION("destination-current-location", "Current location"),
    DESTINATION_RANDOM("destination-random", "Random location"),
    DESTINATION_SELF_RESCUE("destination-self-rescue", "Self rescue"),
    DESTINATION_NOT_SET("destination-not-set", "Not a location"),
    
    NO_PERMISSION_COMMAND("no-permission-command", "You are not allowed to use this command.");

    private String                   path;
    private String                   def;
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
        def = start;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     * 
     * @param config
     *            The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        Lang.LANG = config;
    }

    @Override
    public String toString() {
        if (this == TITLE) {
            return ChatColor.translateAlternateColorCodes('&', Lang.LANG.getString(path, def)) + " ";
        }
        return ChatColor.translateAlternateColorCodes('&', Lang.LANG.getString(path, def));
    }

    /**
     * Get the default value of the path.
     * 
     * @return The default value of the path.
     */
    public String getDefault() {
        return def;
    }

    /**
     * Get the path to the string.
     * 
     * @return The path to the string.
     */
    public String getPath() {
        return path;
    }
}