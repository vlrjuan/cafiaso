package com.cafiaso.server.network.server;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionRegistry;
import com.cafiaso.server.network.processor.PacketProcessor;
import com.cafiaso.server.network.processor.PacketProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

/**
 * The network server instance.
 * <p>
 * This class is responsible for listening and accepting incoming {@link Connection}s.
 * <p>
 * The server can be started by calling {@link #bind(String, int)} with the desired host and port
 * and can be stopped by calling {@link #close()}.
 */
public abstract class NetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkServer.class);

    private final ExecutorService executorService;
    private final ConnectionRegistry connectionRegistry;
    private final PacketProcessorFactory packetProcessorFactory;

    public NetworkServer(
            ExecutorService executorService,
            ConnectionRegistry connectionRegistry,
            PacketProcessorFactory packetProcessorFactory
    ) {
        this.executorService = executorService;
        this.connectionRegistry = connectionRegistry;
        this.packetProcessorFactory = packetProcessorFactory;
    }

    /**
     * Binds the server to the specified host and port and starts listening for incoming connections,
     * asynchronously.
     *
     * @param host the host to listen on
     * @param port the port to listen on
     */
    public abstract void bind(String host, int port) throws IOException;

    /**
     * Closes the server and releases all connections.
     * <p>
     * This method will silently fail if the server is not running.
     */
    public void close() throws IOException {
        connectionRegistry.closeAllConnections();
    }

    /**
     * Accepts a new connection and starts reading packets from it.
     *
     * @param connection the accepted connection
     */
    protected void acceptConnection(Connection connection) {
        LOGGER.info("Incoming connection from {}", connection);

        // Add the connection in the registry
        connectionRegistry.addConnection(connection);

        PacketProcessor packetProcessor = packetProcessorFactory.create(connection);

        // Start a new thread to read from the connection
        executorService.submit(() -> {
            while (connection.isOpen()) {
                try {
                    packetProcessor.process();
                } catch (SocketException e) {
                    // Connection reset by peer or other socket error
                    LOGGER.info("Connection {} closed by remote host", connection);

                    connectionRegistry.closeConnection(connection);
                } catch (Exception e) {
                    LOGGER.error("Failed to read from connection {}. Closing connection", connection, e);

                    connectionRegistry.closeConnection(connection);
                }
            }
        });
    }
}
