package org.cafiaso.server.network.packet.client;

import org.cafiaso.server.network.buffers.InputBuffer;
import org.cafiaso.server.network.handler.PacketHandler;

import java.io.IOException;

/**
 * Represents a packet sent by the client.
 * <p>
 * Packets must be registered in {@link org.cafiaso.server.network.connection.ConnectionState} and are handled by a
 * {@link PacketHandler}.
 */
public interface ClientPacket {

    /**
     * Reads the packet data from the given buffer.
     *
     * @param buffer the buffer to read from
     * @throws IOException if an I/O error occurs
     */
    void read(InputBuffer buffer) throws IOException;
}
