package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UUIDSerializerTest {

    private static final byte[] SERIALIZED_VALUE = {
            0x12, 0x3e, 0x45, 0x67, (byte) 0xe8, (byte) 0x9b, 0x12, (byte) 0xd3,
            (byte) 0xa4, 0x56, 0x42, 0x66, 0x14, 0x17, 0x40, 0x00
    };
    private static final UUID VALUE = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    private static final UUIDSerializer SERIALIZER = new UUIDSerializer();

    @Test
    void deserialize_ShouldDeserializeUUID() throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(SERIALIZED_VALUE));

        UUID value = SERIALIZER.deserialize(in);

        assertEquals(VALUE, value);
    }

    @Test
    void serialize_ShouldSerializeUUID() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        SERIALIZER.serialize(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(SERIALIZED_VALUE, bytes);
    }
}
