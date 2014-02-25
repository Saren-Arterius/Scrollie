package net.wtako.Scrollie.Commands;

import net.wtako.Scrollie.Commands.scrollie.ArgCreate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandScrollie implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                new ArgCreate(sender).goToWizard();
                return true;
            }
        }
        return false;
    }
}
