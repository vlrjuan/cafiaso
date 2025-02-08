package org.cafiaso.server.network.packet.client;

import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;

/**
 * Represents a packet sent by the client.
 * <p>
 * Packets must be registered in {@link ConnectionState} and are handled by a {@link PacketHandler}.
 * <p>
 * Packets must have at least one constructor with no parameters to be instantiated by reflection.
 * <p>
 * Packet deserialization is done by reading the data from a {@link InputStream}.
 */
public interface ClientPacket {

    /**
     * Reads the packet data from the given {@link InputStream}.
     *
     * @param in the input to read from
     * @throws IOException if an I/O error occurs
     */
    void read(InputStream in) throws IOException;
}
