package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.data.DataType;
import com.cafiaso.server.network.data.DataTypes;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EnumDataTypeTest {

    private static final EnumDataType<City, Integer, DataType<Integer>> VAR_INT_INSTANCE = new EnumDataType<>(City.class, DataTypes.VAR_INT, City::getDistrict);
    private static final EnumDataType<City, String, DataType<String>> STRING_INSTANCE = new EnumDataType<>(City.class, DataTypes.STRING, City::getName);

    @Test
    void read_VarInt_OK() throws IOException {
        // Given
        byte[] bytes1 = {0x2a}; // 42
        NetworkBuffer in1 = new NetworkByteBuffer(bytes1);

        // When
        City value1 = VAR_INT_INSTANCE.read(in1);

        // Then
        assertEquals(City.SAINT_ETIENNE, value1);

        // Given
        byte[] bytes2 = new byte[]{0x45}; // 69
        NetworkBuffer in2 = new NetworkByteBuffer(bytes2);

        // When
        City value2 = VAR_INT_INSTANCE.read(in2);

        // Then
        assertEquals(City.LYON, value2);

        // Given
        byte[] bytes3 = new byte[]{0x00}; // 0

        NetworkBuffer in3 = new NetworkByteBuffer(bytes3);

        // When/Then
        assertThrowsExactly(IOException.class, () -> VAR_INT_INSTANCE.read(in3));
    }

    @Test
    void read_String_OK() throws IOException {
        // Given
        byte[] bytes1 = {4, 'l', 'y', 'o', 'n'};
        NetworkBuffer in1 = new NetworkByteBuffer(bytes1);

        // When
        City value1 = STRING_INSTANCE.read(in1);

        // Then
        assertEquals(City.LYON, value1);

        // Given
        byte[] bytes2 = new byte[]{13, 's', 'a', 'i', 'n', 't', '_', 'e', 't', 'i', 'e', 'n', 'n', 'e'};
        NetworkBuffer in2 = new NetworkByteBuffer(bytes2);

        // When
        City value2 = STRING_INSTANCE.read(in2);

        // Then
        assertEquals(City.SAINT_ETIENNE, value2);
    }

    @Test
    void write_VarInt_OK() throws IOException {
        // Given
        NetworkBuffer out1 = new NetworkByteBuffer();

        City value1 = City.SAINT_ETIENNE;

        // When
        VAR_INT_INSTANCE.write(value1, out1);

        // Then
        assertArrayEquals(new byte[]{0x2a}, out1.toByteArray()); // 42

        // Given
        NetworkBuffer out2 = new NetworkByteBuffer();

        City value2 = City.LYON;

        // When
        VAR_INT_INSTANCE.write(value2, out2);

        // Then
        assertArrayEquals(new byte[] {0x45}, out2.toByteArray()); // 69
    }

    @Test
    void write_String_OK() throws IOException {
        // Given
        City value = City.LYON;

        NetworkBuffer out1 = new NetworkByteBuffer();

        // When
        STRING_INSTANCE.write(value, out1);

        // Then
        assertArrayEquals(new byte[]{4, 'l', 'y', 'o', 'n'}, out1.toByteArray());

        // Given
        value = City.SAINT_ETIENNE;

        NetworkBuffer out2 = new NetworkByteBuffer();

        // When
        STRING_INSTANCE.write(value, out2);

        // Then
        assertArrayEquals(new byte[]{13, 's', 'a', 'i', 'n', 't', '_', 'e', 't', 'i', 'e', 'n', 'n', 'e'}, out2.toByteArray());
    }

    @Test
    void read_ShouldThrowException_WhenEnumIsInvalid() {
        // Given
        byte[] bytes = new byte[]{5, 'p', 'a', 'r', 'i', 's'};
        NetworkBuffer in = new NetworkByteBuffer(bytes);

        // When/Then
        assertThrowsExactly(IOException.class, () -> STRING_INSTANCE.read(in));
    }

    private enum City {
        LYON(69, "lyon"),
        SAINT_ETIENNE(42, "saint_etienne"),;

        private final int district;
        private final String name;

        City(int district, String name) {
            this.district = district;
            this.name = name;
        }

        public int getDistrict() {
            return district;
        }

        public String getName() {
            return name;
        }
    }
}
