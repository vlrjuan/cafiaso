package com.cafiaso.server.configuration;

/**
 * The server configuration.
 *
 * @see PropertiesServerConfiguration
 */
public interface ServerConfiguration {

    int DEFAULT_MAX_PLAYERS = 20;

    String DEFAULT_DESCRIPTION = "A Minecraft Server";

    /**
     * Returns the maximum number of players that can be connected to the server.
     * <p>
     * Defaults to {@value DEFAULT_MAX_PLAYERS}.
     *
     * @return the maximum number of players
     */
    int getMaxPlayers();

    /**
     * Returns the description of the server, also known as {@code motd} (Message Of The Day).
     * <p>
     * The motd is displayed in the server list in the Minecraft client.
     * <p>
     * Defaults to {@value DEFAULT_DESCRIPTION}.
     *
     * @return the server description
     */
    String getDescription();
}
