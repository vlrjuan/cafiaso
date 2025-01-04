package org.cafiaso.server.network.connection;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.ServerPacket;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Represents a connection to a client in the server.
 * <p>
 * The connection is responsible for reading and writing packets to the client.
 */
public interface Connection {

    /**
     * Reads <em>at most one</em> packet from the client.
     * <p>
     * If no packet is available, this method should return immediately.
     *
     * @throws IOException if an I/O error occurs
     */
    void readPacket() throws IOException;

    /**
     * Sends a packet to the client.
     *
     * @param packet the packet to send
     * @throws IOException if an I/O error occurs
     */
    void sendPacket(ServerPacket packet) throws IOException;

    /**
     * Sets the state of the connection.
     *
     * @param state the new connection state
     */
    void setState(ConnectionState state);

    /**
     * Gets the {@link Server} instance.
     *
     * @return the server instance
     */
    Server getServer();

    /**
     * Gets the address of the client.
     *
     * @return the address of the client
     */
    InetAddress getAddress();

    /**
     * Checks if the connection is open.
     *
     * @return {@code true} if the connection is open, {@code false} otherwise
     */
    boolean isOpen();

    /**
     * Closes the connection to the client and releases any resources.
     *
     * @throws IOException if an I/O error occurs
     */
    void close() throws IOException;
}
