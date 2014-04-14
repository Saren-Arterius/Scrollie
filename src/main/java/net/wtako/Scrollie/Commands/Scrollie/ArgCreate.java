package net.wtako.Scrollie.Commands.Scrollie;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Methods.Database;
import net.wtako.Scrollie.Methods.Wizard;
import net.wtako.Scrollie.Methods.Commands.Create.Wizards.CreateWizard;
import net.wtako.Scrollie.Utils.Lang;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArgCreate {

    private final CommandSender sender;

    public ArgCreate(CommandSender sender) {
        this.sender = sender;
    }

    public void goToWizard() {
        if (!sender.hasPermission("Scrollie.make")) {
            sender.sendMessage(Lang.NO_PERMISSION_COMMAND.toString());
            return;
        }
        try {
            if (canGoToWizard()) {
                Wizard.enterOrLeave((Player) sender, new CreateWizard((Player) sender));
            }
        } catch (final SQLException e) {
            e.printStackTrace();
            sender.sendMessage(Lang.DB_EXCEPTION.toString());
        }
    }

    public boolean canGoToWizard() throws SQLException {
        if (sender.hasPermission("Scrollie.overrideLimit.MaxScrolls")) {
            return true;
        } else {
            final int scrollsLimit = Main.getInstance().getConfig().getInt("variable.create.MaxScrolls");
            final PreparedStatement selStmt = Database.getInstance().conn
                    .prepareStatement("SELECT count(rowid) FROM 'scroll_creations' WHERE owner = ?");
            selStmt.setString(1, sender.getName().toLowerCase());
            final int scrollsCount = selStmt.executeQuery().getInt(1);
            selStmt.close();
            if (scrollsCount < scrollsLimit) {
                return true;
            } else {
                sender.sendMessage(Lang.TOO_MANY_SCROLLS.toString());
                sender.sendMessage(Lang.HELP_LIST.toString());
                sender.sendMessage(Lang.HELP_DELETE.toString());
                return false;
            }
        }
    }
}