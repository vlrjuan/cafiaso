package org.cafiaso.server.network.types;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class UnsignedShortDataTypeTest {

    private static final int VALUE = 25565;
    private static final byte[] DATA = {0x63, (byte) 0xdd};

    private static final UnsignedShortDataType DATA_TYPE = new UnsignedShortDataType();

    @Test
    void read_ShouldReadUnsignedShort() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(DATA);
        DataInputStream dataIn = new DataInputStream(in);

        int value = DATA_TYPE.read(dataIn);

        assertEquals(VALUE, value);
    }

    @Test
    void write_ShouldWriteUnsignedShort() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        DATA_TYPE.write(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(DATA, bytes);
    }
}
