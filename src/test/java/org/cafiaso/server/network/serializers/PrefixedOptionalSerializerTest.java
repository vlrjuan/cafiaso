package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PrefixedOptionalSerializerTest {

    private static final String STRING_VALUE = "lyon";
    private static final byte[] DESERIALIZED_STRING_VALUE = {1, 4, 'l', 'y', 'o', 'n'};

    private static final PrefixedOptionalSerializer<Integer, Serializer<Integer>> OPTIONAL_VAR_INT_SERIALIZER = new PrefixedOptionalSerializer<>(Serializer.VAR_INT);
    private static final PrefixedOptionalSerializer<String, Serializer<String>> OPTIONAL_STRING_SERIALIZER = new PrefixedOptionalSerializer<>(Serializer.STRING);

    @Test
    void deserialize_ShouldDeserializeOptionalVarInt() throws IOException {
        byte[] bytes = {1, 0x2a}; // present, 42

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);

        Optional<Integer> value = OPTIONAL_VAR_INT_SERIALIZER.deserialize(dataIn);

        assertTrue(value.isPresent());
        assertEquals(42, value.get());

        bytes = new byte[]{0}; // not present

        in = new ByteArrayInputStream(bytes);
        dataIn = new DataInputStream(in);

        value = OPTIONAL_VAR_INT_SERIALIZER.deserialize(dataIn);

        assertFalse(value.isPresent());
    }

    @Test
    void serialize_ShouldSerializeOptionalVarInt() throws IOException {
        Optional<Integer> value = Optional.of(42);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        OPTIONAL_VAR_INT_SERIALIZER.serialize(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertEquals(2, bytes.length);
        assertEquals(1, bytes[0]); // present
        assertEquals(0x2a, bytes[1]); // 42

        value = Optional.empty();

        out = new ByteArrayOutputStream();
        dataOut = new DataOutputStream(out);

        OPTIONAL_VAR_INT_SERIALIZER.serialize(dataOut, value);

        bytes = out.toByteArray();

        assertEquals(1, bytes.length);
        assertEquals(0, bytes[0]); // not present
    }

    @Test
    void deserialize_ShouldDeserializeOptionalString() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(DESERIALIZED_STRING_VALUE);
        DataInputStream dataIn = new DataInputStream(in);

        Optional<String> value = OPTIONAL_STRING_SERIALIZER.deserialize(dataIn);

        assertTrue(value.isPresent());
        assertEquals(STRING_VALUE, value.get());

        byte[] bytes = new byte[]{0}; // not present

        in = new ByteArrayInputStream(bytes);
        dataIn = new DataInputStream(in);

        value = OPTIONAL_STRING_SERIALIZER.deserialize(dataIn);

        assertFalse(value.isPresent());
    }

    @Test
    void serialize_ShouldSerializeOptionalString() throws IOException {
        Optional<String> value = Optional.of(STRING_VALUE);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        OPTIONAL_STRING_SERIALIZER.serialize(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertArrayEquals(DESERIALIZED_STRING_VALUE, bytes);

        value = Optional.empty();

        out = new ByteArrayOutputStream();
        dataOut = new DataOutputStream(out);

        OPTIONAL_STRING_SERIALIZER.serialize(dataOut, value);

        bytes = out.toByteArray();

        assertEquals(1, bytes.length);
        assertEquals(0, bytes[0]); // not present
    }
}
