package net.wtako.Scrollie.Commands;

import net.wtako.Scrollie.Commands.scrollie.ArgCreate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandScrollie implements CommandExecutor  {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                ArgCreate create = new ArgCreate((Player) sender);
                create.goToWizard();
                return true;
            }
        }
        return false;
    }
}
