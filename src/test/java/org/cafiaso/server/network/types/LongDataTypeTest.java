package org.cafiaso.server.network.types;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LongDataTypeTest {

    private static final long VALUE = 72623859790382856L;
    private static final byte[] DATA = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

    private static final LongDataType DATA_TYPE = new LongDataType();

    @Test
    void read_ShouldReadLong() throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(DATA));

        long value = DATA_TYPE.read(in);

        assertEquals(VALUE, value);
    }

    @Test
    void write_ShouldWriteLong() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        DATA_TYPE.write(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(DATA, bytes);
    }
}
