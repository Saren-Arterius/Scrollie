package net.wtako.Scrollie.Commands;

import net.wtako.Scrollie.Commands.Scrollie.ArgCreate;
import net.wtako.Scrollie.Commands.Scrollie.ArgDelete;
import net.wtako.Scrollie.Commands.Scrollie.ArgHelp;
import net.wtako.Scrollie.Commands.Scrollie.ArgList;
import net.wtako.Scrollie.Commands.Scrollie.ArgMake;
import net.wtako.Scrollie.Commands.Scrollie.ArgReload;
import net.wtako.Scrollie.Commands.Scrollie.ArgToggletp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandScrollie implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("new")) {
                new ArgCreate(sender).goToWizard();
                return true;
            } else if (args[0].equalsIgnoreCase("make") || args[0].equalsIgnoreCase("mk")) {
                new ArgMake(sender, args).goToWizard();
                return true;
            } else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("ls")) {
                new ArgList(sender, args);
                return true;
            } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")
                    || args[0].equalsIgnoreCase("rm")) {
                new ArgDelete(sender, args);
                return true;
            } else if (args[0].equalsIgnoreCase("toggletp")) {
                new ArgToggletp(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                new ArgHelp(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                new ArgReload(sender);
                return true;
            }
        }
        return false;
    }
}
