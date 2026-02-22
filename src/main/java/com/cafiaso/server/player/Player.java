package com.cafiaso.server.player;

import java.util.UUID;

/**
 * Represents a player in the game.
 */
public interface Player {

    /**
     * Gets the player's unique identifier.
     *
     * @return the player's unique identifier
     */
    UUID getId();

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    String getName();
}
