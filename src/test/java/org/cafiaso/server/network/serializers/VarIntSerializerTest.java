package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VarIntSerializerTest {

    private static final byte[] SERIALIZED_VALUE = {(byte) 0xdd, (byte) 0xc7, 0x01};
    private static final int VALUE = 25565;

    private static final VarIntSerializer SERIALIZER = new VarIntSerializer();

    @Test
    void deserialize_ShouldDeserializeVarInt() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(SERIALIZED_VALUE);
        DataInputStream dataIn = new DataInputStream(in);

        int value = SERIALIZER.deserialize(dataIn);

        assertEquals(VALUE, value);
    }

    @Test
    void serialize_ShouldSerializeVarInt() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        SERIALIZER.serialize(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(SERIALIZED_VALUE, bytes);
    }
}
