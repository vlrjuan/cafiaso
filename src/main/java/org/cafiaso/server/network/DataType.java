package org.cafiaso.server.network;

import org.cafiaso.server.network.types.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents a data type that can be read and written from a packet.
 * <p>
 * Packets implementations should use these data types instead of directly reading and writing from the input and output
 * streams.
 *
 * @param <T> the type of the data
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Data_types">Protocol documentation</a>
 */
public interface DataType<T> {

    DataType<Integer> UNSIGNED_BYTE = new UnsignedByteDataType();
    DataType<Integer> UNSIGNED_SHORT = new UnsignedShortDataType();
    DataType<Long> LONG = new LongDataType();
    DataType<String> STRING = new StringDataType();
    DataType<Integer> VAR_INT = new VarIntDataType();

    /**
     * Reads a value of this data type from the given input stream.
     *
     * @param in the input stream
     * @return the value
     * @throws IOException if an I/O error occurs
     */
    T read(DataInputStream in) throws IOException;

    /**
     * Writes the given value of this data type to the given output stream.
     *
     * @param out   the output stream
     * @param value the value
     * @throws IOException if an I/O error occurs
     */
    void write(DataOutputStream out, T value) throws IOException;

    /**
     * Gets the size of the given value when serialized.
     * <p>
     * Mainly used to calculate the size of a packet id (VarInt).
     * Other implementations are not required to implement this method.
     *
     * @param value the value
     * @return the size of the value
     */
    default int getSize(T value) {
        return 0;
    }
}
