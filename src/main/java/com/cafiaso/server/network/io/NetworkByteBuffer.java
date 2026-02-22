package com.cafiaso.server.network.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * A {@link NetworkBuffer} implementation that wraps a {@link ByteBuffer}.
 */
public class NetworkByteBuffer implements NetworkBuffer {

    private static final int DEFAULT_BUFFER_SIZE = 8192 * 4;

    private final ByteBuffer buffer;

    public NetworkByteBuffer() {
        buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    }

    public NetworkByteBuffer(byte[] data) {
        buffer = ByteBuffer.wrap(data);
    }

    @Override
    public byte readByte() {
        return buffer.get();
    }

    @Override
    public short readShort() {
        return buffer.getShort();
    }

    @Override
    public long readLong() {
        return buffer.getLong();
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);

        return bytes;
    }

    @Override
    public void read(WritableByteChannel channel) throws IOException {
        channel.write(buffer);
    }

    @Override
    public void writeByte(byte value) {
        buffer.put(value);
    }

    @Override
    public void writeShort(short value) {
        buffer.putShort(value);
    }

    @Override
    public void writeBytes(byte[] bytes) {
        buffer.put(bytes);
    }

    @Override
    public void writeLong(long value) {
        buffer.putLong(value);
    }

    @Override
    public void write(NetworkBuffer buffer) {
        if (!(buffer instanceof NetworkByteBuffer byteBuffer)) {
            throw new IllegalArgumentException("Buffer must be an instance of NetworkByteBuffer");
        }

        buffer.flip(); // Flips the given buffer to prepare it for reading

        this.buffer.put(byteBuffer.buffer);
    }

    @Override
    public int write(ReadableByteChannel channel) throws IOException {
       return channel.read(buffer);
    }

    @Override
    public boolean hasRemainingBytes() {
        return buffer.hasRemaining();
    }

    @Override
    public int getRemainingBytes() {
        return buffer.remaining();
    }

    @Override
    public int getPosition() {
        return buffer.position();
    }

    @Override
    public void mark() {
        buffer.mark();
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    @Override
    public void flip() {
        buffer.flip();
    }

    @Override
    public void compact() {
        buffer.compact();
    }

    @Override
    public void clear() {
        buffer.clear();
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = new byte[buffer.position()];
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        readOnlyBuffer.position(0);
        readOnlyBuffer.get(bytes);

        return bytes;
    }

    @Override
    public NetworkBuffer copy(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);

        return new NetworkByteBuffer(bytes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NetworkByteBuffer that)) {
            return false;
        }

        ByteBuffer thisCopy = this.buffer.asReadOnlyBuffer();
        ByteBuffer thatCopy = that.buffer.asReadOnlyBuffer();

        if (thisCopy.limit() != thatCopy.limit()) {
            return false;
        }

        thisCopy.position(0);
        thatCopy.position(0);

        while (thisCopy.hasRemaining()) {
            if (thisCopy.get() != thatCopy.get()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "NetworkByteBuffer{" +
                "buffer=" + buffer +
                '}';
    }
}
