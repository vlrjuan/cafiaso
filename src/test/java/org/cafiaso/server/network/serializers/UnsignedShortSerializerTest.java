package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UnsignedShortSerializerTest {

    private static final byte[] SERIALIZED_VALUE = {0x63, (byte) 0xdd};
    private static final int VALUE = 25565;

    private static final UnsignedShortSerializer SERIALIZER = new UnsignedShortSerializer();

    @Test
    void deserialize_ShouldDeserializeUnsignedShort() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(SERIALIZED_VALUE);
        DataInputStream dataIn = new DataInputStream(in);

        int value = SERIALIZER.deserialize(dataIn);

        assertEquals(VALUE, value);
    }

    @Test
    void serialize_ShouldSerializeUnsignedShort() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        SERIALIZER.serialize(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(SERIALIZED_VALUE, bytes);
    }
}
