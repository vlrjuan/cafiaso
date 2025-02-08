package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ByteArraySerializerTest {

    private static final ByteArraySerializer BYTE_ARRAY_SERIALIZER = new ByteArraySerializer();

    @Test
    void deserialize_ShouldDeserializeByteArray() throws IOException {
        byte[] bytes = {0x01, 0x02, 0x03};

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);

        byte[] value = BYTE_ARRAY_SERIALIZER.deserialize(dataIn);

        assertArrayEquals(bytes, value);
    }

    @Test
    void serialize_ShouldSerializeByteArray() throws IOException {
        byte[] value = {0x01, 0x02, 0x03};

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        BYTE_ARRAY_SERIALIZER.serialize(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(value, bytes);
    }
}
