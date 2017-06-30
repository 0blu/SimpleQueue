package wtf.blu.simpleQueue.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import wtf.blu.simpleQueue.SimpleQueuePermission;
import wtf.blu.simpleQueue.SimpleQueuePlugin;

public class CommandSqVersion implements CommandExecutor {

    private final PluginDescriptionFile pdf;

    public CommandSqVersion(SimpleQueuePlugin plugin) {
        pdf = plugin.getDescription();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getLabel().equals("sqversion") || !SimpleQueuePermission.COMMAND_SQVERSION.hasPermission(sender))
            return false;

        sender.sendMessage(String.format("[SimpleQueue]: You are running %s version %s", pdf.getName(), pdf.getVersion()));


        return true;
    }
}
