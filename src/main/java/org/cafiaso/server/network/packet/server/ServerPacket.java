package org.cafiaso.server.network.packet.server;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.network.stream.output.OutputStream;

import java.io.IOException;

/**
 * Represents a packet that can be sent to the client using {@link Connection#sendPacket(ServerPacket)}.
 * <p>
 * Packet serialization is done by writing the data to an {@link OutputStream}.
 */
public abstract class ServerPacket {

    private final int id;

    /**
     * ServerPacket constructor.
     *
     * @param id the packet ID. Must be unique for a given {@link ConnectionState}.
     */
    public ServerPacket(int id) {
        this.id = id;
    }

    /**
     * Gets the packet ID.
     *
     * @return the packet ID
     */
    public int getId() {
        return id;
    }

    /**
     * Writes the packet data to the given {@link OutputStream}.
     *
     * @param out the output to write to
     * @throws IOException if an I/O error occurs
     */
    public abstract void write(OutputStream out) throws IOException;
}
