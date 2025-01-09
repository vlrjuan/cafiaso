package org.cafiaso.server.network.buffers;

import org.cafiaso.server.network.DataType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

/**
 * A buffer for writing data to a {@link DataOutputStream}.
 * <p>
 * For writing data to a {@link java.nio.channels.SocketChannel}, use {@link OutputBuffer#fromChannel(WritableByteChannel)}.
 */
public class OutputBuffer implements AutoCloseable {

    private final DataOutputStream output;

    /**
     * OutputBuffer constructor.
     *
     * @param output the output stream to write to
     */
    public OutputBuffer(DataOutputStream output) {
        this.output = output;
    }

    /**
     * Creates a buffer from a {@link WritableByteChannel}.
     *
     * @param channel the channel to write to
     * @return the {@link OutputBuffer} instance
     */
    public static OutputBuffer fromChannel(WritableByteChannel channel) {
        return new OutputBuffer(new DataOutputStream(Channels.newOutputStream(channel)));
    }

    @Override
    public void close() throws IOException {
        output.close();
    }

    /**
     * Writes a value of the specified type to the buffer.
     *
     * @param type  the data type
     * @param value the value
     * @param <T>   the value type
     * @throws IOException if an I/O error occurs
     */
    public <T> void write(DataType<T> type, T value) throws IOException {
        type.write(output, value);
    }
}
