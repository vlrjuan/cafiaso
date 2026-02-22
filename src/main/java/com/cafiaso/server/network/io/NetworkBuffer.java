package com.cafiaso.server.network.io;

import com.cafiaso.server.network.data.DataType;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * A buffer that provides utility methods for reading and writing data.
 * <p>
 * This class is mainly designed to be used to deserialize/serialize packets.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * NetworkBuffer buffer = new NetworkByteBuffer();
 *
 * int protocolVersion = in.read(DataTypes.VAR_INT);
 * String serverAddress = in.read(DataTypes.STRING(255));
 * int serverPort = in.read(DataTypes.UNSIGNED_SHORT);
 * Intention intention = in.read(
 *  DataTypes.ENUM(
 *      Intention.class,
 *      DataTypes.VAR_INT
 *  )
 * );
 * }
 * </pre>
 */
public interface NetworkBuffer {

    /**
     * Reads a byte from the buffer.
     *
     * @return the byte read from the buffer
     */
    byte readByte();

    /**
     * Reads a short from the buffer.
     *
     * @return the short read from the buffer
     */
    short readShort();

    /**
     * Reads a long from the buffer.
     *
     * @return the long read from the buffer
     */
    long readLong();

    /**
     * Reads the specified number of bytes from the buffer.
     *
     * @param length the number of bytes to read
     * @return the byte array containing the read bytes
     */
    byte[] readBytes(int length);

    /**
     * Reads a value from the buffer.
     * <p>
     * Example:
     * <pre>{@code
     * class Packet implements ClientPacket {
     *   private int payload;
     *
     *   @Override
     *   public void read(NetworkBuffer in) throws IOException {
     *     payload = in.read(DataType.VAR_INT);
     *   }
     * }
     * }</pre>
     *
     * @param type the data type to read
     * @param <T>  the type of the data
     * @return the value read from the buffer
     * @throws IOException if an I/O error occurs while reading the data
     * @see DataType
     */
    default <T> T read(DataType<T> type) throws IOException {
        return type.read(this);
    }

    /**
     * Writes the contents of the buffer into the specified channel.
     *
     * @param channel the channel to read from
     * @throws IOException if an I/O error occurs while reading from the channel
     */
    void read(WritableByteChannel channel) throws IOException;

    /**
     * Writes a byte to the buffer.
     *
     * @param value the byte to write
     */
    void writeByte(byte value);

    /**
     * Writes a short to the buffer.
     *
     * @param value the short to write
     */
    void writeShort(short value);

    /**
     * Writes a long to the buffer.
     *
     * @param value the long to write
     */
    void writeLong(long value);

    /**
     * Writes a byte array to the buffer.
     *
     * @param bytes the byte array to write
     */
    void writeBytes(byte[] bytes);

    /**
     * Writes a value of the specified data type to the buffer.
     * <p>
     * Example:
     * <pre>{@code
     * class Packet implements ServerPacket {
     *   private final int payload;
     *
     *   public Packet(int payload) {
     *     this.payload = payload;
     *   }
     *
     *   @Override
     *   public void write(NetworkBuffer out) throws IOException {
     *     out.write(payload, DataType.VAR_INT);
     *   }
     * }
     * }</pre>
     *
     * @param <T>   the type of the data
     * @param type  the data type to write
     * @param value the value to write
     * @throws IOException if an I/O error occurs while writing the data
     */
    default <T> void write(DataType<T> type, T value) throws IOException {
        type.write(value, this);
    }

    /**
     * Flips the given buffer and writes its contents to this buffer.
     *
     * @param buffer the buffer to write from
     */
    void write(NetworkBuffer buffer);

    /**
     * Writes the contents of the given channel to this buffer.
     *
     * @param channel the channel to write from
     * @return the number of bytes written
     * @throws IOException if an I/O error occurs while writing to the channel
     */
    int write(ReadableByteChannel channel) throws IOException;

    /**
     * Checks if the buffer has remaining bytes.
     *
     * @return {@code true} if there are remaining bytes, {@code false} otherwise
     */
    boolean hasRemainingBytes();

    /**
     * Gets the number of remaining bytes in the buffer.
     *
     * @return the number of remaining bytes
     */
    int getRemainingBytes();

    /**
     * Gets the current position of the buffer.
     *
     * @return the current position
     */
    int getPosition();

    /**
     * Marks the current position of the buffer.
     */
    void mark();

    /**
     * Resets the buffer to the marked position.
     */
    void reset();

    /**
     * Flips the buffer.
     */
    void flip();

    /**
     * Compacts the buffer.
     */
    void compact();

    /**
     * Clears the buffer.
     */
    void clear();

    /**
     * Gets the contents of the buffer as a byte array (from 0 to position).
     * <p>
     * After this operation, the buffer position is set to the limit.
     *
     * @return the byte array containing the buffer contents
     */
    byte[] toByteArray();

    /**
     * Copies the specified number of bytes from the buffer.
     *
     * @param length the number of bytes to copy
     * @return a new {@link NetworkBuffer} containing the copied bytes
     */
    NetworkBuffer copy(int length);
}
