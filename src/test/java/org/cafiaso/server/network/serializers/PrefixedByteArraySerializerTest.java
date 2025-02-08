package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PrefixedByteArraySerializerTest {

    private static final byte[] SERIALIZED_VALUE = {0x03, 0x01, 0x02, 0x03}; // length prefix 3, followed by 3 bytes
    private static final byte[] VALUE = {0x01, 0x02, 0x03};

    private static final PrefixedByteArraySerializer PREFIXED_BYTE_ARRAY_SERIALIZER = new PrefixedByteArraySerializer();

    @Test
    void deserialize_ShouldDeserializePrefixedByteArray() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(SERIALIZED_VALUE);
        DataInputStream dataIn = new DataInputStream(in);

        byte[] value = PREFIXED_BYTE_ARRAY_SERIALIZER.deserialize(dataIn);

        assertArrayEquals(VALUE, value);
    }

    @Test
    void serialize_ShouldSerializePrefixedByteArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        PREFIXED_BYTE_ARRAY_SERIALIZER.serialize(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(SERIALIZED_VALUE, bytes);
    }
}
