package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ByteSerializerTest {

    private static final ByteSerializer BYTE_SERIALIZER = new ByteSerializer();

    @Test
    void deserialize_ShouldDeserializeByte() throws IOException {
        byte expected = 0x01;

        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{expected});
        DataInputStream dataIn = new DataInputStream(in);

        byte value = BYTE_SERIALIZER.deserialize(dataIn);

        assertEquals(expected, value);
    }

    @Test
    void serialize_ShouldSerializeByte() throws IOException {
        byte value = 0x01;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        BYTE_SERIALIZER.serialize(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(new byte[]{value}, bytes);
    }
}
