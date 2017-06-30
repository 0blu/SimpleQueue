package wtf.blu.simpleQueue;

import org.bukkit.entity.Player;
import wtf.blu.simpleQueue.util.WtfYamlConfiguration;

import java.util.*;

public class PlayerQueue {
    private final long mustReconnectWithinSec;

    private LinkedList<UUID> playerQueue = new LinkedList<>();
    private Map<UUID, Long /* LastUpdateTime */> lastSeenMap = new HashMap<>();
    private Map<UUID, Boolean /* IsPrioritizedPlayer */> prioritizedPlayerMap = new HashMap<>();
    private PrioritizedPlayerConfigHandler prioritizedPlayerHandler;

    public PlayerQueue(PrioritizedPlayerConfigHandler prioritizedPlayerHandler, WtfYamlConfiguration configYaml) {
        this.prioritizedPlayerHandler = prioritizedPlayerHandler;
        mustReconnectWithinSec = configYaml.getLong("mustReconnectWithinSec");
    }

    /**
     * Will return the current index in the queue of a player<br>
     * If the player is not in the queue the player will be added<br>
     * The permissions will respected
     * @param player The player
     * @return The index of the player in the queue
     */
    public int getCurrentIndexOrInsert(Player player) {
        removeExceededPlayers();

        //Update timestamp
        updateLastSeen(player);

        int queueIndex = getInQueueIndex(player);

        if(queueIndex == -1) {
            return addPlayer(player);
        }

        return queueIndex;
    }

    /**
     * @return A unmodifiableList of all queued players
     */
    public List<UUID> getQueuedPlayers() {
        removeExceededPlayers();
        return Collections.unmodifiableList(playerQueue);
    }

    /**
     * @param player The player
     * @return The current index in the queue or -1
     */
    public int getInQueueIndex(Player player) {
        return playerQueue.indexOf(player.getUniqueId());
    }

    /**
     * Updates the last seen timestamp in the {@link #lastSeenMap}
     * @param player The player which should be updated
     */
    private void updateLastSeen(Player player) {
        lastSeenMap.put(player.getUniqueId(), System.currentTimeMillis());
    }

    /**
     * Checks if the player is prioritized
     * @param player The player
     * @return true if the player is prioritized
     */
    private boolean isPrioritized(Player player) {
        return isPrioritized(player.getUniqueId());
    }

    /**
     * Checks if the player is prioritized
     * @param playerUUID The UUID of the player
     * @return true if the player is prioritized
     */
    private boolean isPrioritized(UUID playerUUID) {
        return prioritizedPlayerMap.getOrDefault(playerUUID, false) ||
                prioritizedPlayerHandler.isInConfig(playerUUID);
    }

    /**
     * Adds the player to the {@link #prioritizedPlayerMap} whenever the player is prioritized
     * @param player The player
     */
    private void addToMapIfPrioritizedByPermission(Player player) {
        if(SimpleQueuePermission.PRIORITIZED_PLAYER.hasPermission(player))
            prioritizedPlayerMap.put(player.getUniqueId(), true);
    }

    /**
     * Adds a player to the queue.<br>
     * If prioritized({@link #isPrioritized(UUID)}): The player will be added before the normal players.<br>
     * If putFirst: The player will be added to the first pos of the queue
     * @param player The player
     * @return The queue <b>INDEX</b> (You have to add 1 to get the "position")
     */
    private int addPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        addToMapIfPrioritizedByPermission(player);

        boolean isThisPrioritized = isPrioritized(player);

        if(isThisPrioritized) {
            //Insert prioritized player before normal players
            for(int i = 0; i < playerQueue.size(); i++) {
                if(!isPrioritized(playerQueue.get(i))) {
                    playerQueue.add(i, playerUUID);
                    return i;
                }
            }
        }
        playerQueue.addLast(playerUUID);
        return playerQueue.size() - 1;
    }

    /**
     * Removes a given player from the queue
     * @param player
     */
    void removePlayer(Player player) {
        removePlayer(player.getUniqueId());
    }


    /**
     * Removes a given player from the queue
     * @param playerUUID
     */
    void removePlayer(UUID playerUUID) {
        playerQueue.remove(playerUUID);
        lastSeenMap.remove(playerUUID);
    }

    /***
     * Removes players that are longer in the queue then {@link #mustReconnectWithinSec}
     */
    private void removeExceededPlayers() {
        long timestampMustBeMore = System.currentTimeMillis() - (mustReconnectWithinSec*1000);

        playerQueue.removeIf(uuid -> lastSeenMap.get(uuid) < timestampMustBeMore);
    }
}
