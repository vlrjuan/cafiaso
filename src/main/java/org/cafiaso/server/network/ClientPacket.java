package org.cafiaso.server.network;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Represents a packet sent by the client to the server.
 * <p>
 * Packets must be registered in {@link org.cafiaso.server.network.connection.ConnectionState} and are handled by a
 * {@link org.cafiaso.server.network.PacketHandler}.
 * <p>
 * <em>Implementations of this interface must have a no-argument constructor.</em>
 */
public interface ClientPacket {

    /**
     * Reads the packet data from the given input stream.
     * <p>
     * Implementations are encouraged to use {@link DataType}s to read the data.
     *
     * @param in the input stream
     * @throws IOException if an I/O error occurs
     */
    void read(DataInputStream in) throws IOException;
}
