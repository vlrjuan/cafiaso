package org.cafiaso.server.network.types;

import org.cafiaso.server.network.DataType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EnumDataTypeTest {

    private static final EnumDataType<EnumVarIntTest, Integer, DataType<Integer>> ENUM_VAR_INT_DATA_TYPE = new EnumDataType<>(EnumVarIntTest.class, DataType.VAR_INT, EnumVarIntTest::getValue);
    private static final EnumDataType<EnumStringTest, String, DataType<String>> ENUM_STRING_DATA_TYPE = new EnumDataType<>(EnumStringTest.class, DataType.STRING, EnumStringTest::getValue);

    @Test
    void read_ShouldReadEnumVarInt() throws IOException {
        byte[] bytes = {0x2a}; // 42

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);

        EnumVarIntTest value = ENUM_VAR_INT_DATA_TYPE.read(dataIn);

        assertEquals(EnumVarIntTest.LOIRE, value);

        bytes = new byte[]{0x45}; // 69

        in = new ByteArrayInputStream(bytes);
        dataIn = new DataInputStream(in);

        value = ENUM_VAR_INT_DATA_TYPE.read(dataIn);

        assertEquals(EnumVarIntTest.RHONE, value);

        bytes = new byte[]{0x00}; // 0

        in = new ByteArrayInputStream(bytes);
        DataInputStream finalDataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> ENUM_VAR_INT_DATA_TYPE.read(finalDataIn), "Invalid enum value");
    }

    @Test
    void read_ShouldReadEnumString() throws IOException {
        byte[] bytes = {4, 'l', 'y', 'o', 'n'};

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);

        EnumStringTest value = ENUM_STRING_DATA_TYPE.read(dataIn);

        assertEquals(EnumStringTest.LYON, value);

        bytes = new byte[]{13, 's', 'a', 'i', 'n', 't', '_', 'e', 't', 'i', 'e', 'n', 'n', 'e'};

        in = new ByteArrayInputStream(bytes);
        dataIn = new DataInputStream(in);

        value = ENUM_STRING_DATA_TYPE.read(dataIn);

        assertEquals(EnumStringTest.SAINT_ETIENNE, value);

        bytes = new byte[]{5, 'p', 'a', 'r', 'i', 's'};

        in = new ByteArrayInputStream(bytes);
        DataInputStream finalDataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> ENUM_STRING_DATA_TYPE.read(finalDataIn), "Invalid enum value");
    }

    @Test
    void write_ShouldWriteEnumVarInt() throws IOException {
        EnumVarIntTest value = EnumVarIntTest.LOIRE;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        ENUM_VAR_INT_DATA_TYPE.write(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertEquals(1, bytes.length);
        assertEquals(0x2a, bytes[0]); // 42

        value = EnumVarIntTest.RHONE;

        out = new ByteArrayOutputStream();
        dataOut = new DataOutputStream(out);

        ENUM_VAR_INT_DATA_TYPE.write(dataOut, value);

        bytes = out.toByteArray();
        assertEquals(1, bytes.length);
        assertEquals(0x45, bytes[0]); // 69
    }

    @Test
    void write_ShouldWriteEnumString() throws IOException {
        EnumStringTest value = EnumStringTest.LYON;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        ENUM_STRING_DATA_TYPE.write(dataOut, value);

        byte[] bytes = out.toByteArray();

        assertEquals(5, bytes.length);
        assertArrayEquals(new byte[]{4, 'l', 'y', 'o', 'n'}, bytes);

        value = EnumStringTest.SAINT_ETIENNE;

        out = new ByteArrayOutputStream();
        dataOut = new DataOutputStream(out);

        ENUM_STRING_DATA_TYPE.write(dataOut, value);

        bytes = out.toByteArray();

        assertEquals(14, bytes.length);
        assertArrayEquals(new byte[]{13, 's', 'a', 'i', 'n', 't', '_', 'e', 't', 'i', 'e', 'n', 'n', 'e'}, bytes);
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
