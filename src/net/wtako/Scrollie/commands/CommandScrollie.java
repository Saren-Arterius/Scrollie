package net.wtako.Scrollie.Commands;

import net.wtako.Scrollie.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandScrollie implements CommandExecutor {
    @SuppressWarnings("unused")
    private Main plugin;
    public CommandScrollie(Main plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args[1].toLowerCase() == "create") {
            
            return true;
        }
        return true;
    }
}
