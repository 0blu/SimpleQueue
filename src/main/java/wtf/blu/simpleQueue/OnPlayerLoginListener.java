package wtf.blu.simpleQueue;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import wtf.blu.simpleQueue.util.WtfYamlConfiguration;

import java.util.logging.Logger;

class OnPlayerLoginListener implements Listener {
    private final Server server;
    private final Logger logger;
    private final PlayerQueue playerQueue;

    private final int maxPlayerCount;

    /* Config */
    private final String kickMessage;
    private final long mustReconnectWithinSec;
    private final int reservedSlots;

    OnPlayerLoginListener(Server server, Logger logger, PlayerQueue playerQueue, WtfYamlConfiguration configYaml) {
        this.server = server;
        this.logger = logger;
        this.playerQueue = playerQueue;

        maxPlayerCount = server.getMaxPlayers();

        kickMessage = configYaml.getString("kickMessageQueued");
        mustReconnectWithinSec = configYaml.getLong("mustReconnectWithinSec");
        reservedSlots = configYaml.getInt("reservedSlots");
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();

        if(e.getResult() != PlayerLoginEvent.Result.ALLOWED && e.getResult() != PlayerLoginEvent.Result.KICK_FULL) {
            logger.warning("[SimpleQueue]: A player logged in but had an unexpected login result. Please report this if you this is an error.");
            playerQueue.removePlayer(player);
            return;
        }

        int currentPlayerCount = server.getOnlinePlayers().size();

        //Checks if the player is able to ignore the slot limit
        if(SimpleQueuePermission.IGNORE_SLOT_LIMIT.hasPermission(player)) {
            playerQueue.removePlayer(player);
            e.allow();
            return;
        }

        int indexInQueue = playerQueue.getCurrentIndexOrInsert(player);

        if (
                //Is first in queue
                (indexInQueue == 0) && (
                    //server is not full(incl reserved slots)
                    (maxPlayerCount > (currentPlayerCount + reservedSlots)) ||
                    //server is not full and if prioritized player
                    ((maxPlayerCount > currentPlayerCount) && SimpleQueuePermission.PRIORITIZED_PLAYER.hasPermission(player))
                )) {
            playerQueue.removePlayer(player);
            //Intentionally no e.allow();
            return;
        }

        //Still in queue
        kickedQueued(e, indexInQueue);
    }

    private void kickedQueued(PlayerLoginEvent e, int index) {
        e.setResult(PlayerLoginEvent.Result.KICK_FULL);

        String kickMessageFormatted = String.format(
                ChatColor.translateAlternateColorCodes('&', kickMessage),
                (index+1), /* Current pos */
                (playerQueue.getQueuedPlayers().size()), /* Out of pos */
                (mustReconnectWithinSec) /* Reconnect within sec */
        );

        e.disallow(e.getResult(), kickMessageFormatted);
    }
}
