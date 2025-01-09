package org.cafiaso.server.network.types;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class StringDataTypeTest {

    private static final String VALUE = "Hello, World!";
    private static final byte[] DATA = {0x0D, 'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};

    private static final StringDataType DATA_TYPE = new StringDataType();

    @Test
    void read_ShouldReadString() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(DATA);
        DataInputStream dataIn = new DataInputStream(in);

        String value = DATA_TYPE.read(dataIn);

        assertEquals(VALUE, value);
    }

    @Test
    void write_ShouldWriteString() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        DATA_TYPE.write(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertEquals(DATA.length, bytes.length);
        assertArrayEquals(DATA, bytes);
    }
}
