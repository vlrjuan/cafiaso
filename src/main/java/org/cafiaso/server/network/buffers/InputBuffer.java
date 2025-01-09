package org.cafiaso.server.network.buffers;

import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * A buffer for reading data from a {@link DataInputStream}.
 * <p>
 * For reading data from a {@link java.nio.channels.SocketChannel}, use {@link InputBuffer#fromChannel(ReadableByteChannel)}.
 */
public class InputBuffer implements AutoCloseable {

    private final DataInputStream input;

    /**
     * InputBuffer constructor.
     *
     * @param input the input stream to read from
     */
    public InputBuffer(DataInputStream input) {
        this.input = input;
    }

    /**
     * Creates a buffer from a {@link ReadableByteChannel}.
     *
     * @param channel the channel to read from
     * @return the {@link InputBuffer} instance
     */
    public static InputBuffer fromChannel(ReadableByteChannel channel) {
        return new InputBuffer(new DataInputStream(Channels.newInputStream(channel)));
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    /**
     * Reads a value of the specified type from the buffer.
     *
     * @param type the data type
     * @param <T>  the value type
     * @return the value
     * @throws IOException if an I/O error occurs
     */
    public <T> T read(DataType<T> type) throws IOException {
        return type.read(input);
    }

    /**
     * Checks if the buffer is empty.
     *
     * @return {@code true} if the buffer is empty, {@code false} otherwise
     * @throws IOException if an I/O error occurs
     */
    public boolean isEmpty() throws IOException {
        return input.available() == 0;
    }
}
