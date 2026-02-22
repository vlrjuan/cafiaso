package com.cafiaso.server.network.dispatcher;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.SerializablePacket;

import java.io.IOException;

/**
 * A dispatcher to send a {@link SerializablePacket} to the client.
 */
public interface PacketDispatcher {

    /**
     * Sends a packet to the given client.
     *
     * @param connection the connection to send the packet to
     * @param packet     the packet to send
     * @throws IOException if an I/O error occurs while sending the packet
     */
    void dispatch(Connection connection, SerializablePacket packet) throws IOException;
}
