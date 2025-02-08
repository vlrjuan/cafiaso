package org.cafiaso.server.network.server;

import org.cafiaso.server.network.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Partial implementation of {@link NetworkServer} that provides common functionality for network servers
 * such as managing active {@link Connection}s.
 */
public abstract class AbstractNetworkServer implements NetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNetworkServer.class);

    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (IOException e) {
            LOGGER.error("Failed to close connection {}", connection, e);
        }

        connections.remove(connection);
    }

    @Override
    public void close() throws IOException {
        synchronized (connections) {
            for (Connection connection : connections) {
                try {
                    connection.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close connection {}", connection, e);
                }
            }
        }

        connections.clear();
    }

    /**
     * Gets the list of all active connections.
     *
     * @return the list of all active connections
     */
    public List<Connection> getConnections() {
        return connections;
    }

    /**
     * Accepts an incoming connection and starts reading packets, asynchronously.
     *
     * @param connection the connection to accept
     */
    protected void acceptConnection(Connection connection) {
        LOGGER.info("Incoming connection from {}", connection);

        Thread.startVirtualThread(() -> {
            while (connection.isOpen()) {
                try {
                    connection.readPackets();
                } catch (Exception e) {
                    LOGGER.error("Failed to read from connection {}. Closing connection", connection, e);

                    closeConnection(connection);

                    break;
                }
            }
        });

        connections.add(connection);
    }
}
