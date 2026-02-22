package com.cafiaso.server.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the players connected to the server.
 */
public class PlayerManager {

    private final Map<UUID, Player> players = new HashMap<>();

    /**
     * Gets all connected players.
     *
     * @return a collection of all connected players
     */
    public Collection<Player> getPlayers() {
        return players.values();
    }
}
