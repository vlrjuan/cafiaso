package org.cafiaso.server.network.stream.input;

import org.cafiaso.server.network.Serializer;

import java.io.IOException;

/**
 * Represents an input stream in which data can be read from.
 */
public interface InputStream extends AutoCloseable {

    /**
     * Reads data from this input stream.
     *
     * @param serializer the serializer to use to read the packet
     * @param <T>        the type of packet to read
     * @return the packet read from the input stream
     * @throws IOException if an I/O error occurs
     */
    <T> T read(Serializer<T> serializer) throws IOException;

    /**
     * Returns the number of bytes available to read from this input stream.
     *
     * @return the number of available bytes
     * @throws IOException if an I/O error occurs
     */
    int available() throws IOException;

    /**
     * Returns whether this input stream is empty.
     * <p>
     * By default, this method is equivalent to calling {@code available() == 0}.
     *
     * @return {@code true} if this input stream is empty, {@code false} otherwise
     * @throws IOException if an I/O error occurs
     */
    default boolean isEmpty() throws IOException {
        return available() == 0;
    }

    @Override
    void close() throws IOException;
}
