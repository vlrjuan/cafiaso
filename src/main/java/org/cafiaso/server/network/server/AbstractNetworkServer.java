package org.cafiaso.server.network.server;

import org.cafiaso.server.network.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of a {@link NetworkServer} that provides common functionality for network servers.
 */
public abstract class AbstractNetworkServer implements NetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNetworkServer.class);

    /**
     * The active connections as a map of addresses to connections.
     * <p>
     * This map is thread-safe.
     */
    private final Map<InetAddress, Connection> connections = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void close() throws IOException {
        synchronized (connections) {
            connections.values().forEach(connection -> {
                try {
                    connection.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close connection {}", connection, e);
                }
            });

            connections.clear();
        }
    }

    /**
     * Closes the connection if it exists but does not remove it from the connections map.
     * <p>
     * If the connection does not exist, this method does nothing.
     *
     * @param address the address of the connection
     * @throws IOException if an I/O error occurs
     */
    protected void closeConnectionIfExist(InetAddress address) throws IOException {
        Connection connection = connections.get(address);
        if (connection != null) {
            LOGGER.debug("Closing existing connection from {}", address);
            connection.close();
        }
    }

    /**
     * Accepts an incoming connection and starts reading packets from it, asynchronously.
     */
    protected void acceptConnection(Connection connection) {
        LOGGER.info("Incoming connection from {}", connection.getAddress());

        Thread.startVirtualThread(() -> {
            while (connection.isOpen()) {
                try {
                    connection.readPacket();
                } catch (Exception e) {
                    LOGGER.error("Failed to read from connection {}", connection, e);

                    break;
                }
            }
        });

        connections.put(connection.getAddress(), connection);
    }
}