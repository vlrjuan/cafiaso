package org.cafiaso.server;

import org.cafiaso.server.configuration.PropertiesServerConfiguration;
import org.cafiaso.server.configuration.ServerConfiguration;
import org.cafiaso.server.mojang.MojangClient;
import org.cafiaso.server.mojang.MojangClientImpl;
import org.cafiaso.server.network.server.NetworkServer;
import org.cafiaso.server.network.server.SocketNetworkServer;
import org.cafiaso.server.player.PlayerManager;
import org.cafiaso.server.player.PlayerManagerImpl;
import org.cafiaso.server.security.SecurityManager;
import org.cafiaso.server.security.SecurityManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Default {@link Server} implementation.
 */
public class ServerImpl implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerImpl.class);

    private final ServerConfiguration configuration;

    private final NetworkServer networkServer;

    private final SecurityManager securityManager;
    private final PlayerManager playerManager;

    private final MojangClient mojangClient;

    private volatile boolean isRunning = false;

    /**
     * Default ServerImpl constructor.
     */
    public ServerImpl() {
        this.configuration = new PropertiesServerConfiguration(getClass().getClassLoader());

        this.networkServer = new SocketNetworkServer(this);

        this.securityManager = new SecurityManagerImpl();
        this.playerManager = new PlayerManagerImpl();

        this.mojangClient = new MojangClientImpl();
    }

    /**
     * ServerImpl constructor used for testing.
     *
     * @param configuration   the server configuration
     * @param networkServer   the network server
     * @param securityManager the security manager
     * @param playerManager   the player manager
     * @param mojangClient    the Mojang client
     */
    public ServerImpl(
            ServerConfiguration configuration,
            NetworkServer networkServer,
            SecurityManager securityManager,
            PlayerManager playerManager,
            MojangClient mojangClient
    ) {
        this.configuration = configuration;
        this.networkServer = networkServer;
        this.securityManager = securityManager;
        this.mojangClient = mojangClient;
        this.playerManager = playerManager;
    }

    @Override
    public void start(String host, int port) {
        LOGGER.info("Starting server on {}:{}", host, port);

        configuration.load();

        // Generate a new public/private key pair for packet encryption
        securityManager.generateKeyPair();

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
    public NetworkServer getNetworkServer() {
        return networkServer;
    }

    @Override
    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public MojangClient getMojangClient() {
        return mojangClient;
    }
}
