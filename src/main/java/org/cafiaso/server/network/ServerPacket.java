package org.cafiaso.server.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents a packet sent by the server to the client.
 */
public abstract class ServerPacket {

    /**
     * The packet ID.
     */
    private final int id;

    /**
     * ServerPacket constructor.
     *
     * @param id the packet ID
     */
    public ServerPacket(int id) {
        this.id = id;
    }

    /**
     * Writes the packet data to the given output stream.
     * <p>
     * Implementations are encouraged to use {@link DataType}s to write the data.
     *
     * @param out the output stream
     * @throws IOException if an I/O error occurs
     */
    public abstract void write(DataOutputStream out) throws IOException;

    /**
     * Gets the ID of the packet.
     *
     * @return the packet ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the length of the packet when serialized.
     * <p>
     * <code>LENGTH = PACKET ID + PACKET DATA</code>
     *
     * @return the packet length
     * @throws IOException if an I/O error occurs
     */
    public int getLength() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buffer);

        write(out);

        return buffer.size() + DataType.VAR_INT.getSize(id);
    }
}
