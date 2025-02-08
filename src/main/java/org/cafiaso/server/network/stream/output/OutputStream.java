package org.cafiaso.server.network.stream.output;

import org.cafiaso.server.network.Serializer;

import java.io.IOException;

/**
 * Represents an output stream in which data can be written to.
 */
public interface OutputStream extends AutoCloseable {

    /**
     * Writes data to this output stream.
     *
     * @param serializer the serializer to use to write the packet
     * @param value      the packet to write
     * @param <T>        the type of packet to write
     * @throws IOException if an I/O error occurs
     */
    <T> void write(Serializer<T> serializer, T value) throws IOException;

    @Override
    void close() throws IOException;
}
