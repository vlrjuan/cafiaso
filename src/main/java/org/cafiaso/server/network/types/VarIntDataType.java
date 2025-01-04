package org.cafiaso.server.network.types;

import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VarIntDataType implements DataType<Integer> {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

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

            if (position >= 32) throw new RuntimeException("VarInt is too big");
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

    @Override
    public int getSize(Integer value) {
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
}
