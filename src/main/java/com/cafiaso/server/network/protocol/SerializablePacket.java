package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.io.NetworkBuffer;

import java.io.IOException;

/**
 * Represents a packet that can be sent to the client using a {@link PacketDispatcher}.
 * <p>
 * Packet serialization is done by writing the data to a {@link NetworkBuffer}.
 */
public interface SerializablePacket {

    /**
     * Gets the packet resource ID.
     * <p>
     * This id refers to the unique identifier set by the Notchian client.
     * It must be unique for each packet in a given {@link ConnectionState}.
     *
     * @return the packet resource ID
     */
    String resourceId();

    /**
     * Serializes the packet data to the given {@link NetworkBuffer}, excluding the packet ID and length.
     *
     * @param out the buffer to write to
     * @throws IOException if an I/O error occurs while writing the packet data
     */
    default void serialize(NetworkBuffer out) throws IOException {

    }
}
