package org.cafiaso.server.network.packet.server;

import org.cafiaso.server.network.buffers.OutputBuffer;
import org.cafiaso.server.network.types.VarIntDataType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents a packet that can be sent to the client
 * using {@link org.cafiaso.server.network.connection.Connection#sendPacket(ServerPacket)}.
 */
public interface ServerPacket {

    /**
     * Gets the packet ID.
     *
     * @return the packet ID
     */
    int getId();

    /**
     * Gets the length of the packet when serialized.
     * <p>
     * <code>LENGTH = PACKET ID + PACKET DATA</code>
     *
     * @return the packet length
     * @throws IOException if an I/O error occurs
     */
    default int getLength() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (OutputBuffer buffer = new OutputBuffer(new DataOutputStream(out))) {
            write(buffer);

            return out.size() + VarIntDataType.getSize(getId());
        }
    }

    /**
     * Writes the packet data to the given buffer.
     *
     * @param buffer the buffer to write to
     * @throws IOException if an I/O error occurs
     */
    void write(OutputBuffer buffer) throws IOException;
}
