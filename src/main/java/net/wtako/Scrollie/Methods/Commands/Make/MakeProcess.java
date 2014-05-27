package net.wtako.Scrollie.Methods.Commands.Make;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import me.desht.dhutils.ExperienceManager;
import net.milkbowl.vault.economy.Economy;
import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.FactionLocationChecker;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Methods.ScrollInstance;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.WorldGuardLocationChecker;
import net.wtako.Scrollie.Methods.Commands.Make.Wizards.PlayerClickWizard;
import net.wtako.Scrollie.Methods.Commands.Make.Wizards.SetScrollNameWizard;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class MakeProcess extends ScrollDatabase {

    private final Player  player;
    private final Integer scrollID;
    private Integer       destX;
    private Integer       destY;
    private Integer       destZ;
    private String        destWorld;
    public String         targetName;
    private boolean       readSuccess;

    public MakeProcess(Player player, Integer scrollID) throws SQLException {
        super(player);
        this.player = player;
        this.scrollID = scrollID;
        if (readValueFromDB()) {
            readSuccess = true;
        }
    }

    public MakeProcess(Player player, Integer scrollID, Integer timesBeUsed) throws SQLException {
        super(player);
        this.player = player;
        this.scrollID = scrollID;
        if (readValueFromDB()) {
            player.sendMessage(setTimesBeUsed(timesBeUsed.toString(), true));
            readSuccess = true;
        }
    }

    private boolean readValueFromDB() {
        try {
            final PreparedStatement selStmt = conn
                    .prepareStatement("SELECT * FROM 'scroll_creations' WHERE owner = ? AND scroll_id = ?");
            selStmt.setString(1, player.getName().toLowerCase());
            selStmt.setInt(2, scrollID);

            final ResultSet result = selStmt.executeQuery();
            setDestinationType(result.getString(5), true);
            setWarmUpTime(result.getString(6), true);
            setCoolDownTime(result.getString(7), true);
            setAllowCrossWorldTP(result.getString(8), true);
            setScrollName(result.getString(4), true);
            if (getTimesBeUsed() == null) {
                setTimesBeUsed(result.getString(9), true);
            }
            result.close();
            selStmt.close();
            return true;
        } catch (final SQLException e) {
            player.sendMessage(MessageFormat.format(Lang.NO_SUCH_SCROLL.toString(), scrollID));
            return false;
        }
    }

    private boolean costPlayer() {
        if (Main.getInstance().getConfig().getBoolean("variable.make.UseEconInsteadOfEXP")
                && Main.getInstance().getConfig().getBoolean("system.VaultSupport")) {
            try {
                final RegisteredServiceProvider<Economy> provider = Main.getInstance().getServer().getServicesManager()
                        .getRegistration(net.milkbowl.vault.economy.Economy.class);
                final Economy economy = provider.getProvider();
                final double moneyRequired = getEXPRequired();
                if (economy.has(player, moneyRequired)) {
                    economy.withdrawPlayer(player, moneyRequired);
                    player.sendMessage(MessageFormat.format(Lang.COST_CHARGED.toString(), "$", moneyRequired));
                    return true;
                }
                player.sendMessage(MessageFormat.format(Lang.YOU_DONT_HAVE_ENOUGH_MONEY.toString(), moneyRequired,
                        economy.getBalance(player)));
                return false;
            } catch (final Error e) {
                player.sendMessage(MessageFormat.format(Lang.ERROR_HOOKING.toString(), "Vault"));
                return false;
            }
        } else {
            final ExperienceManager man = new ExperienceManager(player);
            final double EXPRequied = getEXPRequired();
            if (!man.hasExp(EXPRequied)) {
                player.sendMessage(MessageFormat.format(Lang.YOU_DONT_HAVE_ENOUGH_EXP.toString(), EXPRequied,
                        man.getCurrentExp(), Lang.EXP.toString()));
                return false;
            }
            man.changeExp(-EXPRequied);
            player.sendMessage(MessageFormat.format(Lang.COST_CHARGED.toString(), EXPRequied, Lang.EXP.toString()));
            return true;
        }
    }

    public boolean magickScroll(boolean hasCost) {

        if (player.hasPermission(Main.getInstance().getProperty("artifactId") + ".noCostRequiredToMake")) {
            hasCost = false;
        }

        ItemStack scrollItem;
        if (player.getItemInHand().getAmount() > 1) {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Lang.YOUR_INVENTORY_IS_FULL.toString());
                return false;
            }
            try {
                final ScrollInstance scrollInstance = new ScrollInstance(this);
                scrollItem = scrollInstance.getScrollItem(getScrollName());
            } catch (final SQLException e) {
                player.sendMessage(Lang.DB_EXCEPTION.toString());
                e.printStackTrace();
                return false;
            }
            if (hasCost) {
                if (!costPlayer()) {
                    return false;
                }
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            }
        } else {
            if (!hasCost && player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Lang.YOUR_INVENTORY_IS_FULL.toString());
                return false;
            }
            try {
                final ScrollInstance scrollInstance = new ScrollInstance(this);
                scrollItem = scrollInstance.getScrollItem(getScrollName());
            } catch (final SQLException e) {
                player.sendMessage(Lang.DB_EXCEPTION.toString());
                e.printStackTrace();
                return false;
            }
            if (hasCost) {
                if (!costPlayer()) {
                    return false;
                }
                player.getInventory().removeItem(player.getItemInHand());
            }
        }
        player.getInventory().addItem(scrollItem);
        player.sendMessage(MessageFormat.format(Lang.FINISHED_MAKING.toString(), scrollItem.getItemMeta()
                .getDisplayName()));
        return true;
    }

    public void makeScroll() {
        if (readSuccess) {
            final String itemTypeRequiredString = Main.getInstance().getConfig().getString("variable.make.ScrollItem");
            final Material itemTypeRequired = Material.getMaterial(itemTypeRequiredString.toUpperCase());
            if (getDestinationType() == 6) {
                final HashMap<Integer, ? extends ItemStack> existingScrolls = player.getInventory().all(
                        itemTypeRequired);
                for (final Entry<Integer, ? extends ItemStack> entry: existingScrolls.entrySet()) {
                    try {
                        final ScrollInstance scroll = new ScrollInstance(entry.getValue());
                        if (scroll.getDestinationType() != null && scroll.getDestinationType() == 6) {
                            player.sendMessage(Lang.YOU_ALREADY_OWN_RESCUE_SCROLL.toString());
                            return;
                        }
                    } catch (final SQLException e) {
                        player.sendMessage(Lang.DB_EXCEPTION.toString());
                        e.printStackTrace();
                        return;
                    } catch (final NullPointerException e) {
                        continue;
                    }
                }
                magickScroll(false);
            } else if (!player.getItemInHand().isSimilar(new ItemStack(itemTypeRequired, 1))
                    && !player.hasPermission(Main.getInstance().getProperty("artifactId") + ".noCostRequiredToMake")) {
                final String msg = Lang.PLEASE_HOLD_ITEM.toString();
                player.sendMessage(MessageFormat.format(msg, itemTypeRequiredString));
            } else if (getDestinationType() == 3) {
                Wizard.enterOrLeave(player, new PlayerClickWizard(player, this));
            } else if (getDestinationType() == 4) {
                if (Main.getInstance().getConfig().getBoolean("system.FactionsSupport")
                        && !FactionLocationChecker.checkIfCanTeleportFrom(player)) {
                    return;
                }
                destWorld = player.getWorld().getName();
                final Location destLoc = player.getLocation();
                if (Main.getInstance().getConfig().getBoolean("system.WorldGuardSupport")) {
                    if (!WorldGuardLocationChecker.checkWorldGuard(player, destLoc)) {
                        player.sendMessage(Lang.TARGET_LOCATION_NOT_ALLOWED.toString());
                        return;
                    }
                }
                destX = destLoc.getBlockX();
                destY = destLoc.getBlockY();
                destZ = destLoc.getBlockZ();
                Wizard.enterOrLeave(player, new SetScrollNameWizard(player, this));
            } else {
                magickScroll(true);
            }
        }
    }

    public Integer getDestX() {
        return destX;
    }

    public Integer getDestY() {
        return destY;
    }

    public Integer getDestZ() {
        return destZ;
    }

    public String getDestWorld() {
        return destWorld;
    }
}
