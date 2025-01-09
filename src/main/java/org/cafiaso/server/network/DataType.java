package org.cafiaso.server.network;

import org.cafiaso.server.network.types.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;

/**
 * Represents a data type that can be read and written from a packet.
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

    static <E extends Enum<E>, T, D extends DataType<T>> DataType<E> ENUM(Class<E> enumClass, D dataType, Function<E, T> getter) {
        return new EnumDataType<>(enumClass, dataType, getter);
    }

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
}
