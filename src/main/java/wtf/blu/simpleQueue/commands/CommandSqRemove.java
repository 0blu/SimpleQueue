package wtf.blu.simpleQueue.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import wtf.blu.simpleQueue.PrioritizedPlayerConfigHandler;
import wtf.blu.simpleQueue.SimpleQueuePermission;
import wtf.blu.simpleQueue.util.WtfYamlConfiguration;

public class CommandSqRemove implements CommandExecutor {

    private final WtfYamlConfiguration configYaml;
    private final PrioritizedPlayerConfigHandler prioritizedPlayerConfigHandler;

    public CommandSqRemove(WtfYamlConfiguration configYaml, PrioritizedPlayerConfigHandler prioritizedPlayerConfigHandler) {
        this.configYaml = configYaml;
        this.prioritizedPlayerConfigHandler = prioritizedPlayerConfigHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getLabel().equals("sqremove") || !SimpleQueuePermission.COMMAND_SQREMOVE.hasPermission(sender))
            return false;

        if(!configYaml.getBoolean("allowPrioritizedPlayersViaConfig")) {
            sender.sendMessage("[SimpleQueue]: Please enable 'allowPrioritizedPlayersViaConfig' in the config.yml");
            return true;
        }

        if(args.length != 1)
            return false;

        Server server = sender.getServer();
        OfflinePlayer foundPlayer = server.getOfflinePlayer(args[0]);

        prioritizedPlayerConfigHandler.removePlayer(foundPlayer);

        sender.sendMessage(String.format("[SimpleQueue]: %s was successfully removed from the prioritizedPlayers.yml.", foundPlayer.getPlayer()));

        return true;
    }
}
