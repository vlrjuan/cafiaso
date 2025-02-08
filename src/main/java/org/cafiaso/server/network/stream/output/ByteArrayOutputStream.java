package org.cafiaso.server.network.stream.output;

import org.cafiaso.server.network.Serializer;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * {@link OutputStream} implementation that writes to a byte array.
 */
public class ByteArrayOutputStream implements OutputStream {

    private final java.io.ByteArrayOutputStream out;
    private final DataOutputStream dataOut;

    /**
     * ByteArrayOutputStream constructor.
     */
    public ByteArrayOutputStream() {
        this.out = new java.io.ByteArrayOutputStream();
        this.dataOut = new DataOutputStream(this.out);
    }

    @Override
    public <T> void write(Serializer<T> serializer, T value) throws IOException {
        serializer.serialize(dataOut, value);
    }

    /**
     * Returns the contents of this output stream
     *
     * @return the contents of this output stream
     */
    public byte[] toByteArray() {
        return this.out.toByteArray();
    }

    @Override
    public void close() throws IOException {
        dataOut.close();
    }
}
