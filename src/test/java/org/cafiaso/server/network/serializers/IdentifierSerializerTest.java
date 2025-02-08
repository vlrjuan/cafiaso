package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierSerializerTest {

    private static final IdentifierSerializer SERIALIZER = new IdentifierSerializer();

    @Test
    void deserialize_ShouldDeserializeIdentifier() throws IOException {
        String value = "minecraft:stone";
        byte[] serializedValue = {0xf, 'm', 'i', 'n', 'e', 'c', 'r', 'a', 'f', 't', ':', 's', 't', 'o', 'n', 'e'};

        ByteArrayInputStream in = new ByteArrayInputStream(serializedValue);
        DataInputStream dataIn = new DataInputStream(in);

        String deserializedValue = SERIALIZER.deserialize(dataIn);

        assertEquals(value, deserializedValue);
    }

    @Test
    void serialize_ShouldSerializeIdentifier() throws IOException {
        String value = "minecraft:stone";
        byte[] expectedSerializedValue = {0xf, 'm', 'i', 'n', 'e', 'c', 'r', 'a', 'f', 't', ':', 's', 't', 'o', 'n', 'e'};

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        SERIALIZER.serialize(dataOut, value);

        byte[] serializedValue = out.toByteArray();

        assertArrayEquals(expectedSerializedValue, serializedValue);
    }
}
