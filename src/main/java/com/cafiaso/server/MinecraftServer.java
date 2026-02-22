package com.cafiaso.server;

import com.cafiaso.server.network.server.NetworkServer;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Minecraft server instance.
 * <p>
 * This class is the main entry point and is responsible for managing the server lifecycle.
 * This class performs minimal logic itself, delegating most responsibilities to the {@link NetworkServer}.
 * <p>
 * The server can be started by calling {@link #start(String, int)} with the desired host and port
 * and can be stopped by calling either {@link #stop()} or by killing the process.
 * <p>
 */
public class MinecraftServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftServer.class);

    private final NetworkServer networkServer;

    private boolean running = false;

    @Inject
    public MinecraftServer(NetworkServer networkServer) {
        this.networkServer = networkServer;
    }

    /**
     * Starts the server on the specified host and port.
     * <p>
     * This method will simply bind the {@link NetworkServer} to the specified address
     * and start listening for incoming connections.
     *
     * @param host the host to listen on
     * @param port the port to listen on
     * @throws IllegalStateException if the server is already running
     */
    public void start(String host, int port) {
        if (running) {
            throw new IllegalStateException("Server is already running");
        }

        try {
            // Bind the network server and start listening for connections
            networkServer.bind(host, port);

            running = true;

            LOGGER.info("Server started on {}:{}", host, port);
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }

    /**
     * Stops the server and releases all resources.
     * <p>
     * Will fail silently if the server is not running.
     */
    public void stop() {
        if (!running) {
            return;
        }

        try {
            // Close the network server
            networkServer.close();

            LOGGER.info("Server stopped");
        } catch (IOException e) {
            throw new RuntimeException("Failed to stop server", e);
        } finally {
            running = false;
        }
    }
}
