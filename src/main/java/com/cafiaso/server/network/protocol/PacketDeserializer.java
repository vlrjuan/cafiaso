package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.io.NetworkBuffer;

import java.io.IOException;

/**
 * A deserializer for a packet sent by the client to the server.
 * <p>
 * Packet deserialization is done by reading the data from a {@link NetworkBuffer}.
 *
 * @param <P> the packet type
 */
public interface PacketDeserializer<P> {

    /**
     * Deserializes a packet from the given {@link NetworkBuffer}.
     *
     * @param in the buffer to read from
     * @return the deserialized packet
     * @throws IOException if an I/O error occurs while reading the packet data
     */
    P deserialize(NetworkBuffer in) throws IOException;
}
