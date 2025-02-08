package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LongSerializerTest {

    private static final byte[] SERIALIZED_VALUE = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
    private static final long VALUE = 72623859790382856L;

    private static final LongSerializer SERIALIZER = new LongSerializer();

    @Test
    void deserialize_ShouldDeserializeLong() throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(SERIALIZED_VALUE));

        long value = SERIALIZER.deserialize(in);

        assertEquals(VALUE, value);
    }

    @Test
    void serialize_ShouldSerializeLong() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        SERIALIZER.serialize(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(SERIALIZED_VALUE, bytes);
    }
}
