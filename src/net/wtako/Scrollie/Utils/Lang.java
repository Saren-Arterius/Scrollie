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
    WIZARD_ENTER("wizard-enter", "&3Enter wizard mode. Type 'exit' to exit."),
    WIZARD_EXIT("wizard-exit", "&3Exiting wizard mode."),
    WHAT_DESTINATION("what-destination", "&6What is the &3destination&6 of the new scroll?"),
    ALL_DESTINATIONS("all-destinations", "{0}. &a{1}&f <&e{2}&f>"),
    TO_TEXT("to-text", "To"),
    WHAT_WOULD_THE_WARM_UP_TIME_BE("what-would-the-warm-up-time-be", "&6How about the &cwarm up&6 time?"),
    WHAT_WOULD_THE_COOL_DOWN_TIME_BE("what-would-the-cool-down-time-be", "&6How about the &bcool down&6 time?"),
    SHORTER_TIME_MORE_EXP("shorter-time-more-exp", "(&bThe shorter time the more &cEXP&b required&f)"),
    ALLOW_CROSS_WORLD_TP_OR_NOT(
            "allow-cross-world-tp-or-not",
            "&6Allow &3cross-world&6 teleportation? &f<&aYes&f/&cNo&f>"),
    HOW_MANY_TIMES_COULD_THIS_SCROLL_BE_USED(
            "how-many-times-could-this-scroll-be-used",
            "&6How &3many times&6 could this scroll be used?"),
    WILL_BE_MULTIPLIED_BY(
            "will-multiply-by",
            "(&bTotal &cEXP&b required to make this scroll will be multiplied by &c{0}&f)"),
    ENTER_NAME("enter-name", "&6Enter any &3name&6 to identify your new scroll."),
    EXP_REQUIRED("exp-required", "&cEXP&e required to make this scroll: &c{0} exp"),
    FINISHED_CREATING("finished-creating", "&aFinished creating a new scroll."),

    VALUE_SET("value-set", "&aValue set. Total &cEXP&a required: &c{0} exp"),
    KEY_VALUE("key-value", "&a{0}: &c{1}"),

    LIST_PATTERN1("list-pattern1", "{0}. <&c{0}&f>"),
    LIST_PATTERN2("list-pattern2", "(&3D&f: {0}, &cW&f: {1}, &bC&f: {2}, &3CW&f: {3}, &3T&f: {4}, &cEXP&f: {5})"),
    SCROLL_LIST("scroll-list", "&aScroll list:"),
    YOU_DONT_HAVE_ANY_CREATIONS("you-dont-have-any-creations", "&eYou don't have any creations."),

    PLEASE_CLICK_ON_A_PLAYER(
            "please-click-on-a-player",
            "&ePlease left click on a player so that you can teleport to him/her using the scroll."),
    HOW_TO_CANCEL("how-to-cancel", "&3Right click any block or type 'exit' to cancel."),
    NOT_PREMITTED_TO_TELEPORT_TO_THAT_PLAYER(
            "not-permitted-to-teleport-to-that-player",
            "&cYou are not powerful enough to teleport to that player."),
    YOU_CAN_TP_TO_HIM_BECAUSE_POWER_HIGHER(
            "you-can-tp-to-him-because-power-higher",
            "&cYou should not be able to teleport to {0} &ebut your faction's power is higher."),
    YOU_CANT_TP_TO_HIM_BECAUSE_FACTION_BAD(
            "you-cant-tp-to-him-because-faction-bad",
            "&c{0} doesn't let you to teleport to him because of bad faction relationship."),
    TARGET_HAS_TURNED_TP_OFF("target-has-turned-tp-off", "&c{0} has turned TP off so nobody can teleport to him."),
    COST_CHARGED("cost-charged", "&aCharged: &c{0} {1}."),
    EXIT_MAKING("exit-making", "&eCancelled making a scroll."),
    FINISHED_MAKING("finished-making", "&eYou made the scroll &f<&c{0}&f>&e. Right click on the scroll to use it."),

    WARMING_UP("warming-up", "&eWarming up for &c{0} &esecond(s)."),
    WARMING_UP_SECONDS_LEFT("warming-up-seconds left", "&c{0} &fseconds left..."),
    COOLING_DOWN(
            "cooling-down",
            "&cCooling down is in process. &eYou have to wait for &c{0} &eseconds before using teleportation scrolls again."),
    SCROLL_DISAPPEARED("scroll-disappeared", "&eYour scroll &f<&c{0}&f>&e is faded away."),
    SCROLL_MAGIC_GETTING_WEAKER(
            "scroll-magic-getting-weaker",
            "&eThe magic of your scroll &f<&c{0}&f>&e is getting weaker."),
    SCROLL_MAGIC_DISAPPEARED("scroll-magic-disappeared", "&eThe magic of your scroll &f<&c{0}&f>&e is disappeared."),
    FINISHED_USING("finished-using", "&eYou are teleported to &c{0}&f. <X:{1} Y:{2} Z:{3}>"),

    FINISHED_DELETING("finished-deleting", "&aYou deleted the scroll creatoion &f<&c{0}&f>&a."),
    PLUGIN_RELOADED("plugin-reloaded", "&aPlugin reloaded."),

    NO_SUCH_SCROLL("no-such-scroll", "&cCould not find scroll ID {0}."),
    WRONG_VALUE("wrong-value", "&cValue &4{0}&c in config.yml is invalid. Falling back to hard-coded default."),
    FALLING_BACK_TO("falling-back-to", "&cFalling back to: &4{0}"),
    DB_EXCEPTION("db-exception", "&4A database error occured! Please contact server administrators."),
    FACTIONS_UNSUPPORTED("factions-unsupported", "&cFactions support is disabled."),
    ERROR_HOOKING("error-hooking", "&4Error in hooking into {0}! Please contact server administrators."),
    ALREADY_IN_WIZARD("already-in-wizard", "&cYou are already in another wizard!"),

    TOO_MANY_SCROLLS("too-many-scrolls", "&cYou have created too many scrolls."),
    ENTER_AGAIN("enter-again", "&cSorry, please enter again."),
    EXPECTED_GOT("expected-got", "&aExpected: {0}. &cGot: {1}"),
    NOT_MATCHING_REGEX("not-matching-regex", "Not matching regex &f{0}"),
    MATCHING_REGEX("matching-regex", "Matching regex &f{0}"),
    IS_SCROLL_NAME_CONTAINING_BAD_WORD(
            "is-scroll-name-contains-bad-word",
            "&cIs your scroll name containing bad words?"),
    PLEASE_HOLD_ITEM("please-hold-item", "&ePlease hold at least &c1 unenchanted {0}&e in your hand."),
    YOUR_INVENTORY_IS_FULL("your-inventory-is-full", "&cYour inventory is full."),
    YOU_ALREADY_OWN_RESCUE_SCROLL(
            "you-already-own-rescue-scroll",
            "&cYou are already owning at least 1 rescue scroll in your inventory."),
    YOU_DONT_HAVE_ENOUGH_EXP(
            "you-dont-have-enough-exp",
            "&cYou are too weak to make that scroll. &f<&aRequired: {0} {2}. &cYou have: {1} {2}&f>"),
    YOU_DONT_HAVE_ENOUGH_MONEY(
            "you-dont-have-enough-money",
            "&cYou are too poor to make that scroll. &f<&aRequired: ${0}. &cYou have: ${1}&f>"),

    WARM_UP_FAIL("warm-up-fail", "&cWarming up process has been interrupted."),
    NOT_POWERFUL_ENOUGH_CROSS_WORLD_TP(
            "not-powerful-enough",
            "&eYour scroll &f<&c{0}&f>&e is not powerful enough to perform cross-world teleportation."),
    YOU_HAVE_NO_HOME("you-have-no-home", "&eYou don't have a home. Right click on a bed to set your home."),
    YOU_ARE_NOT_FACTION_MEMBER("you-are-not-faction-member", "&cYou are not a member of any faction!"),
    YOUR_FACTION_DOES_NOT_HAVE_HOME("your-faction-does-not-have-home", "&cYour faction does not have home!"),
    YOUR_SCROLL_STILL_WORKS(
            "your-scroll-still-works",
            "&eYour scroll (will) still works because your faction has higher power than others'."),
    CANT_TP_FROM_ENEMY_TERRITORY(
            "cant-tp-from-enemy-territory",
            "&cYour scroll isn't (will not) going to work! Are you in other faction's territory?"),
    CANT_TP_TO_ENEMY_TERRITORY(
            "cant-tp-to-enemy-territory",
            "&cYour scroll isn't going to work! Are you teleporting to other faction's territory?"),
    TARGET_PLAYER_IS_OFFLINE("target-player-is-offline", "&e{0} is offline."),
    WILL_TP_TO_OFFLINE_PLAYER_LOCATION(
            "will-tp-to-offline-player-location",
            "&aWill teleport to &f{0}&a's offline location."),
    CANT_FIND_OFFLINE_PLAYER_LOCATION("cant-find-offline-player-location", "&aCannot find &f{0}&a's offline location."),
    TARGET_LOCATION_NOT_ALLOWED("target-location-not-allowed", "&cTarget location is not in allowed area."),
    SOURCE_LOCATION_NOT_ALLOWED("source-location-not-allowed", "&cSource location is not in allowed area."),
    CANT_START_TP("cant-start-tp", "&cCannot start teleportation process."),

    YOU_TURNED_OFF_TP("you-turned-off-tp", "&aYou turned off TP. Others can no longer teleport to you."),
    YOU_TURNED_ON_TP("you-turned-on-tp", "&aYou turned on TP. Others can teleport to you from now."),

    HELP_CREATE("help-create", "Type &a/scrollie create&f to create a new scroll."),
    HELP_MAKE("help-make", "Type &a/scrollie make &f<&cScroll ID&f> [&3Max use times&f] to make a predefined scroll."),
    HELP_LIST("help-list", "Type &a/scrollie list&f to view the scrolls you have created."),
    HELP_DELETE("help-delete", "Type &a/scrollie delete &f<&cScroll ID&f> to delete a specific scroll."),
    HELP_RELOAD("help-reload", "Type &a/scrollie reload&f to reload the plugin. &c(OP only)"),

    MAKE_THIS_SCROLL("make-this-scroll", "Type &a/scrollie make &c{0}&f to make this scroll."),
    VIEW_SCROLL_LIST("view-scroll-list", "Type &a/scrollie list&f to view all scrolls."),
    DELETE_THIS_SCROLL("delete-this-scroll", "Type &a/scrollie delete &c{0}&f to delete this scroll."),
    MAKE_USAGE(
            "make-usage",
            "Usage: &a/scrollie make &f<&cScroll ID&f> [<&3Max use times&f> (&coverrides&f)], &a[] = Optional"),
    DELETE_USAGE("delete-usage", "Usage: &a/scrollie delete &f<&cScroll ID&f>"),

    DEFAULT_VALUE("default-value", "Default"),
    MIN_VALUE("min-value", "Min"),
    MAX_VALUE("max-value", "Max"),
    INTEGER("integer", "Integer"),
    NOT_AN_INTEGER("not-an-integer", "Not an integer"),
    TIMES_MEASURE_WORD("times-measure-word", "time(s)"),
    SECONDS("seconds", "second(s)"),
    WORLD("world", "World"),
    ALLOWED("allowed", "Yes"),
    NOT_ALLOWED("not-allowed", "No"),
    EXP("exp", "exp"),

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

    NO_PERMISSION_COMMAND("no-permission-command", "&cYou are not allowed to use this command.");

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