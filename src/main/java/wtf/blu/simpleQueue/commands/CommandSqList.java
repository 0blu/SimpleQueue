package wtf.blu.simpleQueue.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import wtf.blu.simpleQueue.PrioritizedPlayerConfigHandler;
import wtf.blu.simpleQueue.SimpleQueuePermission;
import wtf.blu.simpleQueue.util.WtfYamlConfiguration;

import java.util.List;

public class CommandSqList implements CommandExecutor {

    private final WtfYamlConfiguration configYaml;
    private final PrioritizedPlayerConfigHandler prioritizedPlayerConfigHandler;

    public CommandSqList(WtfYamlConfiguration configYaml, PrioritizedPlayerConfigHandler prioritizedPlayerConfigHandler) {
        this.configYaml = configYaml;
        this.prioritizedPlayerConfigHandler = prioritizedPlayerConfigHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getLabel().equals("sqlist") || !SimpleQueuePermission.COMMAND_SQLIST.hasPermission(sender))
            return false;

        if(!configYaml.getBoolean("allowPrioritizedPlayersViaConfig")) {
            sender.sendMessage("[SimpleQueue]: Please enable 'allowPrioritizedPlayersViaConfig' in the config.yml");
            return true;
        }

        StringBuilder sb = new StringBuilder();
        List<OfflinePlayer> prioritizedPlayerList = prioritizedPlayerConfigHandler.getPrioritizedPlayerList();
        int listSize = prioritizedPlayerList.size();
        int index = 0;

        sb.append(String.format("[SimpleQueue]: There are %d in the prioritizedPlayers.yml:\n", listSize));

        for(OfflinePlayer player : prioritizedPlayerList) {
            sb.append(player.getName());
            if(++index < listSize)
                sb.append(", ");
        }

        sender.sendMessage(sb.toString());

        return true;
    }
}
