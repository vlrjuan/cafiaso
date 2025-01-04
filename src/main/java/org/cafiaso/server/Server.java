package org.cafiaso.server;

import org.cafiaso.server.configuration.PropertiesServerConfiguration;
import org.cafiaso.server.configuration.ServerConfiguration;
import org.cafiaso.server.network.server.NetworkServer;
import org.cafiaso.server.network.server.SocketNetworkServer;
import org.cafiaso.server.player.PlayerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The main server class.
 * <p>
 * It is responsible for starting and stopping the {@link NetworkServer} and managing all the managers.
 * <p>
 * The server can be started by calling {@link #start(String, int)} and stopped by calling {@link #stop()} or by killing
 * the process.
 */
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * The server version.
     */
    public static final String VERSION_NAME = "1.21.4";

    /**
     * The server protocol version.
     */
    public static final int VERSION_PROTOCOL = 769;

    /**
     * The server configuration.
     */
    private final ServerConfiguration configuration = new PropertiesServerConfiguration();

    /**
     * The network server.
     */
    private final NetworkServer networkServer;

    /**
     * The player manager.
     */
    private final PlayerManager playerManager = new PlayerManager();

    /**
     * Whether the server is running.
     */
    private volatile boolean running = false;

    /**
     * Server constructor.
     */
    public Server() {
        this.networkServer = new SocketNetworkServer(this);
    }

    /**
     * Starts the server on the specified host and port.
     * <p>
     * This method will block the current thread until the server is stopped.
     *
     * @param host the host to bind the server to
     * @param port the port to bind the server to
     */
    public void start(String host, int port) {
        long now = System.currentTimeMillis();

        LOGGER.info("Starting server on {}:{}", host, port);

        // Load the server configuration
        configuration.load();

        try {
            // Start the socket server
            networkServer.bind(host, port);
        } catch (IOException e) {
            LOGGER.error("Failed to start socket server", e);
        }

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        running = true;

        LOGGER.info("Server started in {}ms", System.currentTimeMillis() - now);

        // Main server loop
        while (running) {
            Thread.onSpinWait();
        }
    }

    /**
     * Stops the server.
     */
    public void stop() {
        if (!running) {
            throw new IllegalStateException("Server is not running");
        }

        LOGGER.info("Stopping server");

        try {
            networkServer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to close socket server", e);
        }

        running = false;

        LOGGER.info("Server stopped");
    }

    /**
     * Returns the server configuration.
     *
     * @return the server configuration
     */
    public ServerConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Returns the player manager.
     *
     * @return the player manager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
