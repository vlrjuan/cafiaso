package org.cafiaso.server.configuration;

/**
 * Represents an immutable configuration for the server.
 * <p>
 * The configuration should not be considered fully usable until calling {@link #load()}.
 * <p>
 * Implementations must provide a default value for each configuration option as specified in the method documentation,
 * even if it has not been loaded yet.
 */
public interface ServerConfiguration {

    int DEFAULT_MAXIMUM_PLAYERS = 20;

    String DEFAULT_DESCRIPTION = "A Minecraft Server";

    /**
     * Loads the configuration.
     */
    void load();

    /**
     * Gets the maximum number of players that can be connected to the server.
     * <p>
     * Defaults to <code>DEFAULT_MAXIMUM_PLAYERS</code>
     *
     * @return the maximum number of players
     */
    int getMaximumPlayers();

    /**
     * Gets the description (motd) of the server.
     * <p>
     * Defaults to <code>DEFAULT_DESCRIPTION</code>
     *
     * @return the description of the server
     */
    String getDescription();
}
