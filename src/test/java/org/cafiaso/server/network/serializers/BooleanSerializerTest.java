package org.cafiaso.server.network.serializers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BooleanSerializerTest {

    private static final byte[] TRUE_BYTES = {0x01};
    private static final byte[] FALSE_BYTES = {0x00};

    private static final BooleanSerializer BOOLEAN_SERIALIZER = new BooleanSerializer();

    @Test
    void deserialize_ShouldDeserializeTrueBoolean() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(TRUE_BYTES);
        DataInputStream dataIn = new DataInputStream(in);

        boolean value = BOOLEAN_SERIALIZER.deserialize(dataIn);

        assertTrue(value);
    }

    @Test
    void deserialize_ShouldDeserializeFalseBoolean() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(FALSE_BYTES);
        DataInputStream dataIn = new DataInputStream(in);

        boolean value = BOOLEAN_SERIALIZER.deserialize(dataIn);

        assertFalse(value);
    }

    @Test
    void deserialize_ShouldThrowException_WhenInvalidBooleanIsRead() {
        byte[] bytes = new byte[]{0x02}; // invalid

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> BOOLEAN_SERIALIZER.deserialize(dataIn), "Invalid value");
    }

    @Test
    void serialize_ShouldSerializeTrueBoolean() throws IOException {
        boolean value = true;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        BOOLEAN_SERIALIZER.serialize(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(TRUE_BYTES, bytes);
    }

    @Test
    void serialize_ShouldSerializeFalseBoolean() throws IOException {
        boolean value = false;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        BOOLEAN_SERIALIZER.serialize(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(FALSE_BYTES, bytes);
    }
}
