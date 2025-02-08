package org.cafiaso.server;

import org.cafiaso.server.configuration.ServerConfiguration;
import org.cafiaso.server.mojang.MojangClient;
import org.cafiaso.server.network.server.NetworkServer;
import org.cafiaso.server.player.PlayerManager;
import org.cafiaso.server.security.SecurityManager;

/**
 * The main server class.
 * <p>
 * It is responsible for starting and stopping the {@link NetworkServer} and managing all the managers.
 * <p>
 * The server can be started by calling {@link #start(String, int)} and should be stopped by killing the process.
 */
public interface Server {

    /**
     * The server version.
     */
    String MINECRAFT_VERSION = "1.21.4";

    /**
     * The server protocol version.
     */
    int PROTOCOL_VERSION = 769;

    /**
     * Starts the server on the specified host and port.
     *
     * @param host the host to bind the server to
     * @param port the port to bind the server to
     */
    void start(String host, int port);

    /**
     * Stops the server.
     * <p>
     * This will silently fail if the server is not running.
     *
     * @return {@code true} if the server was stopped successfully, {@code false} otherwise
     */
    boolean stop();

    /**
     * Checks whether the server is running.
     *
     * @return {@code true} if the server is running, {@code false} otherwise
     */
    boolean isRunning();

    /**
     * Gets the server configuration.
     *
     * @return the server configuration
     */
    ServerConfiguration getConfiguration();

    /**
     * Gets the network server.
     *
     * @return the network server
     */
    NetworkServer getNetworkServer();

    /**
     * Gets the security manager.
     *
     * @return the security manager
     */
    SecurityManager getSecurityManager();

    /**
     * Gets the player manager.
     *
     * @return the player manager
     */
    PlayerManager getPlayerManager();

    /**
     * Gets the mojang client.
     *
     * @return the mojang client
     */
    MojangClient getMojangClient();
}
