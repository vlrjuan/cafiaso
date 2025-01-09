package org.cafiaso.server;

import org.cafiaso.server.configuration.PropertiesServerConfiguration;
import org.cafiaso.server.configuration.ServerConfiguration;
import org.cafiaso.server.network.server.NetworkServer;
import org.cafiaso.server.network.server.SocketNetworkServer;
import org.cafiaso.server.player.PlayerManager;
import org.cafiaso.server.player.PlayerManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Default implementation of the {@link Server} interface.
 */
public class ServerImpl implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerImpl.class);

    /**
     * The server configuration.
     */
    private final ServerConfiguration configuration;

    /**
     * The network server.
     */
    private final NetworkServer networkServer;

    /**
     * The player manager.
     */
    private final PlayerManager playerManager;

    /**
     * Whether the server is running.
     */
    private volatile boolean isRunning = false;

    /**
     * Server constructor.
     */
    public ServerImpl() {
        this.configuration = new PropertiesServerConfiguration(getClass().getClassLoader());
        this.networkServer = new SocketNetworkServer(this);
        this.playerManager = new PlayerManagerImpl();
    }

    /**
     * Server constructor.
     *
     * @param playerManager the player manager
     * @param networkServer the network server
     * @param configuration the server configuration
     */
    public ServerImpl(PlayerManager playerManager, NetworkServer networkServer, ServerConfiguration configuration) {
        this.playerManager = playerManager;
        this.networkServer = networkServer;
        this.configuration = configuration;
    }

    @Override
    public void start(String host, int port) {
        LOGGER.info("Starting server on {}:{}", host, port);

        // Load the server configuration
        configuration.load();

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        isRunning = true;

        try {
            // Start the socket server
            networkServer.bind(host, port);
        } catch (IOException e) {
            LOGGER.error("Failed to start socket server. Stopping...", e);

            stop();
        }
    }

    @Override
    public boolean stop() {
        if (!isRunning) {
            return true;
        }

        boolean success = true;

        LOGGER.info("Stopping server");

        isRunning = false;

        try {
            networkServer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to close socket server", e);

            success = false;
        }

        LOGGER.info("Server stopped");

        return success;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public ServerConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
