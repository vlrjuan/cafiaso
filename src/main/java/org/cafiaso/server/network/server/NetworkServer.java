package org.cafiaso.server.network.server;

import java.io.IOException;

/**
 * Represents a network server.
 * <p>
 * A network server is responsible for listening for incoming {@link org.cafiaso.server.network.connection.Connection}s
 * and handling them.
 */
public interface NetworkServer {

    /**
     * Binds the server to the specified host and port.
     *
     * @param host the host to bind to
     * @param port the port to bind to
     * @throws IOException if an I/O error occurs
     */
    void bind(String host, int port) throws IOException;

    /**
     * Closes the server and all underlying connections.
     *
     * @throws IOException if an I/O error occurs
     */
    void close() throws IOException;
}
