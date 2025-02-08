package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EnumSerializerTest {

    private static final EnumSerializer<EnumVarIntTest, Integer, Serializer<Integer>> ENUM_VAR_SERIALIZER = new EnumSerializer<>(EnumVarIntTest.class, Serializer.VAR_INT, EnumVarIntTest::getValue);
    private static final EnumSerializer<EnumStringTest, String, Serializer<String>> ENUM_STRING_SERIALIZER = new EnumSerializer<>(EnumStringTest.class, Serializer.STRING, EnumStringTest::getValue);

    @Test
    void deserialize_ShouldDeserializeEnumVarInt() throws IOException {
        byte[] bytes = {0x2a}; // 42

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);

        EnumVarIntTest value = ENUM_VAR_SERIALIZER.deserialize(dataIn);

        assertEquals(EnumVarIntTest.LOIRE, value);

        bytes = new byte[]{0x45}; // 69

        in = new ByteArrayInputStream(bytes);
        dataIn = new DataInputStream(in);

        value = ENUM_VAR_SERIALIZER.deserialize(dataIn);

        assertEquals(EnumVarIntTest.RHONE, value);

        bytes = new byte[]{0x00}; // 0

        in = new ByteArrayInputStream(bytes);
        DataInputStream finalDataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> ENUM_VAR_SERIALIZER.deserialize(finalDataIn), "Invalid enum value");
    }

    @Test
    void deserialize_ShouldDeserializeEnumString() throws IOException {
        byte[] bytes = {4, 'l', 'y', 'o', 'n'};

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);

        EnumStringTest value = ENUM_STRING_SERIALIZER.deserialize(dataIn);

        assertEquals(EnumStringTest.LYON, value);

        bytes = new byte[]{13, 's', 'a', 'i', 'n', 't', '_', 'e', 't', 'i', 'e', 'n', 'n', 'e'};

        in = new ByteArrayInputStream(bytes);
        dataIn = new DataInputStream(in);

        value = ENUM_STRING_SERIALIZER.deserialize(dataIn);

        assertEquals(EnumStringTest.SAINT_ETIENNE, value);
    }

    @Test
    void serialize_ShouldSerializeEnumVarInt() throws IOException {
        EnumVarIntTest value = EnumVarIntTest.LOIRE;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        ENUM_VAR_SERIALIZER.serialize(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertEquals(1, bytes.length);
        assertEquals(0x2a, bytes[0]); // 42

        value = EnumVarIntTest.RHONE;

        out = new ByteArrayOutputStream();
        dataOut = new DataOutputStream(out);

        ENUM_VAR_SERIALIZER.serialize(dataOut, value);

        bytes = out.toByteArray();
        assertEquals(1, bytes.length);
        assertEquals(0x45, bytes[0]); // 69
    }

    @Test
    void serialize_ShouldSerializeEnumString() throws IOException {
        EnumStringTest value = EnumStringTest.LYON;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        ENUM_STRING_SERIALIZER.serialize(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertEquals(5, bytes.length);
        assertArrayEquals(new byte[]{4, 'l', 'y', 'o', 'n'}, bytes);

        value = EnumStringTest.SAINT_ETIENNE;

        out = new ByteArrayOutputStream();
        dataOut = new DataOutputStream(out);

        ENUM_STRING_SERIALIZER.serialize(dataOut, value);

        bytes = out.toByteArray();

        assertEquals(14, bytes.length);
        assertArrayEquals(new byte[]{13, 's', 'a', 'i', 'n', 't', '_', 'e', 't', 'i', 'e', 'n', 'n', 'e'}, bytes);
    }

    @Test
    void deserialize_ShouldThrowExceptionOnInvalidEnum() {
        byte[] bytes = new byte[]{5, 'p', 'a', 'r', 'i', 's'};

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> ENUM_STRING_SERIALIZER.deserialize(dataIn), "Invalid enum value");
    }

    private enum EnumVarIntTest {
        LOIRE(42),
        RHONE(69);

        private final int value;

        EnumVarIntTest(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private enum EnumStringTest {
        LYON("lyon"),
        SAINT_ETIENNE("saint_etienne");

        private final String value;

        EnumStringTest(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
