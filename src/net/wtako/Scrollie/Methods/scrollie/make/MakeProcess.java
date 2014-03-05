package net.wtako.Scrollie.Methods.scrollie.make;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Methods.ScrollInstance;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.scrollie.make.Locations.PlayerClickWizard;
import net.wtako.Scrollie.Methods.scrollie.make.Locations.SetScrollNameWizard;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MakeProcess extends ScrollDatabase {

    private final Player  player;
    private final Integer scrollID;
    private Integer       destX;
    private Integer       destY;
    private Integer       destZ;
    private Integer       scrollInstanceID;
    private String        destWorld;
    public String         targetName;

    public MakeProcess(Player player, Integer scrollID) throws SQLException {
        super(player);
        this.player = player;
        this.scrollID = scrollID;
    }

    public MakeProcess(Player player, Integer scrollID, Integer timesBeUsed) throws SQLException {
        super(player);
        this.player = player;
        this.scrollID = scrollID;
        player.sendMessage(setTimesBeUsed(timesBeUsed.toString(), true));
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

    public int saveScrollInstance() throws SQLException {
        final PreparedStatement selStmt = conn.prepareStatement("SELECT max(rowid) FROM 'scrolls'");
        final int rowid = selStmt.executeQuery().getInt(1) + 1;
        selStmt.close();

        final PreparedStatement insStmt = conn
                .prepareStatement("INSERT INTO `scrolls` (`rowid`, `scroll_destination`, `target_player`, `destination_x`, `destination_y`, `destination_z`, `destination_world`, `warm_up_time`, `cool_down_time`, `allow_cross_world_tp`, `times_remaining`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insStmt.setInt(1, rowid);
        insStmt.setInt(2, getDestinationType());
        insStmt.setInt(8, getWarmUpTime());
        insStmt.setInt(9, getCoolDownTime());
        insStmt.setInt(11, getTimesBeUsed());
        if (getAllowCrossWorldTP()) {
            insStmt.setInt(10, 1);
        } else {
            insStmt.setInt(10, 0);
        }
        if (targetName != null) {
            insStmt.setString(3, targetName);
        }
        if (destX != null) {
            insStmt.setInt(4, destX);
        }
        if (destY != null) {
            insStmt.setInt(5, destY);
        }
        if (destZ != null) {
            insStmt.setInt(6, destZ);
        }
        if (destWorld != null) {
            insStmt.setString(7, destWorld);
        }
        insStmt.execute();
        insStmt.close();
        return rowid;
    }

    public boolean magickScroll(boolean cost) {
        final String itemTypeRequiredString = Main.getInstance().getConfig().getString("variable.make.ScrollItem");
        final Material itemTypeRequired = Material.getMaterial(itemTypeRequiredString.toUpperCase());
        final ItemStack magicItem = new ItemStack(itemTypeRequired, 1);
        List<String> lores;

        if (player.getItemInHand().getAmount() > 1) {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Lang.YOUR_INVENTORY_IS_FULL.toString());
                return false;
            }
            try {
                scrollInstanceID = saveScrollInstance();
                lores = new ScrollInstance(scrollInstanceID).getLores();
            } catch (final SQLException e) {
                player.sendMessage(Lang.DB_EXCEPTION.toString());
                e.printStackTrace();
                return false;
            }
            if (cost) {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            }
        } else {
            if (!cost && player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Lang.YOUR_INVENTORY_IS_FULL.toString());
                return false;
            }
            try {
                scrollInstanceID = saveScrollInstance();
                lores = new ScrollInstance(scrollInstanceID).getLores();
            } catch (final SQLException e) {
                player.sendMessage(Lang.DB_EXCEPTION.toString());
                e.printStackTrace();
                return false;
            }
            if (cost) {
                player.getInventory().removeItem(player.getItemInHand());
            }
        }

        final ItemMeta magicItemMeta = magicItem.getItemMeta();
        magicItemMeta.setLore(lores);
        magicItemMeta.setDisplayName(getScrollName());
        magicItem.setItemMeta(magicItemMeta);
        magicItem.addUnsafeEnchantment(Enchantment.LUCK, 1);
        player.getInventory().addItem(magicItem);
        return true;
    }

    public void makeScroll() {
        if (readValueFromDB()) {
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
                    }
                }
                magickScroll(false);
            } else if (!player.getItemInHand().isSimilar(new ItemStack(itemTypeRequired, 1))) {
                final String msg = Lang.PLEASE_HOLD_ITEM.toString();
                player.sendMessage(MessageFormat.format(msg, itemTypeRequiredString));
            } else if (getDestinationType() == 3) {
                Wizard.enterOrLeave(player, new PlayerClickWizard(player, this));
            } else if (getDestinationType() == 4) {
                destWorld = player.getWorld().getName();
                final Location loc = player.getLocation();
                destX = loc.getBlockX();
                destY = loc.getBlockY();
                destZ = loc.getBlockZ();
                Wizard.enterOrLeave(player, new SetScrollNameWizard(player, this));
            } else {
                magickScroll(true);
            }
        }
    }
}
