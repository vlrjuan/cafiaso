package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.data.DataType;
import com.cafiaso.server.network.io.NetworkBuffer;

import java.io.IOException;

/**
 * A {@link DataType} for VarInt values.
 * <p>
 * VarInts are variable-length integers.
 * <p>
 * Bounds: [-2,147,483,648, 2,147,483,647]
 * <p>
 * It allows for efficient storage of integers by using fewer bytes for smaller values.
 * <p>
 * Some examples of VarInt values and their corresponding representation in decimal:
 * <ul>
 *     <li>0x00 -> 0</li>
 *     <li>0x7F -> 127</li>
 *     <li>0x80 0x01 -> 128</li>
 *     <li>0xdd 0xc7 0x01 -> 25565</li>
 * </ul>
 *
 * @see <a href="https://wiki.vg/Protocol#VarInt_and_VarLong">VarInt Type</a>
 */
public class VarIntDataType implements DataType<Integer> {

    private static final int SEGMENT_BITS = 0x7f;
    private static final int CONTINUE_BIT = 0x80;

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Integer read(NetworkBuffer in) throws IOException {
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
    public void write(Integer value, NetworkBuffer out) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                out.writeByte(value.byteValue());

                return;
            }

            out.writeByte((byte) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }
}
