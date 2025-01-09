package org.cafiaso.server.network.types;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class VarIntDataTypeTest {

    private static final int VALUE = 25565;
    private static final byte[] DATA = {(byte) 0xdd, (byte) 0xc7, 0x01};

    private static final VarIntDataType DATA_TYPE = new VarIntDataType();

    @Test
    void read_ShouldReadVarInt() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(DATA);
        DataInputStream dataIn = new DataInputStream(in);

        int value = DATA_TYPE.read(dataIn);

        assertEquals(VALUE, value);
    }

    @Test
    void write_ShouldWriteVarInt() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        DATA_TYPE.write(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(DATA, bytes);
    }

    @Test
    void getSize_ShouldReturnSize() {
        int[] values = {25565, 42, 0};
        int[] sizes = {3, 1, 1};

        for (int i = 0; i < values.length; i++) {
            assertEquals(sizes[i], VarIntDataType.getSize(values[i]));
        }
    }
}
