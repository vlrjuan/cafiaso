package org.cafiaso.server.network.types;

import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VarIntDataType implements DataType<Integer> {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    /**
     * Gets the size of the given value.
     * <p>
     * Mainly used to calculate the packet length.
     *
     * @param value the value
     * @return the size of the value
     */
    public static int getSize(Integer value) {
        int size = 0;

        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                size++;

                return size;
            }

            size++;
            value >>>= 7;
        }
    }

    /**
     * Converts the given value to a byte array.
     * <p>
     * Mainly used for testing purposes.
     *
     * @param value the value
     * @return the byte array
     */
    public static byte[] toByteArray(Integer value) {
        byte[] bytes = new byte[getSize(value)];

        int index = 0;

        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                bytes[index] = value.byteValue();

                return bytes;
            }

            bytes[index] = (byte) ((value & SEGMENT_BITS) | CONTINUE_BIT);

            value >>>= 7;
            index++;
        }
    }

    @Override
    public Integer read(DataInputStream in) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = in.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new IOException("VarInt is too big");
        }

        return value;
    }

    @Override
    public void write(DataOutputStream out, Integer value) throws IOException {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                out.writeByte(value);

                return;
            }

            out.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);

            value >>>= 7;
        }
    }
}
