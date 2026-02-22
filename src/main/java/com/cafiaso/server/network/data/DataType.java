package com.cafiaso.server.network.data;

import com.cafiaso.server.network.io.NetworkBuffer;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A generic interface for data types used in the protocol.
 * <p>
 * Each data type is responsible for reading and writing its value
 * to and from a {@link NetworkBuffer}.
 * <p>
 * Data type instances are available in {@link DataTypes} for easy access.
 *
 * @param <T> the type of data to be read and written
 */
public interface DataType<T> {

    /**
     * Returns the class type of the data.
     *
     * @return the class type of the data
     */
    Class<T> getType();

    /**
     * Reads a value of type {@code T} from the given {@link NetworkBuffer}.
     *
     * @param in the buffer to read from
     * @return the read value
     * @throws IOException if an I/O error occurs
     */
    T read(NetworkBuffer in) throws IOException;

    /**
     * Writes a value of type {@code T} to the given {@link DataOutputStream}.
     *
     * @param value the value to write
     * @param out   the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    void write(T value, NetworkBuffer out) throws IOException;
}
