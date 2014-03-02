package net.wtako.Scrollie.Methods.scrollie.make;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.ScrollDatabase;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.scrollie.make.Locations.PlayerClickWizard;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MakeProcess extends ScrollDatabase {

    private final Player  player;
    private final Integer scrollID;

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

    private boolean readValueFromDB() throws SQLException {
        final PreparedStatement selStmt = conn
                .prepareStatement("SELECT * FROM 'scrolls' WHERE owner = ? AND scroll_id = ?");
        selStmt.setString(1, player.getName().toLowerCase());
        selStmt.setInt(2, scrollID);

        final ResultSet result = selStmt.executeQuery();
        try {
            setScrollName(result.getString(4), true);
            setDestinationType(result.getString(5), true);
            setWarmUpTime(result.getString(6), true);
            setCoolDownTime(result.getString(7), true);
            setAllowCrossWorldTP(result.getString(8), true);
            if (getTimesBeUsed() == null) {
                setTimesBeUsed(result.getString(9), true);
            }
            selStmt.close();
            return true;
        } catch (final SQLException e) {
            player.sendMessage(MessageFormat.format(Lang.NO_SUCH_SCROLL.toString(), scrollID));
            return false;
        }
    }

    public void makeScroll() throws SQLException {
        if (readValueFromDB()) {
            String itemTypeRequiredString = Main.getInstance().getConfig().getString("variable.make.ScrollItem");
            Material itemTypeRequired = Material.getMaterial(itemTypeRequiredString.toUpperCase());
            ItemStack itemStackRequired = new ItemStack(itemTypeRequired, 1);
            if (!player.getItemInHand().equals(itemStackRequired)) {
                String msg = Lang.PLEASE_HOLD_ITEM.toString();
                player.sendMessage(MessageFormat.format(msg, itemTypeRequiredString));
                return;
            } else if (getDestinationType() == 3) {
                Wizard.enterOrLeave(player, new PlayerClickWizard(player, this));
            } else {
                player.getItemInHand().addUnsafeEnchantment(Enchantment.LUCK, 1);
            }
        }
    }
}
