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

    /**
     * Loads the configuration.
     */
    void load();

    /**
     * Gets the maximum number of players that can be connected to the server.
     * <p>
     * The default value is 20.
     *
     * @return the maximum number of players
     */
    int getMaximumPlayers();

    /**
     * Gets the description (motd) of the server.
     * <p>
     * The default value is "A Minecraft Server".
     *
     * @return the description of the server
     */
    String getDescription();
}
