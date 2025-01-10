package org.cafiaso.server.network.types;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UnsignedByteDataTypeTest {

    private static final int VALUE = 128;
    private static final byte[] DATA = {(byte) 0x80};

    private static final UnsignedByteDataType DATA_TYPE = new UnsignedByteDataType();

    @Test
    public void read_ShouldReadUnsignedByte() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(DATA);
        DataInputStream dataIn = new DataInputStream(in);

        int value = DATA_TYPE.read(dataIn);

        assertEquals(VALUE, value);
    }

    @Test
    public void write_ShouldWriteUnsignedByte() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        DATA_TYPE.write(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(DATA, bytes);
    }
}
