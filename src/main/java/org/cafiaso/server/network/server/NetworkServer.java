package org.cafiaso.server.network.server;

import org.cafiaso.server.network.connection.Connection;

import java.io.IOException;

/**
 * Represents a network server.
 * <p>
 * A network server is responsible for listening for incoming {@link Connection}s and handling them.
 * <p>
 * The server will start listening for incoming connections as soon as {@link #bind(String, int)} is called.
 */
public interface NetworkServer {

    /**
     * Starts the server to the specified host and port.
     * <p>
     * Blocks until the server is closed.
     *
     * @param host the host to bind to
     * @param port the port to bind to
     * @throws IOException if an I/O error occurs while starting the server
     */
    void bind(String host, int port) throws IOException;

    /**
     * Closes the specified connection.
     *
     * @param connection the connection to close
     */
    void closeConnection(Connection connection);

    /**
     * Closes the server and all underlying connections.
     *
     * @throws IOException if an I/O error occurs while closing the server
     */
    void close() throws IOException;
}
