package wtf.blu.simpleQueue;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import wtf.blu.simpleQueue.util.WtfYamlConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class PrioritizedPlayerConfigHandler {

    private final Server server;
    private final Logger logger;
    private final WtfYamlConfiguration prioritizedPlayerYaml;
    private final List<OfflinePlayer> prioritizedPlayerList;

    public PrioritizedPlayerConfigHandler(Server server, Logger logger, WtfYamlConfiguration prioritizedPlayerYaml) {
        this.server = server;
        this.logger = logger;
        this.prioritizedPlayerYaml = prioritizedPlayerYaml;
        prioritizedPlayerList = (List<OfflinePlayer>)(prioritizedPlayerYaml.getList("prioritizedPlayers"));
    }

    /**
     * @return Returns a unmodifiable list view of prioritizedPlayerList
     */
    public List<OfflinePlayer> getPrioritizedPlayerList() {
        return Collections.unmodifiableList(prioritizedPlayerList);
    }

    /**
     * Checks if player is in the prioritizedPlayer config
     * @param playerUUID The UUID of an player to check
     * @return true if the player is prioritized
     */
    public boolean isInConfig(UUID playerUUID) {
        return isInConfig(server.getOfflinePlayer(playerUUID));
    }

    /**
     * Checks if player is in the prioritizedPlayer config
     * @param player The player to check
     * @return true if the player is prioritized
     */
    public boolean isInConfig(OfflinePlayer player) {
        return prioritizedPlayerList.contains(player);
    }

    /**
     * Adds a player to the prioritizedPlayer config and saves it
     * @param offlinePlayer The player to add
     */
    public void addPlayer(OfflinePlayer offlinePlayer) {
        if(isInConfig(offlinePlayer))
            return;
        prioritizedPlayerList.add(offlinePlayer);
        prioritizedPlayerYaml.save();
    }

    /**
     * Removes a player from the prioritizedPlayer config and saves it
     * @param offlinePlayer The player to remove
     */
    public void removePlayer(OfflinePlayer offlinePlayer) {
        if(!isInConfig(offlinePlayer))
            return;
        prioritizedPlayerList.remove(offlinePlayer);
        prioritizedPlayerYaml.save();
    }
}
