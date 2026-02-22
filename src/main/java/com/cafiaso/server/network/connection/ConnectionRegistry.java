package com.cafiaso.server.network.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A registry to manage active {@link Connection}s.
 */
public class ConnectionRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionRegistry.class);

    private final Set<Connection> connections = ConcurrentHashMap.newKeySet();

    /**
     * Adds a new connection to the registry.
     *
     * @param connection the connection to add
     */
    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    /**
     * Closes a connection and removes it from the registry.
     *
     * @param connection the connection to close
     */
    public void closeConnection(Connection connection) {
        try {
            connection.close();

            LOGGER.info("Closed connection {}", connection);
        } catch (IOException e) {
            LOGGER.error("Failed to close connection {}", connection, e);
        }

        connections.remove(connection);
    }

    /**
     * Closes all connections in the registry.
     * <p>
     * This method is strictly equivalent to:
     * <pre>
     * {@code
     * for (Connection connection : getConnections()) {
     *     closeConnection(connection);
     * }
     * }
     * </pre>
     */
    public void closeAllConnections() {
        connections.forEach(this::closeConnection);
    }
}
