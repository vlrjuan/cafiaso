package org.cafiaso.server.network.stream.input;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * {@link InputStream} implementation that reads from a byte array.
 */
public class ByteArrayInputStream implements InputStream {

    private final DataInputStream in;

    /**
     * ByteArrayInputStream constructor.
     *
     * @param data the byte array to read from
     */
    public ByteArrayInputStream(byte[] data) {
        this.in = new DataInputStream(new java.io.ByteArrayInputStream(data));
    }

    /**
     * Creates an empty {@link ByteArrayInputStream}.
     * <p>
     * This method is equivalent to calling {@code new ByteArrayInputStream(new byte[0])}.
     * <p>
     * Mainly used for testing purposes.
     *
     * @return the empty {@link ByteArrayInputStream}
     */
    public static ByteArrayInputStream empty() {
        return new ByteArrayInputStream(new byte[0]);
    }

    @Override
    public <T> T read(Serializer<T> serializer) throws IOException {
        return serializer.deserialize(in);
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
