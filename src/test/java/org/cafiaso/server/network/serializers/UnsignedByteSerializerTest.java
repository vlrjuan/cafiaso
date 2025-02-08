package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UnsignedByteSerializerTest {

    private static final UnsignedByteSerializer SERIALIZER = new UnsignedByteSerializer();

    @Test
    void deserialize_ShouldDeserializeUnsignedByte() throws IOException {
        byte[] serializedValue = {0x7F};
        ByteArrayInputStream in = new ByteArrayInputStream(serializedValue);
        DataInputStream dataIn = new DataInputStream(in);

        byte value = SERIALIZER.deserialize(dataIn);

        assertEquals(0x7F, value);
    }

    @Test
    void serialize_ShouldSerializeUnsignedByte() throws IOException {
        byte[] expectedValue = {0x7F};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        SERIALIZER.serialize(dataOut, (byte) 0x7F);

        assertArrayEquals(expectedValue, out.toByteArray());
    }
}
