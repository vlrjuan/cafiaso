package org.cafiaso.server.network;

import org.cafiaso.server.network.connection.Connection;

import java.io.IOException;

/**
 * Represents a handler for a packet sent by the client to the server.
 * <p>
 * The handler is responsible for processing the packet and sending a response back to the client if necessary.
 *
 * @param <P> the packet type
 */
public interface PacketHandler<P extends ClientPacket> {

    /**
     * Handles the given packet.
     *
     * @param connection the connection that sent the packet
     * @param packet     the packet to handle
     * @throws IOException if an I/O error occurs
     */
    void handle(Connection connection, P packet) throws IOException;
}
